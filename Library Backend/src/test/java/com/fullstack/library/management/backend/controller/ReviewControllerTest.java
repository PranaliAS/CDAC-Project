package com.fullstack.library.management.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullstack.library.management.backend.SpringBootLibraryManagementApplicationTests;
import com.fullstack.library.management.backend.entity.Book;
import com.fullstack.library.management.backend.entity.Review;
import com.fullstack.library.management.backend.jwtsecurity.JwtProvider;
import com.fullstack.library.management.backend.mockEntityDtoData.mockData;
import com.fullstack.library.management.backend.repository.BookRepository;
import com.fullstack.library.management.backend.repository.ReviewRepository;
import com.fullstack.library.management.backend.security.UserPrinciple;
import com.fullstack.library.management.backend.service.BookService;
import com.fullstack.library.management.backend.service.ReviewService;
import com.fullstack.library.management.backend.utils.ExtractJWT;
import com.fullstack.library.management.backend.utils.SecurityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = SpringBootLibraryManagementApplicationTests.class)
@ActiveProfiles("test")
public class ReviewControllerTest extends mockData
{
    @Value("${sql.create.table.review}")
    private String createTableReviewQuery;

    @Value("${sql.insert.review}")
    private String insertDataReviewQuery;

    @Value("${sql.delete.review}")
    private String deleteReviewQuery;

    @Value("${sql.drop.review}")
    private String dropReviewQuery;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger ControllerTestLogger = LoggerFactory.getLogger(ReviewControllerTest.class);

    @MockBean
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtProvider jwtProvider;

    @BeforeEach
    private void setDatabaseBeforeTransaction()
    {
        jdbcTemplate.execute(createTableReviewQuery);
        jdbcTemplate.execute(insertDataReviewQuery);
    }

