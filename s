[1mdiff --git a/pom.xml b/pom.xml[m
[1mindex 0f17e0a..750d2e3 100644[m
[1m--- a/pom.xml[m
[1m+++ b/pom.xml[m
[36m@@ -2,7 +2,6 @@[m
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"[m
          xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">[m
     <modelVersion>4.0.0</modelVersion>[m
[31m-[m
     <groupId>com.example</groupId>[m
     <artifactId>book-catalog-service</artifactId>[m
     <version>1.0.0</version>[m
[36m@@ -104,4 +103,4 @@[m
             </plugin>[m
         </plugins>[m
     </build>[m
[31m-</project>[m
\ No newline at end of file[m
[32m+[m[32m</project>[m
[1mdiff --git a/src/main/java/com/example/bookcatalog/enums/BookStatusEnum.java b/src/main/java/com/example/bookcatalog/enums/BookStatusEnum.java[m
[1mnew file mode 100644[m
[1mindex 0000000..96af385[m
[1m--- /dev/null[m
[1m+++ b/src/main/java/com/example/bookcatalog/enums/BookStatusEnum.java[m
[36m@@ -0,0 +1,17 @@[m
[32m+[m[32mpackage com.example.bookcatalog.enums;[m
[32m+[m
[32m+[m[32mimport lombok.Getter;[m
[32m+[m
[32m+[m[32m@Getter[m
[32m+[m[32mpublic enum BookStatusEnum {[m
[32m+[m[32m    NORMAL(0, "正常"),[m
[32m+[m[32m    UNDER_REVIEW(1, "审核中");[m
[32m+[m
[32m+[m[32m    private final int code;[m
[32m+[m[32m    private final String message;[m
[32m+[m
[32m+[m[32m    BookStatusEnum(int code, String message) {[m
[32m+[m[32m        this.code = code;[m
[32m+[m[32m        this.message = message;[m
[32m+[m[32m    }[m
[32m+[m[32m}[m
[1mdiff --git a/src/main/java/com/example/bookcatalog/factory/BookFactory.java b/src/main/java/com/example/bookcatalog/factory/BookFactory.java[m
[1mindex b638843..64ba9ab 100644[m
[1m--- a/src/main/java/com/example/bookcatalog/factory/BookFactory.java[m
[1m+++ b/src/main/java/com/example/bookcatalog/factory/BookFactory.java[m
[36m@@ -1,6 +1,7 @@[m
 package com.example.bookcatalog.factory;[m
 [m
 import com.example.bookcatalog.dto.BookDTO;[m
[32m+[m[32mimport com.example.bookcatalog.enums.BookStatusEnum;[m
 import com.example.bookcatalog.model.Book;[m
 [m
 public class BookFactory {[m
[36m@@ -11,6 +12,7 @@[m [mpublic class BookFactory {[m
                 .author(dto.getAuthor())[m
                 .isbn(dto.getIsbn())[m
                 .description(dto.getDescription())[m
[32m+[m[32m                .status(BookStatusEnum.NORMAL.getCode())[m
                 .build();[m
 [m
         //考虑到不同类型的书籍可能有不同的处理方式，因此不同类型的书使用不同类型的书籍处理器进行加工。目前仅简单的加个标签[m
[1mdiff --git a/src/main/java/com/example/bookcatalog/model/Book.java b/src/main/java/com/example/bookcatalog/model/Book.java[m
[1mindex f034599..7b59a9b 100644[m
[1m--- a/src/main/java/com/example/bookcatalog/model/Book.java[m
[1m+++ b/src/main/java/com/example/bookcatalog/model/Book.java[m
[36m@@ -20,5 +20,7 @@[m [mpublic class Book {[m
     private String isbn;[m
     private String description;[m
     private String tag;[m
[32m+[m[32m    //0 正常 1审核中[m
[32m+[m[32m    private int status;[m
 [m
 }[m
\ No newline at end of file[m
[1mdiff --git a/src/main/java/com/example/bookcatalog/service/impl/BookServiceImpl.java b/src/main/java/com/example/bookcatalog/service/impl/BookServiceImpl.java[m
[1mindex 9dae34f..fd3a27c 100644[m
[1m--- a/src/main/java/com/example/bookcatalog/service/impl/BookServiceImpl.java[m
[1m+++ b/src/main/java/com/example/bookcatalog/service/impl/BookServiceImpl.java[m
[36m@@ -2,6 +2,7 @@[m [mpackage com.example.bookcatalog.service.impl;[m
 [m
 import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;[m
 import com.example.bookcatalog.dto.BookDTO;[m
[32m+[m[32mimport com.example.bookcatalog.enums.BookStatusEnum;[m
 import com.example.bookcatalog.exception.BusinessException;[m
 import com.example.bookcatalog.exception.ErrorCode;[m
 import com.example.bookcatalog.factory.BookFactory;[m
[36m@@ -13,17 +14,23 @@[m [mimport com.example.bookcatalog.observer.LogObserver;[m
 import com.example.bookcatalog.observer.RecommendObserver;[m
 import com.example.bookcatalog.service.BookService;[m
 import jakarta.annotation.PostConstruct;[m
[32m+[m[32mimport lombok.extern.slf4j.Slf4j;[m
 import org.springframework.stereotype.Service;[m
 import org.springframework.util.StringUtils;[m
 [m
[32m+[m[32mimport java.util.HashSet;[m
 import java.util.List;[m
[32m+[m[32mimport java.util.Set;[m
 [m
 @Service[m
[32m+[m[32m@Slf4j[m
 public class BookServiceImpl implements BookService {[m
 [m
     private final BookMapper bookMapper;[m
     private BookUpdateNotifier notifier;[m
 [m
[32m+[m[32m    private Set<String> sensitiveWords;[m
[32m+[m
     public BookServiceImpl(BookMapper bookMapper) {[m
         this.bookMapper = bookMapper;[m
     }[m
[36m@@ -34,6 +41,11 @@[m [mpublic class BookServiceImpl implements BookService {[m
         notifier.register(new LogObserver());[m
         notifier.register(new CacheObserver());[m
         notifier.register(new RecommendObserver());[m
[32m+[m[32m        //模拟敏感词[m
[32m+[m[32m        sensitiveWords = new HashSet<>();[m
[32m+[m[32m        sensitiveWords.add("政治");[m
[32m+[m[32m        sensitiveWords.add("暴力");[m
[32m+[m[32m        sensitiveWords.add("色情");[m
     }[m
 [m
     @Override[m
[36m@@ -43,6 +55,12 @@[m [mpublic class BookServiceImpl implements BookService {[m
         //通过工厂转换为实体对象，实现对象创建逻辑与业务逻辑解耦。[m
         //使用场景：当输入格式可能变化时，集中处理转换逻辑更便于维护。[m
         Book book = BookFactory.createBook(dto);[m
[32m+[m
[32m+[m[32m        //新增书籍时判断数据如果出现敏感词这图书进入待审核状态[m
[32m+[m[32m        if (isSensitiveWords(book.getDescription())) {[m
[32m+[m[32m            log.warn("新增书籍时判断数据出现敏感词 {}", book.getDescription());[m
[32m+[m[32m            book.setStatus(BookStatusEnum.UNDER_REVIEW.getCode());[m
[32m+[m[32m        }[m
         bookMapper.insert(book);[m
         //观察者模式，在数据更新后会触发一系列的动作，如刷新缓存、记录日志、推送书籍等，如需扩展仅需要加入到观察者列表中。[m
         notifier.notifyObservers(book);[m
[36m@@ -113,4 +131,20 @@[m [mpublic class BookServiceImpl implements BookService {[m
             throw new BusinessException(ErrorCode.PARAMS_ERROR, "ISBN 不能为空");[m
         }[m
     }[m
[32m+[m
[32m+[m
[32m+[m[32m    private boolean isSensitiveWords(String str) {[m
[32m+[m
[32m+[m[32m        if (str == null) {[m
[32m+[m[32m            return false;[m
[32m+[m[32m        }[m
[32m+[m[32m        for (String word : sensitiveWords) {[m
[32m+[m[32m            if (str.contains(word)) {[m
[32m+[m[32m                return true;[m
[32m+[m[32m            }[m
[32m+[m[32m        }[m
[32m+[m[32m        return false;[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    ;[m
 }[m
[1mdiff --git a/src/test/java/com/example/bookcatalog/service/BookServiceImplTest.java b/src/test/java/com/example/bookcatalog/service/BookServiceImplTest.java[m
[1mindex 82743e7..f7f8023 100644[m
[1m--- a/src/test/java/com/example/bookcatalog/service/BookServiceImplTest.java[m
[1m+++ b/src/test/java/com/example/bookcatalog/service/BookServiceImplTest.java[m
[36m@@ -1,6 +1,7 @@[m
 package com.example.bookcatalog.service;[m
 [m
 import com.example.bookcatalog.dto.BookDTO;[m
[32m+[m[32mimport com.example.bookcatalog.enums.BookStatusEnum;[m
 import com.example.bookcatalog.exception.BusinessException;[m
 import com.example.bookcatalog.exception.ErrorCode;[m
 import com.example.bookcatalog.mapper.BookMapper;[m
[36m@@ -42,6 +43,21 @@[m [mclass BookServiceImplTest {[m
         verify(bookMapper, times(1)).insert(any(Book.class));[m
     }[m
 [m
[32m+[m[32m    @Test[m
[32m+[m[32m    void testCreateBook_with_sensitive_wordsInput_shouldSucceed() {[m
[32m+[m[32m        BookDTO dto = new BookDTO();[m
[32m+[m[32m        dto.setTitle("Java Guide");[m
[32m+[m[32m        dto.setAuthor("Tom");[m
[32m+[m[32m        dto.setIsbn("1234567890");[m
[32m+[m[32m        //触发铭感词分支[m
[32m+[m[32m        dto.setDescription("政治");[m
[32m+[m
[32m+[m[32m        Book result = bookService.createBook(dto);[m
[32m+[m[32m        //状态为待审核[m
[32m+[m[32m        assertEquals(BookStatusEnum.UNDER_REVIEW.getCode(), result.getStatus());[m
[32m+[m[32m        verify(bookMapper, times(1)).insert(any(Book.class));[m
[32m+[m[32m    }[m
[32m+[m
     @Test[m
     void testCreateBook_withInvalidTitle_shouldThrowException() {[m
         BookDTO dto = new BookDTO();[m
