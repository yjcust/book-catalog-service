package com.example.bookcatalog.controller;

import com.example.bookcatalog.dto.BookDTO;
import com.example.bookcatalog.model.Book;
import com.example.bookcatalog.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateBook() throws Exception {
        BookDTO dto = new BookDTO();
        dto.setTitle("Test");
        dto.setAuthor("Tester");
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test");
        book.setAuthor("Tester");
        Mockito.when(bookService.createBook(any(BookDTO.class))).thenReturn(book);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test"))
                .andExpect(jsonPath("$.author").value("Tester"));
    }

    @Test
    void testGetBook() throws Exception {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test");
        book.setAuthor("Tester");
        Mockito.when(bookService.getBookById(1L)).thenReturn(book);

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test"))
                .andExpect(jsonPath("$.author").value("Tester"));
    }

    @Test
    void testListBooks() throws Exception {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test");
        book.setAuthor("Tester");
        Mockito.when(bookService.listBooks()).thenReturn(Arrays.asList(book));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test"))
                .andExpect(jsonPath("$[0].author").value("Tester"));
    }

    @Test
    void testUpdateBook() throws Exception {
        BookDTO dto = new BookDTO();
        dto.setTitle("Updated");
        dto.setAuthor("Tester2");
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Updated");
        book.setAuthor("Tester2");
        Mockito.when(bookService.updateBook(eq(1L), any(BookDTO.class))).thenReturn(book);

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"))
                .andExpect(jsonPath("$.author").value("Tester2"));
    }

    @Test
    void testDeleteBook() throws Exception {
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isOk());
        Mockito.verify(bookService, Mockito.times(1)).deleteBook(1L);
    }
}