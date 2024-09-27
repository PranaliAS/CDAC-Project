package com.fullstack.library.management.backend.controller;

import com.fullstack.library.management.backend.service.CheckOutService;
import com.fullstack.library.management.backend.service.ReviewService;
import com.fullstack.library.management.backend.utils.ExtractJWT;
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
@RequestMapping("api/checkout")
public class CheckOutController
{
    Logger checkOutLogger = LoggerFactory.getLogger(CheckOutController.class);

    @Autowired
    private Environment environment;

    @Autowired
    private CheckOutService checkOutService;

    @GetMapping("service/port")
    public String getPort()
    {
        checkOutLogger.info("Print Service Running On port number");
        return "Service Running On port number : " + environment.getProperty("local.server.port");
    }

    @GetMapping("service/all")
    @Operation(summary = "Get All The CheckOut Details")
    public ResponseEntity<?> getAllCheckOutDetails()
    {
        checkOutLogger.info("Get All The CheckOut Details");
        return new ResponseEntity<>(checkOutService.getAllCheckOutDetails(), HttpStatus.OK);
    }

    @GetMapping("service/all/pageWise")
    @Operation(summary = "Get All The CheckOut Details page wise")
    public ResponseEntity<?> getAllCheckOutDetailsPageWise(@RequestParam int page,
                                                 @RequestParam int size)
    {
        checkOutLogger.info("Get All The CheckOut Details page wise");
        return new ResponseEntity<>(checkOutService.getAllCheckoutDetailsPageWise(page,size), HttpStatus.OK);
    }

    @GetMapping("service/getById")
    @Operation(summary = "Get The CheckOut Detail for the given id")
    public ResponseEntity<?> getBookByCheckOutId(@RequestParam Long checkoutId)
    {
        checkOutLogger.info("Get The CheckOut Detail for the given id");
        return new ResponseEntity<>(checkOutService.getCheckOutDetailsByCheckOutId(checkoutId), HttpStatus.OK);
    }

    @PutMapping("service/bookCheckout")
    @Operation(summary = "Checkout the given book for given user email")
    public ResponseEntity<?> checkOutBook(@RequestHeader(value = "Authorization") String jwtToken,
                                          @RequestParam Long bookId) throws Exception
    {
        String userEmail = ExtractJWT.payloadJWTExtraction(jwtToken,"\"emailAddress\"");
        if(userEmail == null)
        {
            throw new Exception("userEmail is missing");
        }
        checkOutLogger.info("Checkout the given book for given user email");
        return new ResponseEntity<>(checkOutService.checkOutBook(userEmail,bookId), HttpStatus.CREATED);
    }

    @GetMapping("service/isBookCheckedOutByUser")
    @Operation(summary = "Check is the given book Checkout by given user email")
    public ResponseEntity<?> isBookCheckedOutByUser(@RequestHeader(value = "Authorization") String jwtToken,
                                                    @RequestParam Long bookId) throws Exception
    {
        String userEmail = ExtractJWT.payloadJWTExtraction(jwtToken,"\"emailAddress\"");
        if(userEmail == null)
        {
            throw new Exception("userEmail is missing");
        }
        checkOutLogger.info("Check is the given book Checkout by given user email");
        return new ResponseEntity<>(checkOutService.isBookCheckedOutByUser(userEmail,bookId), HttpStatus.OK);
    }

    @GetMapping("service/currentLoansCount")
    @Operation(summary = "Get the numbers of books checkout by user")
    public ResponseEntity<?> currentLoansCount(@RequestHeader(value = "Authorization") String jwtToken) throws Exception
    {
        String userEmail = ExtractJWT.payloadJWTExtraction(jwtToken,"\"emailAddress\"");
        if(userEmail == null)
        {
            throw new Exception("userEmail is missing");
        }
        checkOutLogger.info("Get the numbers of books checkout by user");
        return new ResponseEntity<>(checkOutService.currentLoansCount(userEmail), HttpStatus.OK);
    }

    @GetMapping("service/currentLoans")
    @Operation(summary = "Get the numbers of loans the user had")
    public ResponseEntity<?> currentLoans(@RequestHeader(value = "Authorization") String jwtToken) throws Exception
    {
        String userEmail = ExtractJWT.payloadJWTExtraction(jwtToken,"\"emailAddress\"");
        if(userEmail == null)
        {
            throw new Exception("userEmail is missing");
        }
        checkOutLogger.info("Get the numbers of loans the user had");
        return new ResponseEntity<>(checkOutService.currentLoans(userEmail), HttpStatus.OK);
    }

    @PutMapping("service/returnCheckOutBook")
    @Operation(summary = "return the checkout book by given user email")
    public ResponseEntity<?> returnCheckOutBook(@RequestHeader(value = "Authorization") String jwtToken,
                                          @RequestParam Long bookId) throws Exception
    {
        String userEmail = ExtractJWT.payloadJWTExtraction(jwtToken,"\"emailAddress\"");
        if(userEmail == null)
        {
            throw new Exception("userEmail is missing");
        }
        checkOutLogger.info("return the checkout book by given user email");
        checkOutService.returnCheckedOutBook(userEmail,bookId);
        return new ResponseEntity<>(" CheckOut Book Returned", HttpStatus.CREATED);
    }

    @PutMapping("service/renewLoan")
    @Operation(summary = "renew the loan for the checkout book by given user email")
    public ResponseEntity<?> renewLoan(@RequestHeader(value = "Authorization") String jwtToken,
                                                @RequestParam Long bookId) throws Exception
    {
        String userEmail = ExtractJWT.payloadJWTExtraction(jwtToken,"\"emailAddress\"");
        if(userEmail == null)
        {
            throw new Exception("userEmail is missing");
        }
        checkOutLogger.info("renew the loan for the checkout book by given user email");
        checkOutService.renewLoan(userEmail,bookId);
        return new ResponseEntity<>("CheckOut Book Loan Renewed", HttpStatus.CREATED);
    }

}
