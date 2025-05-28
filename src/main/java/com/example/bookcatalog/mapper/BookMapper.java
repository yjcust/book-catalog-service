package com.example.bookcatalog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bookcatalog.model.Book;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookMapper extends BaseMapper<Book> {
}