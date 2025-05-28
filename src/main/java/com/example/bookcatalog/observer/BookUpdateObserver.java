package com.example.bookcatalog.observer;


import com.example.bookcatalog.model.Book;

public interface BookUpdateObserver {
    void onBookUpdated(Book updatedBook);
}
