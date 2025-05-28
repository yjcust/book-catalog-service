package com.example.bookcatalog.observer;

import com.example.bookcatalog.model.Book;

public class CacheObserver implements BookObserver {
    @Override
    public void onBookUpdated(Book book) {
        // 模拟更新缓存
        System.out.println("缓存更新：书籍 " + book.getId());
    }
}
