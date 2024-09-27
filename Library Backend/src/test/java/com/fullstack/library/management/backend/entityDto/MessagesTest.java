package com.fullstack.library.management.backend.entityDto;

import com.fullstack.library.management.backend.SpringBootLibraryManagementApplicationTests;
import com.fullstack.library.management.backend.entity.Messages;
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
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,classes = SpringBootLibraryManagementApplicationTests.class)
@ActiveProfiles("test")
public class MessagesTest extends mockData
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

    @BeforeEach
    private void setDatabaseBeforeTransaction()
    {
        jdbcTemplate.execute(createTableMessagesQuery);
        jdbcTemplate.execute(insertDataMessagesQuery);
    }

    @Test
    @DisplayName("Messages Entity : Positive")
    void MessagesEntityPositiveTest()
    {
        Messages messagesEntity = mockMessagesData();
        assertEquals(messagesEntity.getMessagesId(),1L);
        assertEquals(messagesEntity.getUserEmail(),"kirtishekhar1997@gmail.com");
        assertEquals(messagesEntity.getAdminEmail(),"admin@gmail.com");
        assertTrue(messagesEntity.isClosed());
        assertEquals(messagesEntity.getTitle(),"SpringBoot Microservices Query");
        assertEquals(messagesEntity.getQuestion(),"Is This Course Valid for frontend Developer?");
        assertEquals(messagesEntity.getMessageResponse(),"No This course is valid for only backend developer!!!");
    }

    @Test
    @DisplayName("Book Entity : Negative")
    void MessagesEntityNegativeTest()
    {
        Messages messagesEntity = mockMessagesData();
        assertNotEquals(messagesEntity.getMessagesId(),9L);
        assertNotEquals(messagesEntity.getUserEmail(),"kirtishekhar19@gmail.com");
        assertNotEquals(messagesEntity.getAdminEmail(),"admins@gmail.com");
        assertFalse(!messagesEntity.isClosed());
        assertNotEquals(messagesEntity.getTitle(),"SpringBoot Microservices Gateway Query");
        assertNotEquals(messagesEntity.getQuestion(),"Is This Course Valid for frontend Developers?");
        assertNotEquals(messagesEntity.getMessageResponse(),"No This course is valid for only backend developers!!!");
    }

    @AfterEach
    private void setDatabaseAfterTransaction()
    {
        jdbcTemplate.execute(deleteMessagesQuery);
        jdbcTemplate.execute(dropMessagesQuery);
    }
}