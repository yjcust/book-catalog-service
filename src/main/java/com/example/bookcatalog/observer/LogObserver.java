package com.example.bookcatalog.observer;


import com.example.bookcatalog.model.Book;
import lombok.extern.slf4j.Slf4j;



@Slf4j
public class LogObserver implements BookObserver {
    @Override
    public void onBookUpdated(Book book) {
        log.info("图书更新日志: {}", book);
    }
}

