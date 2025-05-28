package com.example.bookcatalog.factory;

import com.example.bookcatalog.model.Book;

public class TechBookProcessor implements BookProcessor {
    @Override
    public void process(Book book) {
        book.setTag("[技术]");
    }
}
