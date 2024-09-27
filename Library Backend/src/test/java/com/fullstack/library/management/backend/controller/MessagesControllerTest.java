package com.fullstack.library.management.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullstack.library.management.backend.SpringBootLibraryManagementApplicationTests;
import com.fullstack.library.management.backend.entity.Messages;
import com.fullstack.library.management.backend.jwtsecurity.JwtProvider;
import com.fullstack.library.management.backend.mockEntityDtoData.mockData;
import com.fullstack.library.management.backend.security.UserPrinciple;
import com.fullstack.library.management.backend.service.MessagesService;
import com.fullstack.library.management.backend.utils.ExtractJWT;
import com.fullstack.library.management.backend.utils.SecurityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = SpringBootLibraryManagementApplicationTests.class)
@ActiveProfiles("test")
public class MessagesControllerTest extends mockData
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

    private static final Logger ControllerTestLogger = LoggerFactory.getLogger(MessagesControllerTest.class);

    @MockBean
    private MessagesService messagesService;

    @InjectMocks
    private MessagesController messagesController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtProvider jwtProvider;

    @BeforeEach
    private void setDatabaseBeforeTransaction()
    {
        jdbcTemplate.execute(createTableMessagesQuery);
        jdbcTemplate.execute(insertDataMessagesQuery);
    }

    @Test
    @DisplayName("Insert New Messages : Positive")
    void insertMessagesPositiveTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"emailAddress\"");

        ControllerTestLogger.info("Insert New Messages : Positive");

        doNothing().when(messagesService).postMessage(mockUserMessageRequestData(),userEmail);

        // context
        ResultActions postUserMessagePositiveResult = mockMvc.perform(post("/api/messages/service/postMessage")
                        .content(objectMapper.writeValueAsString(mockUserMessageRequestData()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwtAuthToken))
                .andExpect(status().isCreated());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {}",postUserMessagePositiveResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {}"+postUserMessagePositiveResult.andReturn().getResponse().getContentAsString());
        // event
        postUserMessagePositiveResult.andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$",is("Message Posted Successfully")));
    }

    @Test
    @DisplayName("Insert New Messages : Negative")
    void insertMessagesNegativeTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"emailAddress\"");

        ControllerTestLogger.info("Insert New Messages : Negative");

        doNothing().when(messagesService).postMessage(null,userEmail);

        // context
        ResultActions postUserMessagePositiveResult = mockMvc.perform(post("/api/messages/service/postMessage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwtAuthToken))
                .andExpect(status().is4xxClientError());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {}",postUserMessagePositiveResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {}"+postUserMessagePositiveResult.andReturn().getResponse().getContentAsString());
        // event
        postUserMessagePositiveResult.andExpect(jsonPath("$").doesNotHaveJsonPath());
    }

    @Test
    @DisplayName("Update Messages Admin : Positive")
    void putMessagesPositiveTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"emailAddress\"");

        ControllerTestLogger.info("Update Messages Admin : Positive");

        doNothing().when(messagesService).putMessage(mockAdminMessageRequestData(),userEmail);

        // context
        ResultActions putAdminMessageResponsePositiveResult = mockMvc.perform(put("/api/messages/service/updateMessage/response")
                        .content(objectMapper.writeValueAsString(mockAdminMessageRequestData()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwtAuthToken))
                .andExpect(status().isCreated());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {}",putAdminMessageResponsePositiveResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {}"+putAdminMessageResponsePositiveResult.andReturn().getResponse().getContentAsString());
        // event
        putAdminMessageResponsePositiveResult.andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$",is("Message Responded  and Closed Successfully")));
    }

    @Test
    @DisplayName("Update Messages Admin : Negative")
    void putMessagesNegativeTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"emailAddress\"");

        ControllerTestLogger.info("Update Messages Admin : Negative");

        doNothing().when(messagesService).putMessage(null,userEmail);

        // context
        ResultActions putAdminMessageResponseNegativeResult = mockMvc.perform(put("/api/messages/service/updateMessage/response")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwtAuthToken))
                .andExpect(status().is4xxClientError());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {}",putAdminMessageResponseNegativeResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {}"+putAdminMessageResponseNegativeResult.andReturn().getResponse().getContentAsString());
        // event
        putAdminMessageResponseNegativeResult.andExpect(jsonPath("$").doesNotHaveJsonPath());
    }

    @Test
    @DisplayName("Get Messages By User Page Wise : Positive")
    void getMessagesByUserPageWisePositiveTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"emailAddress\"");

        Page<Messages> messagesListPageWisePositive = new PageImpl<>(mockMessagesDataListPositiveData());

        ControllerTestLogger.info("Get Messages By User Page Wise : Positive");

        when(messagesService.findMessagesByUserPageWise(userEmail,0,5)).thenReturn(messagesListPageWisePositive);

        // context
        ResultActions getAllMessagesByUserPageWisePositiveResult = mockMvc.perform(get("/api/messages/service/MessagesByUser/pageWise?page=0&size=5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwtAuthToken))
                .andExpect(status().isOk());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {} ",getAllMessagesByUserPageWisePositiveResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+getAllMessagesByUserPageWisePositiveResult.andReturn().getResponse().getContentAsString());
        // event
        getAllMessagesByUserPageWisePositiveResult
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.totalElements",is((int) messagesListPageWisePositive.getTotalElements())));
    }

    @Test
    @DisplayName("Get Messages By User Page Wise : Negative")
    void getMessagesByUserPageWiseNegativeTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"emailAddress\"");

        Page<Messages> messagesListPageWiseNegative = new PageImpl<>(mockMessagesDataListNegativeData());

        ControllerTestLogger.info("Get Messages By User Page Wise : Negative");

        when(messagesService.findMessagesByUserPageWise(userEmail,0,5)).thenReturn(messagesListPageWiseNegative);

        // context
        ResultActions getAllMessagesByUserPageWiseNegativeResult = mockMvc.perform(get("/api/messages/service/MessagesByUser/pageWise?page=0&size=5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwtAuthToken))
                .andExpect(status().isOk());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {} ",getAllMessagesByUserPageWiseNegativeResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+getAllMessagesByUserPageWiseNegativeResult.andReturn().getResponse().getContentAsString());
        // event
        getAllMessagesByUserPageWiseNegativeResult
                .andExpect(jsonPath("$.size()").isNotEmpty())
                .andExpect(jsonPath("$.totalElements",is((int) messagesListPageWiseNegative.getTotalElements())));
    }

    @Test
    @DisplayName("Get Messages By Closed Page Wise : Positive")
    void getMessagesByClosedPageWisePositiveTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"emailAddress\"");

        Page<Messages> messagesListPageWisePositive = new PageImpl<>(mockMessagesDataListPositiveData());

        ControllerTestLogger.info("Get Messages By Closed Page Wise : Positive");

        when(messagesService.findMessagesByClosedPageWise(true,0,5)).thenReturn(messagesListPageWisePositive);

        // context
        ResultActions getAllMessagesByClosedPageWisePositiveResult = mockMvc.perform(get("/api/messages/service/MessagesByClosed/pageWise?closed=true&page=0&size=5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwtAuthToken))
                .andExpect(status().isOk());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {} ",getAllMessagesByClosedPageWisePositiveResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+getAllMessagesByClosedPageWisePositiveResult.andReturn().getResponse().getContentAsString());
        // event
        getAllMessagesByClosedPageWisePositiveResult
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.totalElements",is((int) messagesListPageWisePositive.getTotalElements())));
    }

    @Test
    @DisplayName("Get Messages By Closed Page Wise : Negative")
    void getMessagesByClosedPageWiseNegativeTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"emailAddress\"");

        Page<Messages> messagesListPageWiseNegative = new PageImpl<>(mockMessagesDataListNegativeData());

        ControllerTestLogger.info("Get Messages By Closed Page Wise : Negative");

        when(messagesService.findMessagesByClosedPageWise(false,0,5)).thenReturn(messagesListPageWiseNegative);

        // context
        ResultActions getAllMessagesByClosedPageWiseNegativeResult = mockMvc.perform(get("/api/messages/service/MessagesByUser/pageWise?page=0&size=5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwtAuthToken))
                .andExpect(status().isOk());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {} ",getAllMessagesByClosedPageWiseNegativeResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+getAllMessagesByClosedPageWiseNegativeResult.andReturn().getResponse().getContentAsString());
        // event
        getAllMessagesByClosedPageWiseNegativeResult
                .andExpect(jsonPath("$.size()").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.totalElements").doesNotHaveJsonPath());
    }

    @AfterEach
    private void setDatabaseAfterTransaction()
    {
        jdbcTemplate.execute(deleteMessagesQuery);
        jdbcTemplate.execute(dropMessagesQuery);
    }
}
