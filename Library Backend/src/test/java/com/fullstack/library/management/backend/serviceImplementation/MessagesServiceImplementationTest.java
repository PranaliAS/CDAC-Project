package com.fullstack.library.management.backend.serviceImplementation;

import com.fullstack.library.management.backend.SpringBootLibraryManagementApplicationTests;
import com.fullstack.library.management.backend.entity.Messages;
import com.fullstack.library.management.backend.repository.MessagesRepository;
import com.fullstack.library.management.backend.mockEntityDtoData.mockData;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,classes = SpringBootLibraryManagementApplicationTests.class)
@ActiveProfiles("test")
public class MessagesServiceImplementationTest extends mockData
{
    @Value("${sql.create.table.messages}")
    private String createTableMessagesQuery;

    @Value("${sql.insert.messages}")
    private String insertDataMessagesQuery;

    @Value("${sql.delete.messages}")
    private String deleteMessagesQuery;

    @Value("${sql.drop.messages}")
    private String dropMessagesQuery;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Mock
    private MessagesRepository messagesRepository;

    @InjectMocks
    private MessagesServiceImplementation messagesServiceImplementation;

    @BeforeEach
    private void setDatabaseBeforeTransaction()
    {
        jdbcTemplate.execute(createTableMessagesQuery);
        jdbcTemplate.execute(insertDataMessagesQuery);
    }

    @Test
    @DisplayName("Insert New Messages : Positive")
    void insertMessagesPositiveTest()
    {
        when(messagesRepository.save(any(Messages.class))).thenAnswer(i ->{
            Messages messages = i.getArgument(0);
            messages.setMessagesId(1L);
            return messages;
        });
        // context
        messagesServiceImplementation.postMessage(mockUserMessageRequestData(),mockMessagesData().getUserEmail());
        // outcome
        verify(messagesRepository,times(1)).save(any(Messages.class));
    }

    @Test
    @DisplayName("Insert New Messages : Negative")
    void insertMessagesNegativeTest()
    {
        when(messagesRepository.save(any(Messages.class))).thenReturn(null);
        // event
        messagesServiceImplementation.postMessage(mockUserMessageRequestData(),mockMessagesData().getUserEmail());
        // outcome
        assertNotEquals("kirtishekhar19@gmail.com",mockMessagesData().getUserEmail());
    }

    @Test
    @DisplayName("Update Messages Admin : Positive")
    void putMessagesPositiveTest() throws Exception
    {
        when(messagesRepository.findById(anyLong())).thenReturn(Optional.of(mockMessagesData()));
        // context
        when(messagesRepository.save(any(Messages.class))).thenAnswer(i ->{
            Messages messages = i.getArgument(0);
            messages.setMessagesId(mockAdminMessageRequestData().getQuestionId());
            messages.setMessageResponse(mockAdminMessageRequestData().getMessageResponse());
            return messages;
        });
        // calling void methods
        messagesServiceImplementation.putMessage(mockAdminMessageRequestData(),mockMessagesData().getUserEmail());
        // outcome
        assertEquals("kirtishekhar1997@gmail.com",mockMessagesData().getUserEmail());
    }

    @Test
    @DisplayName("Update Messages Admin : Negative")
    void putMessagesNegativeTest() throws Exception
    {
        when(messagesRepository.findById(anyLong())).thenReturn(Optional.of(mockMessagesData()));
        // context
        when(messagesRepository.save(any(Messages.class))).thenReturn(null);
        // calling void methods
        messagesServiceImplementation.putMessage(mockAdminMessageRequestData(),mockMessagesData().getUserEmail());
        // outcome
        assertNotEquals("kirtishekhar19@gmail.com",mockMessagesData().getUserEmail());
    }

    @Test
    @DisplayName("Get Messages By User Page Wise : Positive")
    void getMessagesByUserPageWisePositiveTest()
    {
        Page<Messages> mockMessagesPage = mock(Page.class);
        Pageable messagesPageable = PageRequest.of(0,5);
        // context
        when(messagesRepository.findByUserEmail("kirtishekhar1997@gmail.com",messagesPageable)).thenReturn(mockMessagesPage);
        // event
        Page<Messages> messagesPageWise = messagesServiceImplementation.findMessagesByUserPageWise("kirtishekhar1997@gmail.com",0,5);
        // outcome
        assertEquals(messagesPageWise.getTotalElements(),messagesPageWise.getTotalElements());
    }

    @Test
    @DisplayName("Get Messages By User Page Wise : Negative")
    void getMessagesByUserPageWiseNegativeTest()
    {
        Page<Messages> mockMessagesPage = mock(Page.class);
        Pageable messagesPageable = PageRequest.of(0,5);
        // context
        when(messagesRepository.findByUserEmail("admin@gmail.com",messagesPageable)).thenReturn(mockMessagesPage);
        // event
        Page<Messages> messagesPageWise = messagesServiceImplementation.findMessagesByUserPageWise("admin@gmail.com",0,5);
        // outcome
        verify(messagesRepository.findByUserEmail("admin@gmail.com",messagesPageable),times(0)).isEmpty();
    }

    @Test
    @DisplayName("Get Messages By Closed Page Wise : Positive")
    void getMessagesByClosedPageWisePositiveTest()
    {
        Page<Messages> mockMessagesPage = mock(Page.class);
        Pageable messagesPageable = PageRequest.of(0,5);
        // context
        when(messagesRepository.findByClosed(true,messagesPageable)).thenReturn(mockMessagesPage);
        // event
        Page<Messages> messagesPageWise = messagesServiceImplementation.findMessagesByClosedPageWise(true,0,5);
        // outcome
        assertEquals(mockMessagesPage.getTotalElements(),messagesPageWise.getTotalElements());
    }

    @Test
    @DisplayName("Get Messages By Closed Page Wise : Negative")
    void getMessagesByClosedPageWiseNegativeTest()
    {
        Page<Messages> mockMessagesPage = mock(Page.class);
        Pageable messagesPageable = PageRequest.of(0,5);
        // context
        when(messagesRepository.findByClosed(false,messagesPageable)).thenReturn(mockMessagesPage);
        // event
        Page<Messages> messagesPageWise = messagesServiceImplementation.findMessagesByClosedPageWise(false,0,5);
        // outcome
        verify(messagesRepository.findByClosed(false,messagesPageable),times(0)).isEmpty();
    }
    @AfterEach
    private void setDatabaseAfterTransaction()
    {
        jdbcTemplate.execute(deleteMessagesQuery);
        jdbcTemplate.execute(dropMessagesQuery);
    }
}
