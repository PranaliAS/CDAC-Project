package com.fullstack.library.management.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullstack.library.management.backend.SpringBootLibraryManagementApplicationTests;
import com.fullstack.library.management.backend.dto.ShelfCurrentLoansResponseDto;
import com.fullstack.library.management.backend.jwtsecurity.JwtProvider;
import com.fullstack.library.management.backend.mockEntityDtoData.mockData;
import com.fullstack.library.management.backend.repository.BookRepository;
import com.fullstack.library.management.backend.repository.CheckOutRepository;
import com.fullstack.library.management.backend.repository.HistoryRepository;
import com.fullstack.library.management.backend.entity.CheckOut;
import com.fullstack.library.management.backend.entity.History;
import com.fullstack.library.management.backend.entity.Book;
import com.fullstack.library.management.backend.security.UserPrinciple;
import com.fullstack.library.management.backend.service.BookService;
import com.fullstack.library.management.backend.service.CheckOutService;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = SpringBootLibraryManagementApplicationTests.class)
@ActiveProfiles("test")
public class CheckOutControllerTest extends mockData
{
    @Value("${sql.create.table.checkout}")
    private String createTableCheckOutQuery;

    @Value("${sql.insert.checkout}")
    private String insertDataCheckOutQuery;

    @Value("${sql.delete.checkout}")
    private String deleteCheckOutQuery;

    @Value("${sql.drop.checkout}")
    private String dropCheckOutQuery;

    @Value("${sql.create.table.book}")
    private String createTableBookQuery;

    @Value("${sql.insert.book}")
    private String insertDataBookQuery;

    @Value("${sql.delete.book}")
    private String deleteBookQuery;

    @Value("${sql.drop.book}")
    private String dropBookQuery;

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

    private static final Logger ControllerTestLogger = LoggerFactory.getLogger(CheckOutControllerTest.class);

    @MockBean
    private CheckOutService checkOutService;

    @InjectMocks
    private CheckOutController checkOutController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtProvider jwtProvider;

    @BeforeEach
    private void setDatabaseBeforeTransaction()
    {
        jdbcTemplate.execute(createTableCheckOutQuery);
        jdbcTemplate.execute(createTableBookQuery);
        jdbcTemplate.execute(createTableHistoryQuery);
        jdbcTemplate.execute(insertDataCheckOutQuery);
        jdbcTemplate.execute(insertDataBookQuery);
        jdbcTemplate.execute(insertDataHistoryQuery);
    }

