package com.example.bookcatalog.observer;

import com.example.bookcatalog.model.Book;

public class RecommendObserver implements BookObserver {
    @Override
    public void onBookUpdated(Book book) {
        // 模拟刷新推荐系统
        System.out.println("推荐系统已感知书籍更新：" + book.getTitle());
    }
}
