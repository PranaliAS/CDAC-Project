package com.fullstack.library.management.backend.entityDto;

import com.fullstack.library.management.backend.SpringBootLibraryManagementApplicationTests;
import com.fullstack.library.management.backend.dto.ShelfCurrentLoansResponseDto;
import com.fullstack.library.management.backend.mockEntityDtoData.mockData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,classes = SpringBootLibraryManagementApplicationTests.class)
@ActiveProfiles("test")
public class ShelfCurrentLoansResponseTest extends mockData
{
    @Test
    @DisplayName("Shelf Current Loans Response Dto : Positive")
    void ShelfCurrentLoansResponseDtoPositiveTest()
    {
        ShelfCurrentLoansResponseDto mockShelfCurrentLoansResponse = mockShelfCurrentLoansResponseData();
        assertEquals(mockShelfCurrentLoansResponse.getBook(),mockBookData());
        assertEquals(mockShelfCurrentLoansResponse.getDaysLeft(), LocalDateTime.now().getDayOfMonth());
    }

    @Test
    @DisplayName("Shelf Current Loans Response Dto : Negative")
    void ShelfCurrentLoansResponseDtoNegativeTest()
    {
        ShelfCurrentLoansResponseDto mockShelfCurrentLoansResponse = mockShelfCurrentLoansResponseData();
        assertNotEquals(mockShelfCurrentLoansResponse.getBook(),null);
        assertNotEquals(mockShelfCurrentLoansResponse.getDaysLeft(), LocalDateTime.now().getDayOfMonth()+1);
    }
}