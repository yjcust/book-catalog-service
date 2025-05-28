package com.example.bookcatalog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.bookcatalog.dto.BookDTO;
import com.example.bookcatalog.enums.BookStatusEnum;
import com.example.bookcatalog.exception.BusinessException;
import com.example.bookcatalog.exception.ErrorCode;
import com.example.bookcatalog.factory.BookFactory;
import com.example.bookcatalog.mapper.BookMapper;
import com.example.bookcatalog.model.Book;
import com.example.bookcatalog.observer.BookUpdateNotifier;
import com.example.bookcatalog.observer.CacheObserver;
import com.example.bookcatalog.observer.LogObserver;
import com.example.bookcatalog.observer.RecommendObserver;
import com.example.bookcatalog.service.BookService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;
    private BookUpdateNotifier notifier;

    private Set<String> sensitiveWords;

    public BookServiceImpl(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    @PostConstruct
    public void initObservers() {
        this.notifier = new BookUpdateNotifier();
        notifier.register(new LogObserver());
        notifier.register(new CacheObserver());
        notifier.register(new RecommendObserver());
        //模拟敏感词
        sensitiveWords = new HashSet<>();
        sensitiveWords.add("政治");
        sensitiveWords.add("暴力");
        sensitiveWords.add("色情");
    }

    @Override
    public Book createBook(BookDTO dto) {
        validateBookDTO(dto);

        //通过工厂转换为实体对象，实现对象创建逻辑与业务逻辑解耦。
        //使用场景：当输入格式可能变化时，集中处理转换逻辑更便于维护。
        Book book = BookFactory.createBook(dto);

        //新增书籍时判断数据如果出现敏感词这图书进入待审核状态
        if (isSensitiveWords(book.getDescription())) {
            log.warn("新增书籍时判断数据出现敏感词 {}", book.getDescription());
            book.setStatus(BookStatusEnum.UNDER_REVIEW.getCode());
        }
        bookMapper.insert(book);
        //观察者模式，在数据更新后会触发一系列的动作，如刷新缓存、记录日志、推送书籍等，如需扩展仅需要加入到观察者列表中。
        notifier.notifyObservers(book);
        return book;
    }

    @Override
    public Book getBookById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "图书 ID 非法");
        }

        Book book = bookMapper.selectById(id);
        if (book == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "图书不存在");
        }

        return book;
    }

    @Override
    public List<Book> listBooks() {
        return bookMapper.selectList(new QueryWrapper<>());
    }

    @Override
    public Book updateBook(Long id, BookDTO dto) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "图书 ID 非法");
        }

        validateBookDTO(dto);
        //通过工厂转换为实体对象，实现对象创建逻辑与业务逻辑解耦。
        //使用场景：当输入格式可能变化时，集中处理转换逻辑更便于维护。
        Book book = BookFactory.createBook(dto);

        book.setId(id);
        bookMapper.updateById(book);

        //观察者模式，在数据更新后会触发一系列的动作，如刷新缓存、记录日志、推送书籍等，如需扩展仅需要加入到观察者列表中。
        notifier.notifyObservers(book);
        return book;
    }

    @Override
    public void deleteBook(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "图书 ID 非法");
        }

        bookMapper.deleteById(id);
    }

    private void validateBookDTO(BookDTO dto) {
        if (dto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "图书信息不能为空");
        }

        if (!StringUtils.hasText(dto.getTitle())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题不能为空");
        }

        if (!StringUtils.hasText(dto.getAuthor())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "作者不能为空");
        }

        if (!StringUtils.hasText(dto.getIsbn())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "ISBN 不能为空");
        }
    }


    private boolean isSensitiveWords(String str) {

        if (str == null) {
            return false;
        }
        for (String word : sensitiveWords) {
            if (str.contains(word)) {
                return true;
            }
        }
        return false;
    }

    ;
}
