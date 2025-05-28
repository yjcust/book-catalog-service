package com.example.bookcatalog.service;

import com.example.bookcatalog.dto.BookDTO;
import com.example.bookcatalog.model.Book;

import java.util.List;

public interface BookService {
    Book createBook(BookDTO dto);
    Book getBookById(Long id);
    List<Book> listBooks();
    Book updateBook(Long id, BookDTO dto);
    void deleteBook(Long id);
}