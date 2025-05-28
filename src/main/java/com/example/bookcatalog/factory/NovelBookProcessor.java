package com.example.bookcatalog.factory;

import com.example.bookcatalog.model.Book;

public class NovelBookProcessor implements BookProcessor {
    @Override
    public void process(Book book) {
        book.setTag("[小说]");
    }
}
