package com.fullstack.library.management.backend.serviceImplementation;

import com.fullstack.library.management.backend.SpringBootLibraryManagementApplicationTests;
import com.fullstack.library.management.backend.dto.ShelfCurrentLoansResponseDto;
import com.fullstack.library.management.backend.mockEntityDtoData.mockData;
import com.fullstack.library.management.backend.repository.BookRepository;
import com.fullstack.library.management.backend.repository.CheckOutRepository;
import com.fullstack.library.management.backend.repository.HistoryRepository;
import com.fullstack.library.management.backend.entity.CheckOut;
import com.fullstack.library.management.backend.entity.History;
import com.fullstack.library.management.backend.entity.Book;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,classes = SpringBootLibraryManagementApplicationTests.class)
@ActiveProfiles("test")
public class CheckOutServiceImplementationTest extends mockData
{
    @Value("${sql.create.table.checkout}")
    private String createTableCheckOutQuery;

    @Value("${sql.insert.checkout}")
    private String insertDataCheckOutQuery;

    @Value("${sql.delete.checkout}")
    private String deleteCheckOutQuery;

    @Value("${sql.drop.checkout}")
    private String dropCheckOutQuery;

    @Value("${sql.create.table.book}")
    private String createTableBookQuery;

    @Value("${sql.insert.book}")
    private String insertDataBookQuery;

    @Value("${sql.delete.book}")
    private String deleteBookQuery;

    @Value("${sql.drop.book}")
    private String dropBookQuery;

    @Value("${sql.create.table.history}")
    private String createTableHistoryQuery;

    @Value("${sql.insert.history}")
    private String insertDataHistoryQuery;

    @Value("${sql.delete.history}")
    private String deleteHistoryQuery;

    @Value("${sql.drop.history}")
    private String dropHistoryQuery;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Mock
    private CheckOutRepository checkOutRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private HistoryRepository historyRepository;

    @InjectMocks
    private CheckOutServiceImplementation checkOutServiceImplementation;

    @BeforeEach
    private void setDatabaseBeforeTransaction()
    {
        jdbcTemplate.execute(createTableCheckOutQuery);
        jdbcTemplate.execute(createTableBookQuery);
        jdbcTemplate.execute(createTableHistoryQuery);
        jdbcTemplate.execute(insertDataCheckOutQuery);
        jdbcTemplate.execute(insertDataBookQuery);
        jdbcTemplate.execute(insertDataHistoryQuery);
    }

    @Test
    @DisplayName("Return the checkout Book : Positive")
    void returnCheckOutBookPositiveTest() throws Exception
    {
        // optional book
        Optional<Book> bookOptional = Optional.of(new Book(33L,"Angular React Development","Arvind","Good Book for frontend development",9,6,"Frontend"));
        // checkout
        CheckOut userCHeckOutDetails = new CheckOut("nirmala@gmail.com","08-02-2023","22-02-2023",33L);
        when(bookRepository.findById(33L)).thenReturn(bookOptional);
        when(checkOutRepository.findByUserEmailAndBookId("nirmala@gmail.com",33L)).thenReturn(userCHeckOutDetails);
        // optional book save
        when(bookRepository.save(any(Book.class))).thenAnswer(book -> {
            Book optionalBookSave = book.getArgument(0);
            optionalBookSave.setBookId(bookOptional.get().getBookId());
            optionalBookSave.setBookTitle(bookOptional.get().getBookTitle());
            optionalBookSave.setBookAuthor(bookOptional.get().getBookAuthor());
            optionalBookSave.setBookDescription(bookOptional.get().getBookDescription());
            optionalBookSave.setCopies(bookOptional.get().getCopies());
            optionalBookSave.setCopiesAvailable(bookOptional.get().getCopiesAvailable());
            optionalBookSave.setBookCategory(bookOptional.get().getBookCategory());
            return optionalBookSave;
        });
        // history save
        when(historyRepository.save(any(History.class))).thenAnswer(i ->{
            History history = i.getArgument(0);
            history.setHistoryId(2L);
            history.setBookTitle(bookOptional.get().getBookTitle());
            history.setBookAuthor(bookOptional.get().getBookAuthor());
            history.setBookDescription(bookOptional.get().getBookDescription());
            history.setBookImage(bookOptional.get().getBookImage());
            history.setCheckoutDate(userCHeckOutDetails.getCheckoutDate());
            history.setReturnDate(userCHeckOutDetails.getReturnDate());
            return history;
        });
        // context
        checkOutServiceImplementation.returnCheckedOutBook("nirmala@gmail.com", 33L);
        // outcome
        verify(historyRepository,times(1)).save(any(History.class));
    }

