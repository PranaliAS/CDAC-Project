package com.fullstack.library.management.backend.serviceImplementation;

import com.fullstack.library.management.backend.SpringBootLibraryManagementApplicationTests;
import com.fullstack.library.management.backend.entity.Book;
import com.fullstack.library.management.backend.entity.Review;
import com.fullstack.library.management.backend.mockEntityDtoData.mockData;
import com.fullstack.library.management.backend.repository.BookRepository;
import com.fullstack.library.management.backend.repository.ReviewRepository;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,classes = SpringBootLibraryManagementApplicationTests.class)
@ActiveProfiles("test")
public class ReviewServiceImplementationTest extends mockData
{
    @Value("${sql.create.table.review}")
    private String createTableReviewQuery;

    @Value("${sql.insert.review}")
    private String insertDataReviewQuery;

    @Value("${sql.delete.review}")
    private String deleteReviewQuery;

    @Value("${sql.drop.review}")
    private String dropReviewQuery;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewServiceImplementation reviewServiceImplementation;

    @BeforeEach
    private void setDatabaseBeforeTransaction()
    {
        jdbcTemplate.execute(createTableReviewQuery);
        jdbcTemplate.execute(insertDataReviewQuery);
    }

    @Test
    @DisplayName("Insert New Review : Positive")
    void insertReviewPositiveTest() throws Exception
    {
        when(reviewRepository.save(any(Review.class))).thenAnswer(i ->{
            Review review = i.getArgument(0);
            review.setReviewId(2L);
            return review;
        });
        // context
        reviewServiceImplementation.saveBookReview("monucool@gmail.com",mockReviewRequestData());
        // outcome
        verify(reviewRepository,times(1)).save(any(Review.class));
    }

    @Test
    @DisplayName("Insert New Review : Negative")
    void insertReviewNegativeTest() throws Exception
    {
        when(reviewRepository.findByUserEmailAndBookId(mockReviewData().getUserEmail(),mockReviewRequestData().getBookId())).thenReturn(null);
        // event
        when(reviewRepository.save(any(Review.class))).thenReturn(null);
        reviewServiceImplementation.saveBookReview(mockReviewData().getUserEmail(),mockReviewRequestData());
        // outcome
        assertNotEquals("kirtishekhar19@gmail.com",mockReviewData().getUserEmail());
    }

    @Test
    @DisplayName("Get All Reviews : Positive")
    void getAllReviewsPositiveTest()
    {
        // context
        when(reviewRepository.findAll()).thenReturn(mockReviewListPositiveData());
        // event
        List<Review> savedReviewsList = reviewServiceImplementation.getAllReviews();
        // outcome
        assertNotNull(savedReviewsList);
    }

    @Test
    @DisplayName("Get All Reviews : Negative")
    void getAllReviewsNegativeTest()
    {
        // context
        when(reviewRepository.findAll()).thenReturn(mockReviewListNegativeData());
        // event
        List<Review> savedReviewsList = reviewServiceImplementation.getAllReviews();
        // outcome
        assertNotEquals(savedReviewsList.size(),2);
    }

    @Test
    @DisplayName("Get All Reviews Page Wise : Positive")
    void getAllReviewsPageWisePositiveTest()
    {
        Page<Review> mockReviewPage = mock(Page.class);
        Pageable reviewPageable = PageRequest.of(0,5);
        // context
        when(reviewRepository.findAll(reviewPageable)).thenReturn(mockReviewPage);
        // event
        Page<Review> reviewsPageWise = reviewServiceImplementation.getAllReviewsPageWise(0,5);
        // outcome
        assertEquals(mockReviewPage.getTotalElements(),reviewsPageWise.getTotalElements());
    }

    @Test
    @DisplayName("Get All Reviews Page Wise : Negative")
    void getAllReviewsPageWiseNegativeTest()
    {
        Page<Review> mockReviewPage = mock(Page.class);
        Pageable reviewPageable = PageRequest.of(0,5);
        // context
        when(reviewRepository.findAll(reviewPageable)).thenReturn(mockReviewPage);
        // event
        Page<Review> reviewsPageWise = reviewServiceImplementation.getAllReviewsPageWise(0,5);
        // outcome
        verify(reviewRepository.findAll(reviewPageable),times(0)).isEmpty();
    }

    @Test
    @DisplayName("Get Review By Review Id : Positive")
    void getReviewByReviewIdPositiveTest()
    {
        // context
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(mockReviewData()));
        // event
        Review review = reviewServiceImplementation.getReviewByReviewId(anyLong());
        // outcome
        assertNotNull(review);
    }

    @Test
    @DisplayName("Get Review By Review Id : Negative")
    void getReviewByReviewIdNegativeTest()
    {
        // context
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(mockReviewData()));
        // event
        Review review = reviewServiceImplementation.getReviewByReviewId(anyLong());
        // outcome
        assertNotSame(mockReviewData(),review);
    }

    @Test
    @DisplayName("Get Review By Book Id : Positive")
    void getReviewByBookIdPositiveTest()
    {
        Page<Review> mockReviewPage = mock(Page.class);
        Pageable reviewPageable = PageRequest.of(0,5);
        // context
        when(reviewRepository.findByBookId(1L,reviewPageable)).thenReturn(mockReviewPage);
        // event
        Page<Review> reviewPage = reviewServiceImplementation.findReviewsByBookId(1L,0,5);
        // outcome
        assertEquals(mockReviewPage.getTotalElements(),reviewPage.getTotalElements());
    }

    @Test
    @DisplayName("Get Review By Book Id : Negative")
    void getReviewByBookIdNegativeTest()
    {
        Page<Review> mockReviewPage = mock(Page.class);
        Pageable reviewPageable = PageRequest.of(0,5);
        // context
        when(reviewRepository.findByBookId(39L,reviewPageable)).thenReturn(mockReviewPage);
        // event
        Page<Review> reviewPage = reviewServiceImplementation.findReviewsByBookId(39L,0,5);
        // outcome
        verify(reviewRepository.findByBookId(39L,reviewPageable),times(0)).isEmpty();
    }

    @Test
    @DisplayName("Is User Review Listed : Positive")
    void checkIsUserReviewListedPositiveTest()
    {
        when(reviewRepository.findByUserEmailAndBookId(mockReviewData().getUserEmail(),mockReviewRequestData().getBookId())).thenReturn(mockReviewData());
        assertTrue(reviewServiceImplementation.isUserReviewListed(mockReviewData().getUserEmail(),mockBookData().getBookId()));
    }

    @Test
    @DisplayName("Is User Review Listed : Negative")
    void checkIsUserReviewListedNegativeTest()
    {
        when(reviewRepository.findByUserEmailAndBookId("admin@gmail.com",mockReviewRequestData().getBookId())).thenReturn(null);
        assertFalse(reviewServiceImplementation.isUserReviewListed("admin@gmail.com",mockReviewRequestData().getBookId()));
    }

    @AfterEach
    private void setDatabaseAfterTransaction()
    {
        jdbcTemplate.execute(deleteReviewQuery);
        jdbcTemplate.execute(dropReviewQuery);
    }
}