    @Test
    @DisplayName("Insert New Review : Positive")
    void insertReviewPositiveTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"emailAddress\"");

        ControllerTestLogger.info("Insert New Review : Positive");

        doNothing().when(reviewService).saveBookReview(userEmail,mockReviewRequestData());

        // context
        ResultActions postReviewPositiveResult = mockMvc.perform(post("/api/reviews/service/save")
                        .header("Authorization","Bearer "+jwtAuthToken)
                        .content(objectMapper.writeValueAsString(mockReviewRequestData()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        // logger
        ControllerTestLogger.error("JSON RESPONSE {} ",postReviewPositiveResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+postReviewPositiveResult.andReturn().getResponse().getContentAsString());
        // event
        postReviewPositiveResult.andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$",is("Book Review Saved")));
    }

    @Test
    @DisplayName("Insert New Review : Negative")
    void insertReviewNegativeTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"emailAddress\"");

        ControllerTestLogger.info("Insert New Review : Negative");

        doNothing().when(reviewService).saveBookReview(userEmail,null);

        // context
        ResultActions postReviewNegativeResult = mockMvc.perform(post("/api/reviews/service/save")
                        .header("Authorization","Bearer "+jwtAuthToken)
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        // logger
        ControllerTestLogger.error("JSON RESPONSE {} ",postReviewNegativeResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+postReviewNegativeResult.andReturn().getResponse().getContentAsString());
        // event
        postReviewNegativeResult.andExpect(jsonPath("$").doesNotHaveJsonPath());
    }

    @Test
    @DisplayName("Get All Reviews : Positive")
    void getAllReviewsPositiveTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"emailAddress\"");

        ControllerTestLogger.info("Get All Reviews : Positive");

        when(reviewService.getAllReviews()).thenReturn(mockReviewListPositiveData());
        // context
        ResultActions getAllReviewsPositiveResult = mockMvc.perform(get("/api/reviews/service/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwtAuthToken))
                .andExpect(status().isOk());
        // logger
        ControllerTestLogger.error("JSON RESPONSE {} ",getAllReviewsPositiveResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+getAllReviewsPositiveResult.andReturn().getResponse().getContentAsString());
        // outcome
        getAllReviewsPositiveResult.andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$",hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    @DisplayName("Get All Reviews : Negative")
    void getAllReviewsNegativeTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"emailAddress\"");

        ControllerTestLogger.info("Get All Reviews : Negative");

        when(reviewService.getAllReviews()).thenReturn(mockReviewListNegativeData());
        // context
        ResultActions getAllReviewsNegativeResult = mockMvc.perform(get("/api/reviews/service/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwtAuthToken))
                .andExpect(status().isOk());
        // logger
        ControllerTestLogger.error("JSON RESPONSE {} ",getAllReviewsNegativeResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+getAllReviewsNegativeResult.andReturn().getResponse().getContentAsString());
        // outcome
        getAllReviewsNegativeResult.andExpect(jsonPath("$.size()",is(mockHistoryListNegativeData().size())))
                .andExpect(jsonPath("$",hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    @DisplayName("Get All Reviews Page Wise : Positive")
    void getAllReviewsPageWisePositiveTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"emailAddress\"");

        ControllerTestLogger.info("Get All Reviews Page Wise : Positive");

        Page<Review> reviewListPageWisePositive = new PageImpl<>(mockReviewListPositiveData());

        when(reviewService.getAllReviewsPageWise(0,5)).thenReturn(reviewListPageWisePositive);
        // context
        ResultActions getAllReviewsPageWisePositiveResult = mockMvc.perform(get("/api/reviews/service/all/pageWise?page=0&size=5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwtAuthToken))
                .andExpect(status().isOk());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {} ",getAllReviewsPageWisePositiveResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+getAllReviewsPageWisePositiveResult.andReturn().getResponse().getContentAsString());
        // event
        getAllReviewsPageWisePositiveResult.andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.totalElements",is((int) reviewListPageWisePositive.getTotalElements())));
    }

    @Test
    @DisplayName("Get All Reviews Page Wise : Negative")
    void getAllReviewsPageWiseNegativeTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"emailAddress\"");

        ControllerTestLogger.info("Get All Reviews Page Wise : Negative");

        Page<Review> reviewListPageWiseNegative = new PageImpl<>(mockReviewListNegativeData());

        when(reviewService.getAllReviewsPageWise(0,5)).thenReturn(reviewListPageWiseNegative);
        // context
        ResultActions getAllReviewsPageWiseNegativeResult = mockMvc.perform(get("/api/reviews/service/all/pageWise?page=0&size=5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwtAuthToken))
                .andExpect(status().isOk());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {} ",getAllReviewsPageWiseNegativeResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+getAllReviewsPageWiseNegativeResult.andReturn().getResponse().getContentAsString());
        // event
        getAllReviewsPageWiseNegativeResult.andExpect(jsonPath("$.size",is(reviewListPageWiseNegative.getContent().size())))
                .andExpect(jsonPath("$.totalElements",is((int) reviewListPageWiseNegative.getTotalElements())));
    }

    @Test
    @DisplayName("Get Review By Review Id : Positive")
    void getReviewByReviewIdPositiveTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"emailAddress\"");
        Long reviewId = mockReviewData().getReviewId();

        ControllerTestLogger.info("Get Review By Review Id : Positive");

        when(reviewService.getReviewByReviewId(reviewId)).thenReturn(mockReviewData());
        // context
        ResultActions getReviewByReviewIdPositiveResult = mockMvc.perform(get("/api/reviews/service/getByReviewId?reviewId="+reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwtAuthToken))
                .andExpect(status().isOk());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {} ",getReviewByReviewIdPositiveResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+getReviewByReviewIdPositiveResult.andReturn().getResponse().getContentAsString());
        // event
        getReviewByReviewIdPositiveResult.andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").hasJsonPath());
    }

    @Test
    @DisplayName("Get Review By Review Id : Negative")
    void getReviewByReviewIdNegativeTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"emailAddress\"");
        Long reviewId = mockReviewData().getReviewId();

        ControllerTestLogger.info("Get Review By Review Id : Negative");

        when(reviewService.getReviewByReviewId(99L)).thenReturn(null);
        // context
        ResultActions getReviewByReviewIdNegativeResult = mockMvc.perform(get("/api/reviews/service/getByReviewId?reviewId=99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwtAuthToken))
                .andExpect(status().isOk());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {} ",getReviewByReviewIdNegativeResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+getReviewByReviewIdNegativeResult.andReturn().getResponse().getContentAsString());
        // event
        getReviewByReviewIdNegativeResult.andExpect(jsonPath("$").doesNotHaveJsonPath());
    }

    @Test
    @DisplayName("Get Review By Book Id : Positive")
    void getReviewByBookIdPositiveTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"emailAddress\"");
        Long bookId = mockReviewData().getBookId();

        ControllerTestLogger.info("Get Review By Book Id : Positive");

        Page<Review> reviewListPageWisePositive = new PageImpl<>(mockReviewListPositiveData());

        when(reviewService.findReviewsByBookId(bookId,0,5)).thenReturn(reviewListPageWisePositive);
        // context
        ResultActions getReviewByBookIdPositiveResult = mockMvc.perform(get("/api/reviews/service/getByBookId/pageWise?bookId="+bookId+"&page=0&size=5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwtAuthToken))
                .andExpect(status().isOk());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {} ",getReviewByBookIdPositiveResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+getReviewByBookIdPositiveResult.andReturn().getResponse().getContentAsString());
        // event
        getReviewByBookIdPositiveResult.andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.totalElements",is((int) reviewListPageWisePositive.getTotalElements())));
    }

    @Test
    @DisplayName("Get Review By Book Id : Negative")
    void getReviewByBookIdNegativeTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"emailAddress\"");
        Long reviewId = mockReviewData().getReviewId();

        ControllerTestLogger.info("Get Review By Book Id : Negative");

        when(reviewService.findReviewsByBookId(99L,0,5)).thenReturn(null);
        // context
        ResultActions getReviewByBookIdNegativeResult = mockMvc.perform(get("/api/reviews/service/getByBookId/pageWise?bookId=99&page=0&size=5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwtAuthToken))
                .andExpect(status().isOk());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {} ",getReviewByBookIdNegativeResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+getReviewByBookIdNegativeResult.andReturn().getResponse().getContentAsString());
        // event
        getReviewByBookIdNegativeResult.andExpect(jsonPath("$").doesNotHaveJsonPath());
    }

    @Test
    @DisplayName("Is User Review Listed : Positive")
    void checkIsUserReviewListedPositiveTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"emailAddress\"");
        Long bookId = mockReviewData().getBookId();

        ControllerTestLogger.info("Is User Review Listed : Positive");

        when(reviewService.isUserReviewListed(userEmail,bookId)).thenReturn(true);
        // context
        ResultActions checkIsUserReviewListedPositiveResult = mockMvc.perform(get("/api/reviews/service/isUserReviewListed?bookId="+bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwtAuthToken))
                .andExpect(status().isOk());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {}",checkIsUserReviewListedPositiveResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {}"+checkIsUserReviewListedPositiveResult.andReturn().getResponse().getContentAsString());
        // event
        checkIsUserReviewListedPositiveResult.andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$",is(true)));
    }

    @Test
    @DisplayName("Is User Review Listed : Negative")
    void checkIsUserReviewListedNegativeTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"emailAddress\"");
        Long bookId = 99L;

        ControllerTestLogger.info("Is User Review Listed : Negative");

        when(reviewService.isUserReviewListed(userEmail,bookId)).thenReturn(false);
        // context
        ResultActions checkIsUserReviewListedNegativeResult = mockMvc.perform(get("/api/reviews/service/isUserReviewListed?bookId=99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwtAuthToken))
                .andExpect(status().isOk());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {}",checkIsUserReviewListedNegativeResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {}"+checkIsUserReviewListedNegativeResult.andReturn().getResponse().getContentAsString());
        // event
        checkIsUserReviewListedNegativeResult.andExpect(jsonPath("$").hasJsonPath())
                .andExpect(jsonPath("$",not(true)));
    }

    @AfterEach
    private void setDatabaseAfterTransaction()
    {
        jdbcTemplate.execute(deleteReviewQuery);
        jdbcTemplate.execute(dropReviewQuery);
    }
}
