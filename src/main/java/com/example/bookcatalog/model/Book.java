package com.example.bookcatalog.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("book")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @TableId
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String description;
}