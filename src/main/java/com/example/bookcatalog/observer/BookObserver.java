package com.example.bookcatalog.observer;

import com.example.bookcatalog.model.Book;

public interface BookObserver {
    void onBookUpdated(Book book);
}
