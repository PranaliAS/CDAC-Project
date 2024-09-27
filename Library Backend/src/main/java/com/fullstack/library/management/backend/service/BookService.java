package com.fullstack.library.management.backend.service;

import com.fullstack.library.management.backend.entity.Book;
import org.springframework.data.domain.Page;
import java.util.List;

public interface BookService {
    List<Book> getAllBooks();

    Page<Book> getAllBooksPageWise(int page, int size);

    Book getBookByBookId(Long bookId);

    Page<Book> getBookTitleContainingPageWise(String bookTitle, int page, int size);

    Page<Book> getBookCategoryContainingPageWise(String bookCategory, int page, int size);
}
