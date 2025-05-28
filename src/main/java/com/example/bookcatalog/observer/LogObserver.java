package com.example.bookcatalog.observer;


import com.example.bookcatalog.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LogObserver implements BookUpdateObserver {

    private static final Logger logger = LoggerFactory.getLogger(LogObserver.class);

    @Override
    public void onBookUpdated(Book updatedBook) {
        logger.info("Book updated: {}", updatedBook);
    }
}
