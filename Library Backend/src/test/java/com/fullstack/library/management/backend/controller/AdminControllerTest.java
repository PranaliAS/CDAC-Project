package com.fullstack.library.management.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullstack.library.management.backend.SpringBootLibraryManagementApplicationTests;
import com.fullstack.library.management.backend.entity.User;
import com.fullstack.library.management.backend.jwtsecurity.JwtProvider;
import com.fullstack.library.management.backend.mockEntityDtoData.mockData;
import com.fullstack.library.management.backend.entity.Book;
import com.fullstack.library.management.backend.entity.Review;
import com.fullstack.library.management.backend.repository.BookRepository;
import com.fullstack.library.management.backend.security.UserPrinciple;
import com.fullstack.library.management.backend.service.AdminService;
import com.fullstack.library.management.backend.service.BookService;
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
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = SpringBootLibraryManagementApplicationTests.class)
@ActiveProfiles("test")
public class AdminControllerTest extends mockData
{
    @Value("${sql.create.table.book}")
    private String createTableBookQuery;

    @Value("${sql.insert.book}")
    private String insertDataBookQuery;

    @Value("${sql.delete.book}")
    private String deleteBookQuery;

    @Value("${sql.drop.book}")
    private String dropBookQuery;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger ControllerTestLogger = LoggerFactory.getLogger(AdminControllerTest.class);

    @MockBean
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtProvider jwtProvider;

    @BeforeEach
    private void setDatabaseBeforeTransaction()
    {
        jdbcTemplate.execute(createTableBookQuery);
        jdbcTemplate.execute(insertDataBookQuery);
    }

