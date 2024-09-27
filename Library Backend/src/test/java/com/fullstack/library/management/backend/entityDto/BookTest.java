package com.fullstack.library.management.backend.entityDto;

import com.fullstack.library.management.backend.SpringBootLibraryManagementApplicationTests;
import com.fullstack.library.management.backend.entity.Book;
import com.fullstack.library.management.backend.mockEntityDtoData.mockData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,classes = SpringBootLibraryManagementApplicationTests.class)
@ActiveProfiles("test")
public class BookTest extends mockData
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

    @BeforeEach
    private void setDatabaseBeforeTransaction()
    {
        jdbcTemplate.execute(createTableBookQuery);
        jdbcTemplate.execute(insertDataBookQuery);
    }
    
    @Test
    @DisplayName("Book Entity : Positive")
    void BookEntityPositiveTest()
    {
        Book bookEntity = mockBookData();
        assertEquals(bookEntity.getBookId(),1L);
        assertEquals(bookEntity.getBookTitle(),"Spring Boot Microservices");
        assertEquals(bookEntity.getBookDescription(),"Good book for springboot and microservices concepts");
        assertEquals(bookEntity.getBookAuthor(),"Prahlad");
        assertEquals(bookEntity.getBookCategory(),"Backend Developer Programming");
        assertEquals(bookEntity.getCopies(),9);
        assertEquals(bookEntity.getCopiesAvailable(),6);
    }

    @Test
    @DisplayName("Book Entity : Negative")
    void BookEntityNegativeTest()
    {
        Book bookEntity = mockBookData();
        assertNotEquals(bookEntity.getBookId(),9L);
        assertNotEquals(bookEntity.getBookTitle(),"Spring Boot Microservices With H2 Database");
        assertNotEquals(bookEntity.getBookDescription(),"Good book for SpringBoot and microservices with more concepts");
        assertNotEquals(bookEntity.getBookAuthor(),"Kirti");
        assertNotEquals(bookEntity.getBookCategory(),"Backend Developer Programming With SpringBoot Microservices");
        assertNotEquals(bookEntity.getCopies(),11);
        assertNotEquals(bookEntity.getCopiesAvailable(),9);
    }

    @AfterEach
    private void setDatabaseAfterTransaction()
    {
        jdbcTemplate.execute(deleteBookQuery);
        jdbcTemplate.execute(dropBookQuery);
    }
}