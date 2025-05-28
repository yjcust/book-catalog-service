package com.example.bookcatalog.observer;


import com.example.bookcatalog.model.Book;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


public class BookUpdateNotifier {
    private final List<BookObserver> observers = new ArrayList<>();

    public void register(BookObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers(Book book) {
        observers.forEach(o -> o.onBookUpdated(book));
    }
}

