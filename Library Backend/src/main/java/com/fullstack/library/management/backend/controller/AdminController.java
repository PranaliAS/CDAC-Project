package com.fullstack.library.management.backend.controller;

import com.fullstack.library.management.backend.service.AdminService;
import com.fullstack.library.management.backend.service.UserService;
import com.fullstack.library.management.backend.dto.AddBookRequest;
import com.fullstack.library.management.backend.utils.ExtractJWT;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = {"http://localhost:3000","http://localhost:6060","http://localhost:9090"})
@RestController
@RequestMapping("api/admin")
public class AdminController
{
    Logger adminLogger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private Environment environment;

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @GetMapping("service/port")
    public String getPort()
    {
        adminLogger.info("Print Service Running On port number");
        return "Service Running On port number : " + environment.getProperty("local.server.port");
    }

    @GetMapping("users/all")
    @Operation(summary = "admin view all the users")
    public ResponseEntity<?> findAllUsers()
    {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("post/book")
    @Operation(summary = "admin add new book")
    public ResponseEntity<?> postBook(@RequestHeader(value = "Authorization") String jwtToken,
                                      @RequestBody AddBookRequest addBookRequest) throws Exception
    {
        String admin = ExtractJWT.payloadJWTExtraction(jwtToken,"\"roles\"");
        if(admin == null || !admin.equals("ROLE_ADMIN"))
        {
            throw new Exception("Administration page only");
        }
        adminLogger.info("admin add new book");
        adminService.postBook(addBookRequest);
        return new ResponseEntity<>("New Book Added Successfully", HttpStatus.CREATED);
    }

    @PutMapping("increase/book/quantity")
    @Operation(summary = "admin increase book quantity")
    public ResponseEntity<?> increaseBookQuantity(@RequestHeader(value = "Authorization") String jwtToken,
                                      @RequestParam Long bookId) throws Exception
    {
        String admin = ExtractJWT.payloadJWTExtraction(jwtToken,"\"roles\"");
        if(admin == null || !admin.equals("ROLE_ADMIN"))
        {
            throw new Exception("Administration page only");
        }
        adminLogger.info("admin increase book quantity");
        adminService.increaseBookQuantity(bookId);
        return new ResponseEntity<>("book quantity increased Successfully", HttpStatus.ACCEPTED);
    }

    @PutMapping("decrease/book/quantity")
    @Operation(summary = "admin decrease book quantity")
    public ResponseEntity<?> decreaseBookQuantity(@RequestHeader(value = "Authorization") String jwtToken,
                                                  @RequestParam Long bookId) throws Exception
    {
        String admin = ExtractJWT.payloadJWTExtraction(jwtToken,"\"roles\"");
        if(admin == null || !admin.equals("ROLE_ADMIN"))
        {
            throw new Exception("Administration page only");
        }
        adminLogger.info("admin decrease book quantity");
        adminService.decreaseBookQuantity(bookId);
        return new ResponseEntity<>("book quantity decreased Successfully", HttpStatus.ACCEPTED);
    }

    @DeleteMapping("delete/book")
    @Operation(summary = "admin delete a book")
    public ResponseEntity<?> deleteBook(@RequestHeader(value = "Authorization") String jwtToken,
                                                  @RequestParam Long bookId) throws Exception
    {
        String admin = ExtractJWT.payloadJWTExtraction(jwtToken,"\"roles\"");
        if(admin == null || !admin.equals("ROLE_ADMIN"))
        {
            throw new Exception("Administration page only");
        }
        adminLogger.info("admin delete a book");
        adminService.deleteBook(bookId);
        return new ResponseEntity<>("book deleted Successfully", HttpStatus.OK);
    }
}
