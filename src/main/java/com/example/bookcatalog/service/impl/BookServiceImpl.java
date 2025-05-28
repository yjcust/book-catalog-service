package com.example.bookcatalog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.bookcatalog.dto.BookDTO;
import com.example.bookcatalog.factory.BookFactory;
import com.example.bookcatalog.mapper.BookMapper;
import com.example.bookcatalog.model.Book;
import com.example.bookcatalog.observer.BookUpdateNotifier;
import com.example.bookcatalog.observer.LogObserver;
import com.example.bookcatalog.service.BookService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class BookServiceImpl implements BookService {

    private BookMapper bookMapper;

    private BookUpdateNotifier notifier;


    public BookServiceImpl(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }


    @PostConstruct
    public void initObservers() {
        BookUpdateNotifier notifier = new BookUpdateNotifier();
        LogObserver logObserver = new LogObserver();
        notifier.register(logObserver);
        this.notifier = notifier;
    }


    @Override
    public Book createBook(BookDTO dto) {
        // 工厂模式
        Book book = BookFactory.createBook(dto);
        bookMapper.insert(book);
        return book;
    }

    @Override
    public Book getBookById(Long id) {
        return bookMapper.selectById(id);
    }

    @Override
    public List<Book> listBooks() {
        return bookMapper.selectList(new QueryWrapper<>());
    }

    @Override
    public Book updateBook(Long id, BookDTO dto) {
        // 工厂模式
        Book book = BookFactory.createBook(dto);
        bookMapper.updateById(book);
        //观察者模式
        notifier.notifyObservers(book);
        return book;
    }


    @Override
    public void deleteBook(Long id) {
        bookMapper.deleteById(id);
    }
}