package com.fullstack.library.management.backend.controller;

import com.fullstack.library.management.backend.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:3000","http://localhost:6060","http://localhost:9090"})
@RestController
@RequestMapping("api/books")
public class BookController
{
    Logger booksLogger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private Environment environment;

    @Autowired
    private BookService bookService;

    @GetMapping("service/port")
    public String getPort()
    {
        booksLogger.info("Print Service Running On port number");
        return "Service Running On port number : " + environment.getProperty("local.server.port");
    }

    @GetMapping("service/all")
    @Operation(summary = "Get All The Books")
    public ResponseEntity<?> getAllBooks()
    {
        booksLogger.info("Get All The Books");
        return new ResponseEntity<>(bookService.getAllBooks(), HttpStatus.OK);
    }

    @GetMapping("service/all/pageWise")
    @Operation(summary = "Get All The Books page wise")
    public ResponseEntity<?> getAllBooksPageWise(@RequestParam int page,
                                                 @RequestParam int size)
    {
        booksLogger.info("Get All The Books page wise");
        return new ResponseEntity<>(bookService.getAllBooksPageWise(page,size), HttpStatus.OK);
    }

    @GetMapping("service/getById")
    @Operation(summary = "Get The Book for the given id")
    public ResponseEntity<?> getBookByBookId(@RequestParam Long bookId)
    {
        booksLogger.info("Get The Book for the given id");
        return new ResponseEntity<>(bookService.getBookByBookId(bookId), HttpStatus.OK);
    }

    @GetMapping("service/getByTitle/pageWise")
    @Operation(summary = "Get The Book for the given title page wise")
    public ResponseEntity<?> getBookTitleContainingPageWise(@RequestParam String bookTitle,
                                                            @RequestParam  int page,
                                                            @RequestParam int size)
    {
        booksLogger.info("Get The Book for the given title page wise");
        return new ResponseEntity<>(bookService.getBookTitleContainingPageWise(bookTitle,page,size), HttpStatus.OK);
    }

    @GetMapping("service/getByCategory/pageWise")
    @Operation(summary = "Get The Book for the given category page wise")
    public ResponseEntity<?> getBookCategoryContainingPageWise(@RequestParam String bookCategory,
                                                               @RequestParam  int page,
                                                               @RequestParam  int size)
    {
        booksLogger.info("Get The Book for the given category page wise");
        return new ResponseEntity<>(bookService.getBookCategoryContainingPageWise(bookCategory,page,size), HttpStatus.OK);
    }

}
