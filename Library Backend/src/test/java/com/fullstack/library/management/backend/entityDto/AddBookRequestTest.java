package com.fullstack.library.management.backend.entityDto;

import com.fullstack.library.management.backend.SpringBootLibraryManagementApplicationTests;
import com.fullstack.library.management.backend.dto.AddBookRequest;
import com.fullstack.library.management.backend.mockEntityDtoData.mockData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,classes = SpringBootLibraryManagementApplicationTests.class)
@ActiveProfiles("test")
public class AddBookRequestTest extends mockData
{
    @Test
    @DisplayName("Add Book Request Dto : Positive")
    void AddBookRequestDtoPositiveTest()
    {
        AddBookRequest bookEntity = mockAddBookRequestData();
        assertEquals(bookEntity.getBookTitle(),"Spring Boot Microservices");
        assertEquals(bookEntity.getBookDescription(),"Good book for springboot and microservices concepts");
        assertEquals(bookEntity.getBookAuthor(),"Prahlad");
        assertEquals(bookEntity.getBookCategory(),"Backend Developer Programming");
        assertEquals(bookEntity.getCopies(),9);
    }

    @Test
    @DisplayName("Add Book Request Dto : Negative")
    void AddBookRequestDtoNegativeTest()
    {
        AddBookRequest bookEntity = mockAddBookRequestData();
        assertNotEquals(bookEntity.getBookTitle(),"Spring Boot Microservices With H2 Database");
        assertNotEquals(bookEntity.getBookDescription(),"Good book for SpringBoot and microservices with more concepts");
        assertNotEquals(bookEntity.getBookAuthor(),"Kirti");
        assertNotEquals(bookEntity.getBookCategory(),"Backend Developer Programming With SpringBoot Microservices");
        assertNotEquals(bookEntity.getCopies(),11);
    }
}
