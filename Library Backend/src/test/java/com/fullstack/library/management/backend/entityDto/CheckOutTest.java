package com.fullstack.library.management.backend.entityDto;

import com.fullstack.library.management.backend.SpringBootLibraryManagementApplicationTests;
import com.fullstack.library.management.backend.entity.CheckOut;
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
public class CheckOutTest extends mockData
{
    @Value("${sql.create.table.checkout}")
    private String createTableCheckOutQuery;

    @Value("${sql.insert.checkout}")
    private String insertDataCheckOutQuery;

    @Value("${sql.delete.checkout}")
    private String deleteCheckOutQuery;

    @Value("${sql.drop.checkout}")
    private String dropCheckOutQuery;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    private void setDatabaseBeforeTransaction()
    {
        jdbcTemplate.execute(createTableCheckOutQuery);
        jdbcTemplate.execute(insertDataCheckOutQuery);
    }

    @Test
    @DisplayName("CheckOut Entity : Positive")
    void CheckOutEntityPositiveTest()
    {
        CheckOut checkOutEntity = mockCheckOutData();
        assertEquals(checkOutEntity.getCheckoutId(),1L);
        assertEquals(checkOutEntity.getBookId(),1L);
        assertEquals(checkOutEntity.getUserEmail(),"kirtishekhar1997@gmail.com");
        assertEquals(checkOutEntity.getReturnDate(),LocalDateTime.now().toString());
    }

    @Test
    @DisplayName("CheckOut Entity : Negative")
    void CheckOutEntityNegativeTest()
    {
        CheckOut checkOutEntity = mockCheckOutData();
        assertNotEquals(checkOutEntity.getCheckoutId(),9L);
        assertNotEquals(checkOutEntity.getBookId(),9L);
        assertNotEquals(checkOutEntity.getUserEmail(),"kirtishekhar19@gmail.com");
        assertNotEquals(checkOutEntity.getReturnDate(),LocalDateTime.now().plusDays(9).toString());
        assertNotEquals(checkOutEntity.getCheckoutDate(),LocalDateTime.now().plusDays(11).toString());
    }

    @AfterEach
    private void setDatabaseAfterTransaction()
    {
        jdbcTemplate.execute(deleteCheckOutQuery);
        jdbcTemplate.execute(dropCheckOutQuery);
    }
}