    @Test
    @DisplayName("Return the checkout Book : Negative")
    void returnCheckOutBookNegativeTest() throws Exception
    {
        // optional book
        Optional<Book> bookOptional = Optional.of(new Book(33L,"Angular React Development","Arvind","Good Book for frontend development",9,6,"Frontend"));
        // checkout
        CheckOut userCHeckOutDetails = new CheckOut("nirmala@gmail.com","08-02-2023","22-02-2023",33L);
        when(bookRepository.findById(33L)).thenReturn(bookOptional);
        when(checkOutRepository.findByUserEmailAndBookId("nirmala@gmail.com",33L)).thenReturn(userCHeckOutDetails);
        // context
        checkOutServiceImplementation.returnCheckedOutBook("nirmala@gmail.com", 33L);
        // outcome
        assertNotEquals(bookOptional.get().getBookId(),mockBookData().getBookId());
    }

    @Test
    @DisplayName("Renew Loan For Book : Positive")
    void renewLoanForBookPositiveTest() throws Exception
    {
        // checkout
        CheckOut userCHeckOutDetails = new CheckOut("nirmala@gmail.com","08-02-2023","22-02-2023",33L);
        when(checkOutRepository.findByUserEmailAndBookId("nirmala@gmail.com",33L)).thenReturn(userCHeckOutDetails);
        userCHeckOutDetails.setReturnDate(LocalDate.now().plusDays(7).toString());
        when(checkOutRepository.save(any(CheckOut.class))).thenAnswer(i ->{
            CheckOut checkOut = i.getArgument(0);
            checkOut.setReturnDate(userCHeckOutDetails.getReturnDate());
            return checkOut;
        });
        // context
        checkOutServiceImplementation.renewLoan("nirmala@gmail.com", 33L);
        // outcome
        assertNotNull(userCHeckOutDetails.getReturnDate());
    }

    @Test
    @DisplayName("Renew Loan For Book : Negative")
    void renewLoanForBookNegativeTest() throws Exception
    {
        // checkout
        CheckOut userCHeckOutDetails = new CheckOut("nirmala@gmail.com","08-02-2023","22-02-2023",33L);
        when(checkOutRepository.findByUserEmailAndBookId("nirmala@gmail.com",33L)).thenReturn(userCHeckOutDetails);
        // context
        checkOutServiceImplementation.renewLoan("nirmala@gmail.com", 33L);
        // outcome
        assertNotEquals(LocalDate.now().plusDays(7).toString(),userCHeckOutDetails.getReturnDate());
    }

    @Test
    @DisplayName("Get All CheckOut Details : Positive")
    void getAllCheckOutDetailsPositiveTest()
    {
        // context
        when(checkOutRepository.findAll()).thenReturn(mockCheckOutDataListPositiveData());
        // event
        List<CheckOut> savedCheckOutDetailsList = checkOutServiceImplementation.getAllCheckOutDetails();
        // outcome
        assertNotNull(savedCheckOutDetailsList);
    }

    @Test
    @DisplayName("Get All CheckOut Details : Negative")
    void getAllCheckOutDetailsNegativeTest()
    {
        // context
        when(checkOutRepository.findAll()).thenReturn(mockCheckOutDataListNegativeData());
        // event
        List<CheckOut> savedCheckOutDetailsList = checkOutServiceImplementation.getAllCheckOutDetails();
        // outcome
        assertNotEquals(savedCheckOutDetailsList.size(),2);
    }

    @Test
    @DisplayName("Get All CheckOut Details Page Wise : Positive")
    void getAllCheckOutDetailsPageWisePositiveTest()
    {
        Page<CheckOut> mockCheckOutPage = mock(Page.class);
        Pageable checkOutPageable = PageRequest.of(0,5);
        // context
        when(checkOutRepository.findAll(checkOutPageable)).thenReturn(mockCheckOutPage);
        // event
        Page<CheckOut> checkOutDetailsPageWise = checkOutServiceImplementation.getAllCheckoutDetailsPageWise(0,5);
        // outcome
        assertEquals(mockCheckOutPage.getTotalElements(),checkOutDetailsPageWise.getTotalElements());
    }

    @Test
    @DisplayName("Get All CheckOut Details Page Wise : Negative")
    void getAllCheckOutDetailsPageWiseNegativeTest()
    {
        Page<CheckOut> mockCheckOutPage = mock(Page.class);
        Pageable checkOutPageable = PageRequest.of(0,5);
        // context
        when(checkOutRepository.findAll(checkOutPageable)).thenReturn(mockCheckOutPage);
        // event
        Page<CheckOut> checkOutDetailsPageWise = checkOutServiceImplementation.getAllCheckoutDetailsPageWise(0,5);
        // outcome
        verify(checkOutRepository.findAll(checkOutPageable),times(0)).isEmpty();
    }

