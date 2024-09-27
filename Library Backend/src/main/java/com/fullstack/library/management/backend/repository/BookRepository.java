package com.fullstack.library.management.backend.repository;

import com.fullstack.library.management.backend.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book,Long>
{
    Page<Book> findByBookTitleContaining(@RequestParam("bookTitle") String bookTitle, Pageable pageBooksData);
    Page<Book> findByBookCategoryContaining(@RequestParam("bookCategory") String bookCategory,Pageable pageBooksData);

    @Query("select bo from Book bo where bookId in :bookIds")
    List<Book> findBooksByBookIds(@Param("bookIds") List<Long> bookId);
}
