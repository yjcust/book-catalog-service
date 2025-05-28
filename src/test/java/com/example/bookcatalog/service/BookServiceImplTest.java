package com.example.bookcatalog.service;

import com.example.bookcatalog.dto.BookDTO;
import com.example.bookcatalog.mapper.BookMapper;
import com.example.bookcatalog.model.Book;
import com.example.bookcatalog.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private BookMapper bookMapper;


    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookService = new BookServiceImpl(bookMapper);
        bookService.initObservers();

    }

    @Test
    void testCreateBook() {
        BookDTO dto = new BookDTO();
        dto.setTitle("Java Book");
        dto.setAuthor("Author");
        dto.setIsbn("123456");
        dto.setDescription("A Java programming book");

        Book book = Book.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .isbn(dto.getIsbn())
                .description(dto.getDescription())
                .build();


        Book result = bookService.createBook(dto);

        verify(bookMapper).insert(book);
        assertEquals("Java Book", result.getTitle());
        assertEquals("A Java programming book", result.getDescription());

    }

    @Test
    void testUpdateBook() {
        BookDTO dto = new BookDTO();
        dto.setTitle("Updated Title");
        dto.setAuthor("Updated Author");
        dto.setIsbn("9999");
        dto.setDescription("Updated Description");

        Book book = Book.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .isbn(dto.getIsbn())
                .description(dto.getDescription())
                .build();

        Book result = bookService.updateBook(1L, dto);

        verify(bookMapper).updateById(book);
        assertEquals("Updated Description", result.getDescription());

    }

    @Test
    void testGetBookById() {
        Book book = Book.builder().id(1L).title("Book").description("desc").build();
        when(bookMapper.selectById(1L)).thenReturn(book);

        Book result = bookService.getBookById(1L);
        assertEquals("Book", result.getTitle());
        assertEquals("desc", result.getDescription());
    }

    @Test
    void testListBooks() {
        Book book = Book.builder().id(1L).title("Book").description("desc").build();
        when(bookMapper.selectList(any())).thenReturn(List.of(book));

        List<Book> result = bookService.listBooks();
        assertEquals(1, result.size());
        assertEquals("desc", result.get(0).getDescription());
    }

    @Test
    void testDeleteBook() {
        bookService.deleteBook(1L);
        verify(bookMapper).deleteById(1L);
    }
}
