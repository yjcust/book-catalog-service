package com.example.bookcatalog.factory;

import com.example.bookcatalog.dto.BookDTO;
import com.example.bookcatalog.model.Book;

public class BookFactory {
    public static Book createBook(BookDTO dto) {
        Book book = new Book();
        book.setId(dto.getId());
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setIsbn(dto.getIsbn());
        book.setDescription(dto.getDescription());
        return book;
    }
}