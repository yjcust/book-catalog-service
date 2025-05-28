package com.example.bookcatalog.factory;

import com.example.bookcatalog.dto.BookDTO;
import com.example.bookcatalog.model.Book;

public class BookFactory {
    public static Book createBook(BookDTO dto) {
        Book book = Book.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .isbn(dto.getIsbn())
                .description(dto.getDescription())
                .build();

        //考虑到不同类型的书籍可能有不同的处理方式，因此不同类型的书使用不同类型的书籍处理器进行加工。目前仅简单的加个标签
        BookProcessor processor = getProcessorByCategory(dto);
        processor.process(book);
        return book;
    }

    private static BookProcessor getProcessorByCategory(BookDTO dto) {
        if (dto.getTitle().contains("Java")) {
            return new TechBookProcessor();
        } else {
            return new NovelBookProcessor();
        }
    }
}