    @Test
    @DisplayName("Return the checkout Book : Positive")
    void returnCheckOutBookPositiveTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"emailAddress\"");
        Long bookId = mockCheckOutData().getBookId();

        ControllerTestLogger.info("Return the checkout Book : Positive");
        doNothing().when(checkOutService).returnCheckedOutBook(userEmail,bookId);

        // context
        ResultActions returnCheckOutBookPositiveResult = mockMvc.perform(
                        put("/api/checkout/service/returnCheckOutBook?bookId="+bookId)
                                .header("Authorization","Bearer "+jwtAuthToken)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {} ",returnCheckOutBookPositiveResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+returnCheckOutBookPositiveResult.andReturn().getResponse().getContentAsString());
        // event
        returnCheckOutBookPositiveResult.andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$",is("CheckOut Book Returned")));
    }

    @Test
    @DisplayName("Return the checkout Book : Negative")
    void returnCheckOutBookNegativeTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"emailAddress\"");
        Long bookId = mockCheckOutData().getBookId();

        ControllerTestLogger.info("Return the checkout Book : Negative");
        doNothing().when(checkOutService).returnCheckedOutBook(userEmail,bookId);

        // context
        ResultActions returnCheckOutBookNegativeResult = mockMvc.perform(
                        put("/api/checkout/service/returnCheckOutBook?bookId="+bookId)
                                .header("Authorization","Bearer "+jwtAuthToken)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {} ",returnCheckOutBookNegativeResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+returnCheckOutBookNegativeResult.andReturn().getResponse().getContentAsString());
        // event
        returnCheckOutBookNegativeResult.andExpect(jsonPath("$").hasJsonPath());
    }

    @Test
    @DisplayName("Checkout the given book for given user : Positive")
    void checkOutGivenBookForGivenUserPositiveTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"emailAddress\"");
        Long bookId = mockCheckOutData().getBookId();

        ControllerTestLogger.info("Checkout the given book for given user : Positive");
        when(checkOutService.checkOutBook(userEmail,bookId)).thenReturn(mockBookData());

        // context
        ResultActions checkOutBookPositiveResult = mockMvc.perform(
                        put("/api/checkout/service/bookCheckout?bookId="+bookId)
                                .header("Authorization","Bearer "+jwtAuthToken)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {} ",checkOutBookPositiveResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+checkOutBookPositiveResult.andReturn().getResponse().getContentAsString());
        // event
        checkOutBookPositiveResult.andExpect(jsonPath("$").hasJsonPath());
    }

    @Test
    @DisplayName("Checkout the given book for given user : Negative")
    void checkOutGivenBookForGivenUserNegativeTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"emailAddress\"");
        Long bookId = mockCheckOutData().getBookId();

        ControllerTestLogger.info("Checkout the given book for given user : Positive");
        when(checkOutService.checkOutBook(userEmail,bookId)).thenReturn(null);

        // context
        ResultActions checkOutBookPositiveResult = mockMvc.perform(
                        put("/api/checkout/service/bookCheckout?bookId="+bookId)
                                .header("Authorization","Bearer "+jwtAuthToken)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {} ",checkOutBookPositiveResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+checkOutBookPositiveResult.andReturn().getResponse().getContentAsString());
        // event
        checkOutBookPositiveResult.andExpect(jsonPath("$").doesNotHaveJsonPath());
    }

    @Test
    @DisplayName("Renew Loan For Book : Positive")
    void renewLoanForBookPositiveTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"emailAddress\"");
        Long bookId = mockCheckOutData().getBookId();

        ControllerTestLogger.info("Renew Loan For Book : Positive");
        doNothing().when(checkOutService).renewLoan(userEmail,bookId);

        // context
        ResultActions renewBookLoanPositiveResult = mockMvc.perform(
                        put("/api/checkout/service/renewLoan?bookId="+bookId)
                                .header("Authorization","Bearer "+jwtAuthToken)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {} ",renewBookLoanPositiveResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+renewBookLoanPositiveResult.andReturn().getResponse().getContentAsString());
        // event
        renewBookLoanPositiveResult.andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$",is("CheckOut Book Loan Renewed")));
    }

    @Test
    @DisplayName("Renew Loan For Book : Negative")
    void renewLoanForBookNegativeTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"emailAddress\"");
        Long bookId = mockCheckOutData().getBookId();

        ControllerTestLogger.info("Renew Loan For Book : Negative");
        doNothing().when(checkOutService).renewLoan(userEmail,bookId);

        // context
        ResultActions renewBookLoanNegativeResult = mockMvc.perform(
                        put("/api/checkout/service/renewLoan?bookId="+bookId)
                                .header("Authorization","Bearer "+jwtAuthToken)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {} ",renewBookLoanNegativeResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+renewBookLoanNegativeResult.andReturn().getResponse().getContentAsString());
        // event
        renewBookLoanNegativeResult.andExpect(jsonPath("$").hasJsonPath());
    }


    @Test
    @DisplayName("Get All CheckOut Details : Positive")
    void getAllCheckOutDetailsPositiveTest() throws Exception
    {
        Set<GrantedAuthority> userAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAuthorities));

        when(checkOutService.getAllCheckOutDetails()).thenReturn(mockCheckOutDataListPositiveData());

        // context
        ResultActions getAllCheckOutPositiveResult = mockMvc.perform(get("/api/checkout/service/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwtAuthToken))
                .andExpect(status().isOk());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {}",getAllCheckOutPositiveResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {}"+getAllCheckOutPositiveResult.andReturn().getResponse().getContentAsString());
        // event
        getAllCheckOutPositiveResult.andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$",hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    @DisplayName("Get All CheckOut Details : Negative")
    void getAllCheckOutDetailsNegativeTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        when(checkOutService.getAllCheckOutDetails()).thenReturn(mockCheckOutDataListNegativeData());

        // context
        ResultActions getAllCheckOutNegativeResult = mockMvc.perform(get("/api/checkout/service/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwtAuthToken))
                .andExpect(status().isOk());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {}",getAllCheckOutNegativeResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {}"+getAllCheckOutNegativeResult.andReturn().getResponse().getContentAsString());
        // event
        getAllCheckOutNegativeResult.andExpect(jsonPath("$.size()",is(mockCheckOutDataListNegativeData().size())))
                .andExpect(jsonPath("$",hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    @DisplayName("Get All CheckOut Details Page Wise : Positive")
    void getAllCheckOutDetailsPageWisePositiveTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        Page<CheckOut> checkOutListPageWisePositive = new PageImpl<>(mockCheckOutDataListPositiveData());

        when(checkOutService.getAllCheckoutDetailsPageWise(0,5)).thenReturn(checkOutListPageWisePositive);
        // context
        ResultActions getAllCHeckOutDetailsPageWisePositiveResult = mockMvc.perform(get("/api/checkout/service/all/pageWise?page=0&size=5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwtAuthToken))
                .andExpect(status().isOk());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {}",getAllCHeckOutDetailsPageWisePositiveResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {}"+getAllCHeckOutDetailsPageWisePositiveResult.andReturn().getResponse().getContentAsString());
        // event
        getAllCHeckOutDetailsPageWisePositiveResult
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.size()",is(11)))
                .andExpect(jsonPath("$.totalElements",is((int) checkOutListPageWisePositive.getTotalElements())));
    }

    @Test
    @DisplayName("Get All CheckOut Details Page Wise : Negative")
    void getAllCheckOutDetailsPageWiseNegativeTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        Page<CheckOut> checkOutListPageWiseNegative = new PageImpl<>(mockCheckOutDataListNegativeData());

        when(checkOutService.getAllCheckoutDetailsPageWise(0,5)).thenReturn(checkOutListPageWiseNegative);
        // context
        ResultActions getAllCHeckOutDetailsPageWiseNegativeResult = mockMvc.perform(get("/api/checkout/service/all/pageWise?page=0&size=5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwtAuthToken))
                .andExpect(status().isOk());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {}",getAllCHeckOutDetailsPageWiseNegativeResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {}"+getAllCHeckOutDetailsPageWiseNegativeResult.andReturn().getResponse().getContentAsString());
        // event
        getAllCHeckOutDetailsPageWiseNegativeResult
                .andExpect(jsonPath("$.size()").isNotEmpty())
                .andExpect(jsonPath("$.totalElements",is((int) checkOutListPageWiseNegative.getTotalElements())));
    }

    @Test
    @DisplayName("Get CheckOut Details By CheckOut Id : Positive")
    void getAllCheckOutByCheckOutIdPositiveTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = mockCheckOutData().getUserEmail();
        Long checkoutId = mockCheckOutData().getCheckoutId();

        when(checkOutService.getCheckOutDetailsByCheckOutId(checkoutId)).thenReturn(mockCheckOutData());
        //
        // context
        ResultActions getCheckOutDetailsByCheckOutIdPositiveResult = mockMvc.perform(get("/api/checkout/service/getById?checkoutId="+checkoutId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwtAuthToken))
                .andExpect(status().isOk());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {}",getCheckOutDetailsByCheckOutIdPositiveResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {}"+getCheckOutDetailsByCheckOutIdPositiveResult.andReturn().getResponse().getContentAsString());
        // event
        getCheckOutDetailsByCheckOutIdPositiveResult.andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    @DisplayName("Get CheckOut Details By CheckOut Id : Negative")
    void getCheckOutByCheckOutIdNegativeTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = mockCheckOutData().getUserEmail();
        Long checkoutId = mockCheckOutData().getCheckoutId();

        when(checkOutService.getCheckOutDetailsByCheckOutId(99L)).thenReturn(null);
        //
        // context
        ResultActions getCheckOutDetailsByCheckOutIdNegativeResult = mockMvc.perform(get("/api/checkout/service/getById?checkoutId=99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwtAuthToken))
                .andExpect(status().isOk());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {}",getCheckOutDetailsByCheckOutIdNegativeResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {}"+getCheckOutDetailsByCheckOutIdNegativeResult.andReturn().getResponse().getContentAsString());
        // event
        getCheckOutDetailsByCheckOutIdNegativeResult.andExpect(jsonPath("$").doesNotHaveJsonPath());
    }

    @Test
    @DisplayName("Is Book Checked Out By User : Positive")
    void checkIsBookCheckedOutByUserPositiveTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"emailAddress\"");
        Long bookId = mockCheckOutData().getBookId();

        when(checkOutService.isBookCheckedOutByUser(userEmail,bookId)).thenReturn(true);
        // context
        ResultActions checkIsBookCheckedOutByUserPositiveResult = mockMvc.perform(get("/api/checkout/service/isBookCheckedOutByUser?bookId="+bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwtAuthToken))
                .andExpect(status().isOk());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {}",checkIsBookCheckedOutByUserPositiveResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {}"+checkIsBookCheckedOutByUserPositiveResult.andReturn().getResponse().getContentAsString());
        // event
        checkIsBookCheckedOutByUserPositiveResult.andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$",is(true)));
    }

    @Test
    @DisplayName("Is Book Checked Out By User : Negative")
    void checkIsBookCheckedOutByUserNegativeTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"emailAddress\"");
        Long bookId = mockCheckOutData().getBookId();

        when(checkOutService.isBookCheckedOutByUser(userEmail,99L)).thenReturn(false);
        // context
        ResultActions checkIsBookCheckedOutByUserNegativeResult = mockMvc.perform(get("/api/checkout/service/isBookCheckedOutByUser?bookId=99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwtAuthToken))
                .andExpect(status().isOk());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {}",checkIsBookCheckedOutByUserNegativeResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {}"+checkIsBookCheckedOutByUserNegativeResult.andReturn().getResponse().getContentAsString());
        // event
        checkIsBookCheckedOutByUserNegativeResult.andExpect(jsonPath("$").hasJsonPath());
    }

    @Test
    @DisplayName("Current Loans Count : Positive")
    void currentLoansCountPositiveTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"emailAddress\"");

        when(checkOutService.currentLoansCount(userEmail)).thenReturn(anyInt());
        // context
        ResultActions currentLoansCountPositiveResult = mockMvc.perform(get("/api/checkout/service/currentLoansCount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwtAuthToken))
                .andExpect(status().isOk());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {}",currentLoansCountPositiveResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {}"+currentLoansCountPositiveResult.andReturn().getResponse().getContentAsString());
        // event
        currentLoansCountPositiveResult.andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").hasJsonPath());
    }

    @Test
    @DisplayName("Current Loans Count : Negative")
    void currentLoansCountNegativeTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"emailAddress\"");

        when(checkOutService.currentLoansCount(userEmail)).thenReturn(0);
        // context
        ResultActions currentLoansCountNegativeResult = mockMvc.perform(get("/api/checkout/service/currentLoansCount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwtAuthToken))
                .andExpect(status().isOk());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {}",currentLoansCountNegativeResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {}"+currentLoansCountNegativeResult.andReturn().getResponse().getContentAsString());
        // event
        currentLoansCountNegativeResult.andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$",is(0)));
    }

    @Test
    @DisplayName("Current Loans : Positive")
    void currentLoansPositiveTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"emailAddress\"");
        Long bookId = mockCheckOutData().getBookId();

        when(checkOutService.currentLoans(userEmail)).thenReturn(mockShelfCurrentLoansResponseListPositiveData());
        // context
        ResultActions userCurrentLoansPositiveResult = mockMvc.perform(get("/api/checkout/service/currentLoans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwtAuthToken))
                .andExpect(status().isOk());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {}",userCurrentLoansPositiveResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {}"+userCurrentLoansPositiveResult.andReturn().getResponse().getContentAsString());
        // event
        userCurrentLoansPositiveResult.andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.size()",is((int) mockShelfCurrentLoansResponseListPositiveData().size())));
    }

    @Test
    @DisplayName("Current Loans : Negative")
    void currentLoansNegativeTest() throws Exception
    {
        Set<GrantedAuthority> userAdminAuthorities = Set.of(SecurityUtils.convertToAuthority("USER"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockUserData().getUserId(),mockUserData().getUserName(),mockUserData().getPassword(),mockUserData(),userAdminAuthorities));

        String userEmail = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"emailAddress\"");
        Long bookId = mockCheckOutData().getBookId();

        when(checkOutService.currentLoans(userEmail)).thenReturn(null);
        // context
        ResultActions userCurrentLoansNegativeResult = mockMvc.perform(get("/api/checkout/service/currentLoans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+jwtAuthToken))
                .andExpect(status().isOk());
        // logger
        ControllerTestLogger.info("JSON RESPONSE {}",userCurrentLoansNegativeResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {}"+userCurrentLoansNegativeResult.andReturn().getResponse().getContentAsString());
        // event
        userCurrentLoansNegativeResult.andExpect(jsonPath("$").doesNotHaveJsonPath());
    }

    @AfterEach
    private void setDatabaseAfterTransaction()
    {
        jdbcTemplate.execute(deleteCheckOutQuery);
        jdbcTemplate.execute(deleteBookQuery);
        jdbcTemplate.execute(deleteHistoryQuery);
        jdbcTemplate.execute(dropCheckOutQuery);
        jdbcTemplate.execute(dropBookQuery);
        jdbcTemplate.execute(dropHistoryQuery);
    }
}
