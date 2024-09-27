package com.fullstack.library.management.backend.entityDto;

import com.fullstack.library.management.backend.SpringBootLibraryManagementApplicationTests;
import com.fullstack.library.management.backend.entity.History;
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
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,classes = SpringBootLibraryManagementApplicationTests.class)
@ActiveProfiles("test")
public class HistoryTest extends mockData
{
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

    @BeforeEach
    private void setDatabaseBeforeTransaction()
    {
        jdbcTemplate.execute(createTableHistoryQuery);
        jdbcTemplate.execute(insertDataHistoryQuery);
    }

    @Test
    @DisplayName("History Entity : Positive")
    void HistoryEntityPositiveTest()
    {
        History historyEntity = mockHistoryData();
        assertEquals(historyEntity.getHistoryId(),1L);
        assertEquals(historyEntity.getReturnDate(),LocalDateTime.now().toString());
        assertEquals(historyEntity.getCheckoutDate(),LocalDateTime.now().plusDays(9).toString());
        assertEquals(historyEntity.getUserEmail(),"kirtishekhar1997@gmail.com");
        assertEquals(historyEntity.getBookTitle(),"Spring Boot Microservices");
        assertEquals(historyEntity.getBookAuthor(),"Prahlad");
        assertEquals(historyEntity.getBookDescription(),"Good book for springBoot and microservices concepts");
    }

    @Test
    @DisplayName("History Entity : Negative")
    void HistoryEntityNegativeTest()
    {
        History historyEntity = mockHistoryData();
        assertNotEquals(historyEntity.getHistoryId(),9L);
        assertNotEquals(historyEntity.getReturnDate(),LocalDateTime.now().plusDays(1).toString());
        assertNotEquals(historyEntity.getCheckoutDate(),LocalDateTime.now().plusDays(11).toString());
        assertNotEquals(historyEntity.getUserEmail(),"kirtishekhar19@gmail.com");
        assertNotEquals(historyEntity.getBookTitle(),"Spring Boot Microservices Course");
        assertNotEquals(historyEntity.getBookAuthor(),"Roopak");
        assertNotEquals(historyEntity.getBookDescription(),"Good book for springBoot and microservices all concepts");
    }

    @AfterEach
    private void setDatabaseAfterTransaction()
    {
        jdbcTemplate.execute(deleteHistoryQuery);
        jdbcTemplate.execute(dropHistoryQuery);
    }
}