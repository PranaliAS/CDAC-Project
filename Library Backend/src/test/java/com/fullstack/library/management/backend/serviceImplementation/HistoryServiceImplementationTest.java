package com.fullstack.library.management.backend.serviceImplementation;

import com.fullstack.library.management.backend.SpringBootLibraryManagementApplicationTests;
import com.fullstack.library.management.backend.entity.Book;
import com.fullstack.library.management.backend.entity.History;
import com.fullstack.library.management.backend.mockEntityDtoData.mockData;
import com.fullstack.library.management.backend.repository.HistoryRepository;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,classes = SpringBootLibraryManagementApplicationTests.class)
@ActiveProfiles("test")
public class HistoryServiceImplementationTest extends mockData
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

    @Mock
    private HistoryRepository historyRepository;

    @InjectMocks
    private HistoryServiceImplementation historyServiceImplementation;

    @BeforeEach
    private void setDatabaseBeforeTransaction()
    {
        jdbcTemplate.execute(createTableHistoryQuery);
        jdbcTemplate.execute(insertDataHistoryQuery);
    }

    @Test
    @DisplayName("Get all History : Positive")
    void getAllHistoryPositiveTest()
    {
        // context
        when(historyRepository.findAll()).thenReturn(mockHistoryListPositiveData());
        // event
        List<History> savedHistory = historyServiceImplementation.getAllHistory();
        // outcome
        assertNotNull(savedHistory);
    }

    @Test
    @DisplayName("Get all History : Negative")
    void getAllHistoryNegativeTest()
    {
        // context
        when(historyRepository.findAll()).thenReturn(mockHistoryListNegativeData());
        // event
        List<History> savedHistory = historyServiceImplementation.getAllHistory();
        // outcome
        assertNotEquals(mockHistoryListNegativeData().size(),2);
    }

    @Test
    @DisplayName("Get all History for Given User Page Wise : Positive")
    void getAllHistoryForGivenUserPageWisePositiveTest()
    {
        Page<History> mockHistoryPage = mock(Page.class);
        Pageable historyPageable = PageRequest.of(0,5);
        // context
        when(historyRepository.findBooksByUserEmail(mockHistoryData().getUserEmail(),historyPageable)).thenReturn(mockHistoryPage);
        // event
        Page<History> historyPageWise = historyServiceImplementation.getBookCheckOutHistoryForUserPageWise(mockHistoryData().getUserEmail(),0,5);
        // outcome
        assertEquals(mockHistoryPage.getTotalElements(),historyPageWise.getTotalElements());
    }

    @Test
    @DisplayName("Get all History for Given User Page Wise : Negative")
    void getAllHistoryForGivenUserPageWiseNegativeTest()
    {
        Page<History> mockHistoryPage = mock(Page.class);
        Pageable historyPageable = PageRequest.of(0,5);
        // context
        when(historyRepository.findBooksByUserEmail("kirtishekhar19@gmail.com",historyPageable)).thenReturn(mockHistoryPage);
        // event
        Page<History> historyPageWise = historyServiceImplementation.getBookCheckOutHistoryForUserPageWise("kirtishekhar19@gmail.com",0,5);
        // outcome
        verify(historyRepository.findBooksByUserEmail("kirtishekhar19@gmail.com",historyPageable),times(0)).isEmpty();
    }

    @AfterEach
    private void setDatabaseAfterTransaction()
    {
        jdbcTemplate.execute(deleteHistoryQuery);
        jdbcTemplate.execute(dropHistoryQuery);
    }
}
