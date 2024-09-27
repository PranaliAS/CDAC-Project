package com.fullstack.library.management.backend.entityDto;

import com.fullstack.library.management.backend.SpringBootLibraryManagementApplicationTests;
import com.fullstack.library.management.backend.entity.Review;
import com.fullstack.library.management.backend.mockEntityDtoData.mockData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.Date;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,classes = SpringBootLibraryManagementApplicationTests.class)
@ActiveProfiles("test")
public class ReviewTest extends mockData
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

    @BeforeEach
    private void setDatabaseBeforeTransaction()
    {
        jdbcTemplate.execute(createTableReviewQuery);
        jdbcTemplate.execute(insertDataReviewQuery);
    }

    @Test
    @DisplayName("Book Entity : Positive")
    void ReviewEntityPositiveTest()
    {
        Review reviewEntity = mockReviewData();
        assertEquals(reviewEntity.getReviewId(),1L);
        assertEquals(reviewEntity.getUserEmail(),"kirtishekhar1997@gmail.com");
        assertEquals(reviewEntity.getRating(),4.5);
        assertEquals(reviewEntity.getReviewDescription(),"Good and Nice backend developer concept book");
        assertEquals(reviewEntity.getDate(),Date.valueOf(LocalDate.now()));
        assertEquals(reviewEntity.getBookId(),1L);
    }

    @Test
    @DisplayName("Review Entity : Negative")
    void ReviewEntityNegativeTest()
    {
        Review reviewEntity = mockReviewData();
        assertNotEquals(reviewEntity.getReviewId(),9L);
        assertNotEquals(reviewEntity.getUserEmail(),"kirtishekhar19@gmail.com");
        assertNotEquals(reviewEntity.getRating(),4.8);
        assertNotEquals(reviewEntity.getReviewDescription(),"Good and Nice backend developer concept books");
        assertNotEquals(reviewEntity.getDate(),Date.valueOf(LocalDate.now().plusDays(11)));
        assertNotEquals(reviewEntity.getBookId(),4L);
    }

    @AfterEach
    private void setDatabaseAfterTransaction()
    {
        jdbcTemplate.execute(deleteReviewQuery);
        jdbcTemplate.execute(dropReviewQuery);
    }
}