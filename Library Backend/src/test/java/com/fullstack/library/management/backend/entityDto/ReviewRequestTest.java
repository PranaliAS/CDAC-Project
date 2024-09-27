package com.fullstack.library.management.backend.entityDto;

import com.fullstack.library.management.backend.SpringBootLibraryManagementApplicationTests;
import com.fullstack.library.management.backend.dto.ReviewRequestDto;
import com.fullstack.library.management.backend.mockEntityDtoData.mockData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,classes = SpringBootLibraryManagementApplicationTests.class)
@ActiveProfiles("test")
public class ReviewRequestTest extends mockData
{
    @Test
    @DisplayName("Review Request Dto : Positive")
    void ReviewRequestDtoPositiveTest()
    {
        ReviewRequestDto mockReviewRequestDto = mockReviewRequestData();
        assertEquals(mockReviewRequestDto.getRating(),4.5);
        assertEquals(mockReviewRequestDto.getReviewDescription(), Optional.of("Good and Nice backend developer concept book"));
        assertEquals(mockReviewRequestDto.getBookId(),1L);
    }

    @Test
    @DisplayName("Review Request Dto : Negative")
    void ReviewRequestDtoNegativeTest()
    {
        ReviewRequestDto mockReviewRequestDto = mockReviewRequestData();
        assertNotEquals(mockReviewRequestDto.getRating(),5.5);
        assertNotEquals(mockReviewRequestDto.getReviewDescription(),Optional.of("Bad backend developer concept book"));
        assertNotEquals(mockReviewRequestDto.getBookId(),5L);
    }
}