package com.fullstack.library.management.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullstack.library.management.backend.SpringBootLibraryManagementApplicationTests;
import com.fullstack.library.management.backend.entity.Book;
import com.fullstack.library.management.backend.mockEntityDtoData.mockData;
import com.fullstack.library.management.backend.service.BookService;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = SpringBootLibraryManagementApplicationTests.class)
@ActiveProfiles("test")
public class BookControllerTest extends mockData
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

    private static final Logger ControllerTestLogger = LoggerFactory.getLogger(BookControllerTest.class);

    @MockBean
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    private void setDatabaseBeforeTransaction()
    {
        jdbcTemplate.execute(createTableBookQuery);
        jdbcTemplate.execute(insertDataBookQuery);
    }

    @Test
    @DisplayName("Get all Books : Positive")
    void getAllBooksPositiveTest() throws Exception
    {
        List<Book> bookListPositive = mockBookListPositiveData();
        when(bookService.getAllBooks()).thenReturn(bookListPositive);
        // context
        ResultActions getAllBooksPositiveResult = mockMvc.perform(get("/api/books/service/all").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        // logger
        ControllerTestLogger.error("JSON RESPONSE {}",getAllBooksPositiveResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+ getAllBooksPositiveResult.andReturn().getResponse().getContentAsString());
        // event
        getAllBooksPositiveResult.andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$",hasSize(greaterThanOrEqualTo(0))));

    }

    @Test
    @DisplayName("Get all Books : Negative")
    void getAllBooksNegativeTest() throws Exception
    {
        List<Book> bookListNegative = mockBookListNegativeData();
        when(bookService.getAllBooks()).thenReturn(bookListNegative);
        // context
        ResultActions getAllBooksNegativeResult = mockMvc.perform(get("/api/books/service/all").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        // logger
        ControllerTestLogger.error("JSON RESPONSE {}",getAllBooksNegativeResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+ getAllBooksNegativeResult.andReturn().getResponse().getContentAsString());
        // event
        getAllBooksNegativeResult.andExpect(jsonPath("$.size()",is(bookListNegative.size())))
                .andExpect(jsonPath("$",hasSize(greaterThanOrEqualTo(0))));

    }

        @Test
        @DisplayName("Get all Books Page Wise : Positive")
        void getAllBooksPageWisePositiveTest() throws Exception
    {
        List<Book> bookListPositive = mockBookListPositiveData();
        Page<Book> bookListPageWisePositive = new PageImpl<>(bookListPositive);
        when(bookService.getAllBooksPageWise(0,9)).thenReturn(bookListPageWisePositive);
        // context
        ResultActions getAllBooksPageWisePositiveResult = mockMvc.perform(get("/api/books/service/all/pageWise?page=0&size=9").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        // logger
        ControllerTestLogger.error("JSON RESPONSE {}",getAllBooksPageWisePositiveResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+ getAllBooksPageWisePositiveResult.andReturn().getResponse().getContentAsString());
        // event
        getAllBooksPageWisePositiveResult.andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.totalElements",is(((int) bookListPageWisePositive.getTotalElements()))));

    }

        @Test
        @DisplayName("Get all Books Page Wise : Negative")
        void getAllBooksPageWiseNegativeTest() throws Exception
        {
            List<Book> bookListNegative = mockBookListNegativeData();
            Page<Book> bookListPageWiseNegative = new PageImpl<>(bookListNegative);
            when(bookService.getAllBooksPageWise(0,9)).thenReturn(bookListPageWiseNegative);
            // context
            ResultActions getAllBooksPageWiseNegativeResult = mockMvc.perform(get("/api/books/service/all/pageWise?page=0&size=9").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
            // logger
            ControllerTestLogger.error("JSON RESPONSE {}",getAllBooksPageWiseNegativeResult.andReturn().getResponse().getContentAsString());
            System.out.println("JSON RESPONSE {} "+ getAllBooksPageWiseNegativeResult.andReturn().getResponse().getContentAsString());
            // event
            getAllBooksPageWiseNegativeResult.andExpect(jsonPath("$.content",is(bookListPageWiseNegative.getContent())))
                    .andExpect(jsonPath("$.totalElements",is(((int) bookListPageWiseNegative.getTotalElements()))))
                    .andExpect(jsonPath("$.size",is(bookListPageWiseNegative.getContent().size())));

        }

    @Test
    @DisplayName("Get Book By Book Id : Positive")
    void getBookByBookIdPositiveTest() throws Exception
    {
        when(bookService.getBookByBookId(6L)).thenReturn(mockBookData());
        // context
        ResultActions getBookByBookIdPositiveResult = mockMvc.perform(get("/api/books/service/getById?bookId=6").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        // logger
        ControllerTestLogger.error("JSON RESPONSE {}",getBookByBookIdPositiveResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+ getBookByBookIdPositiveResult.andReturn().getResponse().getContentAsString());
        // event
        getBookByBookIdPositiveResult.andExpect(jsonPath("$").isNotEmpty());

    }

    @Test
    @DisplayName("Get Book By Book Id : Negative")
    void getBookByBookIdNegativeTest() throws Exception
    {
        when(bookService.getBookByBookId(99L)).thenReturn(null);
        // context
        ResultActions getBookByBookIdNegativeResult = mockMvc.perform(get("/api/books/service/getById?bookId=99").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        // logger
        ControllerTestLogger.error("JSON RESPONSE {}",getBookByBookIdNegativeResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+ getBookByBookIdNegativeResult.andReturn().getResponse().getContentAsString());
        // event
        getBookByBookIdNegativeResult.andExpect(jsonPath("$").doesNotHaveJsonPath());
    }

    @Test
    @DisplayName("Get Book Title Containing Page Wise : Positive")
    void getBookTitleContainingPageWisePositiveTest() throws Exception
    {
        List<Book> bookListPositive = mockBookListPositiveData();
        Page<Book> bookListPageWisePositive = new PageImpl<>(bookListPositive);
        when(bookService.getBookTitleContainingPageWise("javascript",0,6)).thenReturn(bookListPageWisePositive);
        // context
        ResultActions getBookTitleContainingPageWisePositiveResult = mockMvc.perform(get("/api/books/service/getByTitle/pageWise?bookTitle=javascript&page=0&size=6").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        // logger
        ControllerTestLogger.error("JSON RESPONSE {}",getBookTitleContainingPageWisePositiveResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+ getBookTitleContainingPageWisePositiveResult.andReturn().getResponse().getContentAsString());
        // event
        getBookTitleContainingPageWisePositiveResult.andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.totalElements",is(((int) bookListPageWisePositive.getTotalElements()))));

    }

    @Test
    @DisplayName("Get Book Title Containing Page Wise : Negative")
    void getBookTitleContainingPageWiseNegativeTest() throws Exception
    {
        List<Book> bookListNegative = mockBookListNegativeData();
        Page<Book> bookListPageWiseNegative = new PageImpl<>(bookListNegative);
        when(bookService.getBookTitleContainingPageWise("test",0,6)).thenReturn(bookListPageWiseNegative);
        // context
        ResultActions getBookTitleContainingPageWiseNegativeResult = mockMvc.perform(get("/api/books/service/getByTitle/pageWise?bookTitle=test&page=0&size=6").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        // logger
        ControllerTestLogger.error("JSON RESPONSE {}",getBookTitleContainingPageWiseNegativeResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+ getBookTitleContainingPageWiseNegativeResult.andReturn().getResponse().getContentAsString());
        // event
        getBookTitleContainingPageWiseNegativeResult.andExpect(jsonPath("$.content").hasJsonPath())
                .andExpect(jsonPath("$.totalElements").hasJsonPath())
                .andExpect(jsonPath("$.size").hasJsonPath());

    }

    @Test
    @DisplayName("Get Book Category Containing Page Wise : Positive")
    void getBookCategoryContainingPageWisePositiveTest() throws Exception
    {
        List<Book> bookListPositive = mockBookListPositiveData();
        Page<Book> bookListPageWisePositive = new PageImpl<>(bookListPositive);
        when(bookService.getBookCategoryContainingPageWise("BE",0,6)).thenReturn(bookListPageWisePositive);
        // context
        ResultActions getBookCategoryContainingPageWisePositiveResult = mockMvc.perform(get("/api/books/service/getByCategory/pageWise?bookCategory=BE&page=0&size=6").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        // logger
        ControllerTestLogger.error("JSON RESPONSE {}",getBookCategoryContainingPageWisePositiveResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+ getBookCategoryContainingPageWisePositiveResult.andReturn().getResponse().getContentAsString());
        // event
        getBookCategoryContainingPageWisePositiveResult.andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.totalElements",is(((int) bookListPageWisePositive.getTotalElements()))));

    }

    @Test
    @DisplayName("Get Book Category Containing Page Wise : Negative")
    void getBookCategoryContainingPageWiseNegativeTest() throws Exception
    {
        List<Book> bookListNegative = mockBookListNegativeData();
        Page<Book> bookListPageWiseNegative = new PageImpl<>(bookListNegative);
        when(bookService.getBookCategoryContainingPageWise("TE",0,6)).thenReturn(bookListPageWiseNegative);
        // context
        ResultActions getBookCategoryContainingPageWiseNegativeResult = mockMvc.perform(get("/api/books/service/getByCategory/pageWise?bookCategory=TE&page=0&size=6").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        // logger
        ControllerTestLogger.error("JSON RESPONSE {}",getBookCategoryContainingPageWiseNegativeResult.andReturn().getResponse().getContentAsString());
        System.out.println("JSON RESPONSE {} "+ getBookCategoryContainingPageWiseNegativeResult.andReturn().getResponse().getContentAsString());
        // event
        getBookCategoryContainingPageWiseNegativeResult.andExpect(jsonPath("$.content").hasJsonPath())
                .andExpect(jsonPath("$.totalElements").hasJsonPath())
                .andExpect(jsonPath("$.size").hasJsonPath());

    }

    @AfterEach
    private void setDatabaseAfterTransaction()
    {
        jdbcTemplate.execute(deleteBookQuery);
        jdbcTemplate.execute(dropBookQuery);
    }
}
