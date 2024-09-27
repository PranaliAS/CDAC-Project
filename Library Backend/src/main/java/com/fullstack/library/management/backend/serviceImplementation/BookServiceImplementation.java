package com.fullstack.library.management.backend.serviceImplementation;

import com.fullstack.library.management.backend.entity.Book;
import com.fullstack.library.management.backend.repository.BookRepository;
import com.fullstack.library.management.backend.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
public class BookServiceImplementation implements BookService
{
    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Book> getAllBooks()
    {
        return bookRepository.findAll();
    }

    @Override
    public Page<Book> getAllBooksPageWise(int page,int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return bookRepository.findAll(pageable);
    }

    @Override
    public Book getBookByBookId(Long bookId)
    {
        return bookRepository.findById(bookId).get();
    }

    @Override
    public Page<Book> getBookTitleContainingPageWise(String bookTitle,int page,int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return bookRepository.findByBookTitleContaining(bookTitle,pageable);
    }

    @Override
    public Page<Book> getBookCategoryContainingPageWise(String bookCategory,int page,int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return bookRepository.findByBookCategoryContaining(bookCategory,pageable);
    }
}