    @Test
    @DisplayName("Get CheckOut Details By CheckOut Id : Positive")
    void getAllCheckOutByCheckOutIdPositiveTest()
    {
        // context
        when(checkOutRepository.findById(anyLong())).thenReturn(Optional.of(mockCheckOutData()));
        // event
        CheckOut checkOut = checkOutServiceImplementation.getCheckOutDetailsByCheckOutId(anyLong());
        // outcome
        assertNotNull(checkOut);
    }

    @Test
    @DisplayName("Get CheckOut Details By CheckOut Id : Negative")
    void getCheckOutByCheckOutIdNegativeTest()
    {
        // context
        when(checkOutRepository.findById(anyLong())).thenReturn(Optional.of(mockCheckOutData()));
        // event
        CheckOut checkOut = checkOutServiceImplementation.getCheckOutDetailsByCheckOutId(anyLong());
        // outcome
        assertNotSame(mockCheckOutData(),checkOut);
    }

    @Test
    @DisplayName("Is Book Checked Out By User : Positive")
    void checkIsBookCheckedOutByUserPositiveTest()
    {
        when(checkOutRepository.findByUserEmailAndBookId(mockReviewData().getUserEmail(),mockReviewRequestData().getBookId())).thenReturn(mockCheckOutData());
        assertTrue(checkOutServiceImplementation.isBookCheckedOutByUser(mockReviewData().getUserEmail(),mockBookData().getBookId()));
    }

    @Test
    @DisplayName("Is Book Checked Out By User : Negative")
    void checkIsBookCheckedOutByUserNegativeTest()
    {
        when(checkOutRepository.findByUserEmailAndBookId("admin@gmail.com",mockReviewRequestData().getBookId())).thenReturn(null);
        assertFalse(checkOutServiceImplementation.isBookCheckedOutByUser("admin@gmail.com",mockReviewRequestData().getBookId()));
    }

    @Test
    @DisplayName("Current Loans Count : Positive")
    void currentLoansCountPositiveTest() throws Exception {
        when(checkOutRepository.findBooksByUserEmail(mockReviewData().getUserEmail())).thenReturn(mockCheckOutDataListPositiveData());
        int loans  = checkOutServiceImplementation.currentLoansCount(mockReviewData().getUserEmail());
        assertEquals(loans,mockCheckOutDataListPositiveData().size());
    }

    @Test
    @DisplayName("Current Loans Count : Negative")
    void currentLoansCountNegativeTest() throws Exception
    {
        when(checkOutRepository.findBooksByUserEmail(mockReviewData().getUserEmail())).thenReturn(mockCheckOutDataListNegativeData());
        int loans  = checkOutServiceImplementation.currentLoansCount(mockReviewData().getUserEmail());
        assertNotEquals(loans,2);
    }

    @Test
    @DisplayName("Current Loans : Positive")
    void currentLoansPositiveTest() throws Exception
    {
        when(checkOutRepository.findBooksByUserEmail(mockCheckOutData().getUserEmail())).thenReturn(mockCheckOutDataListPositiveData());
        when(bookRepository.findBooksByBookIds(mockListOfBookIds())).thenReturn(mockBookListPositiveData());
        List<ShelfCurrentLoansResponseDto> shelfCurrentLoansResponseDtoList = checkOutServiceImplementation.currentLoans(mockCheckOutData().getUserEmail());
        assertEquals(mockShelfCurrentLoansResponseListPositiveData().size(),shelfCurrentLoansResponseDtoList.size());
    }

    @Test
    @DisplayName("Current Loans : Negative")
    void currentLoansNegativeTest() throws Exception
    {
        when(checkOutRepository.findBooksByUserEmail("admin@gmail.com")).thenReturn(mockCheckOutDataListNegativeData());
        List<ShelfCurrentLoansResponseDto> shelfCurrentLoansResponseDtoList = checkOutServiceImplementation.currentLoans("admin@gmail.com");
        assertNotSame(mockShelfCurrentLoansResponseListNegativeData(),shelfCurrentLoansResponseDtoList);
    }

    @AfterEach
    private void setDatabaseAfterTransaction()
    {
        jdbcTemplate.execute(deleteCheckOutQuery);
        jdbcTemplate.execute(deleteBookQuery);
        jdbcTemplate.execute(deleteHistoryQuery);
        jdbcTemplate.execute(dropCheckOutQuery);
        jdbcTemplate.execute(dropBookQuery);
        jdbcTemplate.execute(dropHistoryQuery);
    }
}
