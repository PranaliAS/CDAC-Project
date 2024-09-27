package com.fullstack.library.management.backend.entityDto;

import com.fullstack.library.management.backend.SpringBootLibraryManagementApplicationTests;
import com.fullstack.library.management.backend.dto.AdminMessageRequest;
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
public class AdminMessageRequestTest extends mockData
{
    @Test
    @DisplayName("Admin Message Request Dto : Positive")
    void AdminMessageRequestDtoPositiveTest()
    {
        AdminMessageRequest mockAdminMessageRequest = mockAdminMessageRequestData();
        assertEquals(mockAdminMessageRequest.getQuestionId(),1L);
        assertEquals(mockAdminMessageRequest.getMessageResponse(),"No This course is valid for only backend developer!!!");
    }

    @Test
    @DisplayName("Admin Message Request Dto : Negative")
    void AdminMessageRequestDtoNegativeTest()
    {
        AdminMessageRequest mockAdminMessageRequest = mockAdminMessageRequestData();
        assertNotEquals(mockAdminMessageRequest.getQuestionId(),9L);
        assertNotEquals(mockAdminMessageRequest.getMessageResponse(),"No This course is valid for only backend developers");
    }
}
