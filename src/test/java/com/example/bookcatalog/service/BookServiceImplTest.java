package com.example.bookcatalog.service;

import com.example.bookcatalog.dto.BookDTO;
import com.example.bookcatalog.exception.BusinessException;
import com.example.bookcatalog.exception.ErrorCode;
import com.example.bookcatalog.mapper.BookMapper;
import com.example.bookcatalog.model.Book;
import com.example.bookcatalog.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    private BookMapper bookMapper;
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        bookMapper = mock(BookMapper.class);
        bookService = new BookServiceImpl(bookMapper);
        bookService.initObservers(); // 初始化观察者
    }

    @Test
    void testCreateBook_withValidInput_shouldSucceed() {
        BookDTO dto = new BookDTO();
        dto.setTitle("Java Guide");
        dto.setAuthor("Tom");
        dto.setIsbn("1234567890");
        dto.setDescription("Java 101");

        Book result = bookService.createBook(dto);

        assertEquals(dto.getTitle(), result.getTitle());
        verify(bookMapper, times(1)).insert(any(Book.class));
    }

    @Test
    void testCreateBook_withInvalidTitle_shouldThrowException() {
        BookDTO dto = new BookDTO();
        dto.setTitle(" "); // invalid
        dto.setAuthor("Tom");
        dto.setIsbn("123");
        dto.setDescription("desc");

        BusinessException ex = assertThrows(BusinessException.class, () -> bookService.createBook(dto));
        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), ex.getCode());

    }

    @Test
    void testGetBookById_shouldReturnBook() {
        Book book = Book.builder()
                .id(1L)
                .title("Book A")
                .author("Author A")
                .isbn("1111")
                .description("desc")
                .build();

        when(bookMapper.selectById(1L)).thenReturn(book);

        Book result = bookService.getBookById(1L);
        assertEquals("Book A", result.getTitle());
        verify(bookMapper, times(1)).selectById(1L);
    }

    @Test
    void testListBooks_shouldReturnList() {
        when(bookMapper.selectList(any())).thenReturn(Collections.emptyList());

        List<Book> list = bookService.listBooks();
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    void testUpdateBook_withValidInput_shouldSucceedAndNotifyObserver() {
        BookDTO dto = new BookDTO();
        dto.setId(1L);
        dto.setTitle("Updated Title");
        dto.setAuthor("Updated Author");
        dto.setIsbn("9999");
        dto.setDescription("Updated Desc");

        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);

        Book updated = bookService.updateBook(1L, dto);

        assertEquals("Updated Title", updated.getTitle());
        verify(bookMapper, times(1)).updateById(captor.capture());
        assertEquals("Updated Title", captor.getValue().getTitle());
    }

    @Test
    void testUpdateBook_withNullId_shouldThrowException() {
        BookDTO dto = new BookDTO();
        dto.setTitle("test");

        BusinessException ex = assertThrows(BusinessException.class, () -> bookService.updateBook(null, dto));
        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), ex.getCode());
    }

    @Test
    void testUpdateBook_withMismatchedIds_shouldThrowException() {
        BookDTO dto = new BookDTO();
        dto.setId(2L);

        BusinessException ex = assertThrows(BusinessException.class, () -> bookService.updateBook(1L, dto));
        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), ex.getCode());

    }

    @Test
    void testDeleteBook_shouldCallMapper() {
        bookService.deleteBook(5L);
        verify(bookMapper, times(1)).deleteById(5L);
    }
}
