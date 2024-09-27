package com.fullstack.library.management.backend.serviceImplementation;

import com.fullstack.library.management.backend.SpringBootLibraryManagementApplicationTests;
import com.fullstack.library.management.backend.entity.Book;
import com.fullstack.library.management.backend.mockEntityDtoData.mockData;
import com.fullstack.library.management.backend.repository.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.isNull;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNotSame;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,classes = SpringBootLibraryManagementApplicationTests.class)
@ActiveProfiles("test")
public class BookServiceImplementationTest extends mockData
{
    @Value("${sql.create.table.book}")
    private String createTableBookQuery;

    @Value("${sql.insert.book}")
    private String insertDataBookQuery;

    @Value("${sql.delete.book}")
    private String deleteBookQuery;

    @Value("${sql.drop.book}")
    private String dropBookQuery;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImplementation bookServiceImplementation;

    @BeforeEach
    private void setDatabaseBeforeTransaction()
    {
        jdbcTemplate.execute(createTableBookQuery);
        jdbcTemplate.execute(insertDataBookQuery);
    }

    @Test
    @DisplayName("Get all Books : Positive")
    void getAllBooksPositiveTest()
    {
        // context
        when(bookRepository.findAll()).thenReturn(mockBookListPositiveData());
        // event
        List<Book> savedBook = bookServiceImplementation.getAllBooks();
        // outcome
        assertNotNull(savedBook);
    }

    @Test
    @DisplayName("Get all Books : Negative")
    void getAllBooksNegativeTest()
    {
        // context
        when(bookRepository.findAll()).thenReturn(mockBookListNegativeData());
        // event
        List<Book> savedBook = bookServiceImplementation.getAllBooks();
        // outcome
        assertNotEquals(savedBook.size(),2);
    }

    @Test
    @DisplayName("Get all Books Page Wise : Positive")
    void getAllBooksPageWisePositiveTest()
    {
        Page<Book> mockBookPage = mock(Page.class);
        Pageable bookPageable = PageRequest.of(0,5);
        // context
        when(bookRepository.findAll(bookPageable)).thenReturn(mockBookPage);
        // event
        Page<Book> booksPageWise = bookServiceImplementation.getAllBooksPageWise(0,5);
        // outcome
        assertEquals(mockBookPage.getTotalElements(),booksPageWise.getTotalElements());
    }

    @Test
    @DisplayName("Get all Books Page Wise : Negative")
    void getAllBooksPageWiseNegativeTest()
    {
        Page<Book> mockBookPage = mock(Page.class);
        Pageable bookPageable = PageRequest.of(0,5);
        // context
        when(bookRepository.findAll(bookPageable)).thenReturn(mockBookPage);
        // event
        Page<Book> booksPageWise = bookServiceImplementation.getAllBooksPageWise(0,5);
        // outcome
        verify(bookRepository.findAll(bookPageable),times(0)).isEmpty();
    }

    @Test
    @DisplayName("Get Book By Book Id : Positive")
    void getBookByBookIdPositiveTest()
    {
        // context
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(mockBookData()));
        // event
        Book books = bookServiceImplementation.getBookByBookId(anyLong());
        // outcome
        assertNotNull(mockBookData());
    }

    @Test
    @DisplayName("Get Book By Book Id : Negative")
    void getBookByBookIdNegativeTest()
    {
        // context
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(mockBookData()));
        // event
        Book books = bookServiceImplementation.getBookByBookId(anyLong());
        // outcome
        assertNotSame(mockBookData(),books);
    }

    @Test
    @DisplayName("Get Book Title Containing Page Wise : Positive")
    void getBookTitleContainingPageWisePositiveTest()
    {
        Page<Book> mockBookPage = mock(Page.class);
        Pageable bookPageable = PageRequest.of(0,5);
        // context
        when(bookRepository.findByBookTitleContaining("SpringBoot",bookPageable)).thenReturn(mockBookPage);
        // event
        Page<Book> booksPageWise = bookServiceImplementation.getBookTitleContainingPageWise("SpringBoot",0,5);
        // outcome
        assertEquals(mockBookPage.getTotalElements(),booksPageWise.getTotalElements());
    }

    @Test
    @DisplayName("Get Book Title Containing Page Wise : Negative")
    void getBookTitleContainingPageWiseNegativeTest()
    {
        Page<Book> mockBookPage = mock(Page.class);
        Pageable bookPageable = PageRequest.of(0,5);
        // context
        when(bookRepository.findByBookTitleContaining("test",bookPageable)).thenReturn(mockBookPage);
        // event
        Page<Book> booksPageWise = bookServiceImplementation.getBookTitleContainingPageWise("test",0,5);
        // outcome
        verify(bookRepository.findByBookTitleContaining("test",bookPageable),times(0)).isEmpty();
    }

    @Test
    @DisplayName("Get Book Category Containing Page Wise : Positive")
    void getBookCategoryContainingPageWisePositiveTest()
    {
        Page<Book> mockBookPage = mock(Page.class);
        Pageable bookPageable = PageRequest.of(0,5);
        // context
        when(bookRepository.findByBookCategoryContaining("FE",bookPageable)).thenReturn(mockBookPage);
        // event
        Page<Book> booksPageWise = bookServiceImplementation.getBookCategoryContainingPageWise("FE",0,5);
        // outcome
        assertEquals(mockBookPage.getTotalElements(),booksPageWise.getTotalElements());
    }

    @Test
    @DisplayName("Get Book Category Containing Page Wise : Negative")
    void getBookCategoryContainingPageWiseNegativeTest()
    {
        Page<Book> mockBookPage = mock(Page.class);
        Pageable bookPageable = PageRequest.of(0,5);
        // context
        when(bookRepository.findByBookCategoryContaining("FE",bookPageable)).thenReturn(mockBookPage);
        // event
        Page<Book> booksPageWise = bookServiceImplementation.getBookCategoryContainingPageWise("FE",0,5);
        // outcome
        verify(bookRepository.findByBookCategoryContaining("FE",bookPageable),times(0)).isEmpty();
    }

    @AfterEach
    private void setDatabaseAfterTransaction()
    {
        jdbcTemplate.execute(deleteBookQuery);
        jdbcTemplate.execute(dropBookQuery);
    }
}
