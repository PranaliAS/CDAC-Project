package com.fullstack.library.management.backend.entityDto;

import com.fullstack.library.management.backend.SpringBootLibraryManagementApplicationTests;
import com.fullstack.library.management.backend.dto.UserMessageRequest;
import com.fullstack.library.management.backend.entity.Book;
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
public class UserMessageRequestTest extends mockData
{
    @Test
    @DisplayName("User Message Request Dto : Positive")
    void UserMessageRequestDtoPositiveTest()
    {
        UserMessageRequest mockUserMessageRequest = mockUserMessageRequestData();
        assertEquals(mockUserMessageRequest.getTitle(),"SpringBoot Microservices Query");
        assertEquals(mockUserMessageRequest.getQuestion(),"Is This Course Valid for frontend Developer?");
    }

    @Test
    @DisplayName("User Message Request Dto : Negative")
    void UserMessageRequestDtoNegativeTest()
    {
        UserMessageRequest mockUserMessageRequest = mockUserMessageRequestData();

        assertNotEquals(mockUserMessageRequest.getTitle(),"SpringBoot Microservices Future Query");
        assertNotEquals(mockUserMessageRequest.getQuestion(),"Is This Course Valid for frontend Developer or not?");
    }
}
