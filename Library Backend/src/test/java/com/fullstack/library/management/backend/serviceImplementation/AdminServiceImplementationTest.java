package com.fullstack.library.management.backend.serviceImplementation;

import com.fullstack.library.management.backend.SpringBootLibraryManagementApplicationTests;
import com.fullstack.library.management.backend.mockEntityDtoData.mockData;
import com.fullstack.library.management.backend.entity.Book;
import com.fullstack.library.management.backend.entity.Review;
import com.fullstack.library.management.backend.repository.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,classes = SpringBootLibraryManagementApplicationTests.class)
@ActiveProfiles("test")
public class AdminServiceImplementationTest extends mockData
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
    private AdminServiceImplementation adminServiceImplementation;

    @BeforeEach
    private void setDatabaseBeforeTransaction()
    {
        jdbcTemplate.execute(createTableBookQuery);
        jdbcTemplate.execute(insertDataBookQuery);
    }

    @Test
    @DisplayName("Insert New Book : Positive")
    void insertBookPositiveTest() throws Exception
    {
        when(bookRepository.save(any(Book.class))).thenAnswer(i ->{
            Book book = i.getArgument(0);
            book.setBookId(2L);
            return book;
        });
        // context
        adminServiceImplementation.postBook(mockAddBookRequestData());
        // outcome
        verify(bookRepository,times(1)).save(any(Book.class));
    }

    @Test
    @DisplayName("Insert New Book : Negative")
    void insertBookNegativeTest() throws Exception
    {
        // context
        when(bookRepository.save(any(Book.class))).thenReturn(null);
        // event
        adminServiceImplementation.postBook(mockAddBookRequestData());
        // outcome
        assertNotEquals("Arvind",mockBookData().getBookAuthor());
    }

    @Test
    @DisplayName("Increase Book Quantity : Positive")
    void increaseBookQuantityPositiveTest() throws Exception
    {
        Optional<Book> optionalBook = Optional.of(mockBookData());
        when(bookRepository.findById(1L)).thenReturn(optionalBook);
        // context
        optionalBook.get().setCopies(optionalBook.get().getCopies() + 1);
        optionalBook.get().setCopiesAvailable(optionalBook.get().getCopiesAvailable() + 1);
        // event
        when(bookRepository.save(optionalBook.get())).thenReturn(optionalBook.get());
        adminServiceImplementation.increaseBookQuantity(optionalBook.get().getBookId());
        // outcome
        verify(bookRepository,times(1)).save(optionalBook.get());
    }

    @Test
    @DisplayName("Increase Book Quantity : Negative")
    void increaseBookQuantityNegativeTest() throws Exception
    {
        Optional<Book> optionalBook = Optional.of(mockBookData());
        when(bookRepository.findById(1L)).thenReturn(optionalBook);
        // context
        optionalBook.get().setCopies(optionalBook.get().getCopies() + 1);
        optionalBook.get().setCopiesAvailable(optionalBook.get().getCopiesAvailable() + 1);
        // event
        when(bookRepository.save(optionalBook.get())).thenReturn(optionalBook.get());
        adminServiceImplementation.increaseBookQuantity(optionalBook.get().getBookId());
        // outcome
        assertNotEquals("Arvind",mockBookData().getBookAuthor());
    }

    @Test
    @DisplayName("Decrease Book Quantity : Positive")
    void decreaseBookQuantityPositiveTest() throws Exception
    {
        Optional<Book> optionalBook = Optional.of(mockBookData());
        when(bookRepository.findById(1L)).thenReturn(optionalBook);
        // context
        optionalBook.get().setCopies(optionalBook.get().getCopies() - 1);
        optionalBook.get().setCopiesAvailable(optionalBook.get().getCopiesAvailable() - 1);
        // event
        when(bookRepository.save(optionalBook.get())).thenReturn(optionalBook.get());
        adminServiceImplementation.decreaseBookQuantity(optionalBook.get().getBookId());
        // outcome
        verify(bookRepository,times(1)).save(optionalBook.get());
    }

    @Test
    @DisplayName("Decrease Book Quantity : Negative")
    void decreaseBookQuantityNegativeTest() throws Exception
    {
        Optional<Book> optionalBook = Optional.of(mockBookData());
        when(bookRepository.findById(1L)).thenReturn(optionalBook);
        // context
        optionalBook.get().setCopies(optionalBook.get().getCopies() - 1);
        optionalBook.get().setCopiesAvailable(optionalBook.get().getCopiesAvailable() - 1);
        // event
        when(bookRepository.save(optionalBook.get())).thenReturn(optionalBook.get());
        adminServiceImplementation.decreaseBookQuantity(optionalBook.get().getBookId());
        // outcome
        assertNotEquals("Arvind",mockBookData().getBookAuthor());
    }

    @AfterEach
    private void setDatabaseAfterTransaction()
    {
        jdbcTemplate.execute(deleteBookQuery);
        jdbcTemplate.execute(dropBookQuery);
    }

}
