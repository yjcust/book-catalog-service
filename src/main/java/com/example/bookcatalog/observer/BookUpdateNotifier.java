package com.example.bookcatalog.observer;


import com.example.bookcatalog.model.Book;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


public class BookUpdateNotifier {

    private final List<BookUpdateObserver> observers = new ArrayList<>();

    public void register(BookUpdateObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers(Book updatedBook) {
        for (BookUpdateObserver observer : observers) {
            observer.onBookUpdated(updatedBook);
        }
    }
}