    @Test
    @DisplayName("Insert New Book : Positive")
    void insertBookPositiveTest() throws Exception
    {
        Set<GrantedAuthority> userAuthorities = Set.of(SecurityUtils.convertToAuthority("ADMIN"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockAdminUserData().getUserId(),mockAdminUserData().getUserName(),mockAdminUserData().getPassword(),mockAdminUserData(),userAuthorities));

        String admin = ExtractJWT.payloadJWTExtraction(jwtAuthToken,"\"roles\"");

        System.out.println("roles "+admin);

        ControllerTestLogger.info("Insert New Book : Positive");
        doNothing().when(adminService).postBook(mockAddBookRequestData());
        // context
        ResultActions insertNewBooksPositiveResult = mockMvc.perform(post("/api/admin/post/book")
                .header("Authorization","Bearer "+jwtAuthToken)
                .content(objectMapper.writeValueAsString(mockAddBookRequestData()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        // logger
        ControllerTestLogger.error("JSON RESPONSE {}",insertNewBooksPositiveResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+ insertNewBooksPositiveResult.andReturn().getResponse().getContentAsString());
        // event
        insertNewBooksPositiveResult.andExpect(jsonPath("$").isNotEmpty()).
                andExpect(jsonPath("$",is("New Book Added Successfully")));
    }

    @Test
    @DisplayName("Insert New Book : Negative")
    void insertBookNegativeTest() throws Exception
    {
        Set<GrantedAuthority> userAuthorities = Set.of(SecurityUtils.convertToAuthority("ADMIN"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockAdminUserData().getUserId(),mockAdminUserData().getUserName(),mockAdminUserData().getPassword(),mockAdminUserData(),userAuthorities));

        ControllerTestLogger.info("Insert New Book : Negative");
        doNothing().when(adminService).postBook(null);
        // context
        ResultActions insertNewBooksNegativeResult = mockMvc.perform(post("/api/admin/post/book")
                        .header("Authorization","Bearer "+jwtAuthToken)
                        .content(objectMapper.writeValueAsString(""))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        // logger
        ControllerTestLogger.error("JSON RESPONSE {}",insertNewBooksNegativeResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+ insertNewBooksNegativeResult.andReturn().getResponse().getContentAsString());
        // event
        insertNewBooksNegativeResult.andExpect(jsonPath("$").doesNotHaveJsonPath());
    }

    @Test
    @DisplayName("Increase Book Quantity : Positive")
    void increaseBookQuantityPositiveTest() throws Exception
    {
        Set<GrantedAuthority> userAuthorities = Set.of(SecurityUtils.convertToAuthority("ADMIN"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockAdminUserData().getUserId(),mockAdminUserData().getUserName(),mockAdminUserData().getPassword(),mockAdminUserData(),userAuthorities));

        ControllerTestLogger.info("Increase Book Quantity : Positive");
        doNothing().when(adminService).increaseBookQuantity(1L);
        // context
        ResultActions increaseBooksQuantityPositiveResult = mockMvc.perform(put("/api/admin/increase/book/quantity?bookId=1")
                .header("Authorization","Bearer "+jwtAuthToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
        // logger
        ControllerTestLogger.error("JSON RESPONSE {}",increaseBooksQuantityPositiveResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+ increaseBooksQuantityPositiveResult.andReturn().getResponse().getContentAsString());
        // event
        increaseBooksQuantityPositiveResult.andExpect(jsonPath("$").isNotEmpty()).
                andExpect(jsonPath("$",is("book quantity increased Successfully")));
    }

    @Test
    @DisplayName("Increase Book Quantity : Negative")
    void increaseBookQuantityNegativeTest() throws Exception
    {
        Set<GrantedAuthority> userAuthorities = Set.of(SecurityUtils.convertToAuthority("ADMIN"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockAdminUserData().getUserId(),mockAdminUserData().getUserName(),mockAdminUserData().getPassword(),mockAdminUserData(),userAuthorities));

        ControllerTestLogger.info("Insert New Book : Negative");
        doNothing().when(adminService).increaseBookQuantity(99L);
        // context
        ResultActions increaseBooksQuantityNegativeResult = mockMvc.perform(put("/api/admin/increase/book/quantity?bookId=99")
                        .header("Authorization","Bearer "+jwtAuthToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
        // logger
        ControllerTestLogger.error("JSON RESPONSE {}",increaseBooksQuantityNegativeResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+ increaseBooksQuantityNegativeResult.andReturn().getResponse().getContentAsString());
        // event
        increaseBooksQuantityNegativeResult.andExpect(jsonPath("$").hasJsonPath());
    }

    @Test
    @DisplayName("Decrease Book Quantity : Positive")
    void decreaseBookQuantityPositiveTest() throws Exception
    {
        Set<GrantedAuthority> userAuthorities = Set.of(SecurityUtils.convertToAuthority("ADMIN"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockAdminUserData().getUserId(),mockAdminUserData().getUserName(),mockAdminUserData().getPassword(),mockAdminUserData(),userAuthorities));

        ControllerTestLogger.info("Decrease Book Quantity : Positive");
        doNothing().when(adminService).decreaseBookQuantity(1L);
        // context
        ResultActions decreaseBooksQuantityPositiveResult = mockMvc.perform(put("/api/admin/decrease/book/quantity?bookId=1")
                .header("Authorization","Bearer "+jwtAuthToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
        // logger
        ControllerTestLogger.error("JSON RESPONSE {}",decreaseBooksQuantityPositiveResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+ decreaseBooksQuantityPositiveResult.andReturn().getResponse().getContentAsString());
        // event
        decreaseBooksQuantityPositiveResult.andExpect(jsonPath("$").isNotEmpty()).
                andExpect(jsonPath("$",is("book quantity decreased Successfully")));
    }

    @Test
    @DisplayName("Decrease Book Quantity : Negative")
    void decreaseBookQuantityNegativeTest() throws Exception
   {
        Set<GrantedAuthority> userAuthorities = Set.of(SecurityUtils.convertToAuthority("ADMIN"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockAdminUserData().getUserId(),mockAdminUserData().getUserName(),mockAdminUserData().getPassword(),mockAdminUserData(),userAuthorities));

        ControllerTestLogger.info("Decrease Book Quantity : Negative");
       doNothing().when(adminService).decreaseBookQuantity(1L);
       // context
       ResultActions decreaseBooksQuantityNegativeResult = mockMvc.perform(put("/api/admin/decrease/book/quantity?bookId=1")
                       .header("Authorization","Bearer "+jwtAuthToken)
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isAccepted());
        // logger
        ControllerTestLogger.error("JSON RESPONSE {}",decreaseBooksQuantityNegativeResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+ decreaseBooksQuantityNegativeResult.andReturn().getResponse().getContentAsString());
        // event
       decreaseBooksQuantityNegativeResult.andExpect(jsonPath("$").hasJsonPath());
    }

    @Test
    @DisplayName("Delete Book : Positive")
    void deleteBookPositiveTest() throws Exception
    {
        Set<GrantedAuthority> userAuthorities = Set.of(SecurityUtils.convertToAuthority("ADMIN"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockAdminUserData().getUserId(),mockAdminUserData().getUserName(),mockAdminUserData().getPassword(),mockAdminUserData(),userAuthorities));

        ControllerTestLogger.info("Delete Book : Positive");
        doNothing().when(adminService).deleteBook(anyLong());
        // context
        ResultActions deleteBooksNegativeResult = mockMvc.perform(delete("/api/admin/delete/book?bookId=1")
                .header("Authorization","Bearer "+jwtAuthToken)).andExpect(status().isOk());
        // logger
        ControllerTestLogger.error("JSON RESPONSE {}",deleteBooksNegativeResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+ deleteBooksNegativeResult.andReturn().getResponse().getContentAsString());
        // event
        deleteBooksNegativeResult.andExpect(jsonPath("$").isNotEmpty()).
                andExpect(jsonPath("$",is("book deleted Successfully")));
    }

    @Test
    @DisplayName("Delete Book : Negative")
    void deleteBookNegativeTest() throws Exception
    {
        Set<GrantedAuthority> userAuthorities = Set.of(SecurityUtils.convertToAuthority("ADMIN"));

        String jwtAuthToken = jwtProvider.generateToken(new UserPrinciple(mockAdminUserData().getUserId(),mockAdminUserData().getUserName(),mockAdminUserData().getPassword(),mockAdminUserData(),userAuthorities));

        ControllerTestLogger.info("Delete Book : Negative");
        doNothing().when(adminService).deleteBook(anyLong());
        // context
        ResultActions deleteBooksNegativeResult = mockMvc.perform(delete("/api/admin/delete/book?bookId=9").header("Authorization","Bearer "+jwtAuthToken)).andExpect(status().isOk());
        // logger
        ControllerTestLogger.error("JSON RESPONSE {}",deleteBooksNegativeResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+ deleteBooksNegativeResult.andReturn().getResponse().getContentAsString());
        // event
        deleteBooksNegativeResult.andExpect(jsonPath("$").isNotEmpty());
    }

    @AfterEach
    private void setDatabaseAfterTransaction()
    {
        jdbcTemplate.execute(deleteBookQuery);
        jdbcTemplate.execute(dropBookQuery);
    }

}
