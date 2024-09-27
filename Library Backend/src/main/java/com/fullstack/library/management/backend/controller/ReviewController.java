package com.fullstack.library.management.backend.controller;

import com.fullstack.library.management.backend.dto.ReviewRequestDto;
import com.fullstack.library.management.backend.service.BookService;
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
@RequestMapping("api/reviews")
public class ReviewController
{
    Logger reviewsLogger = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    private Environment environment;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("service/port")
    public String getPort()
    {
        reviewsLogger.info("Print Service Running On port number");
        return "Service Running On port number : " + environment.getProperty("local.server.port");
    }

    @PostMapping("service/save")
    @Operation(summary = "save the Review for given user and book")
    public ResponseEntity<?> saveBookReview(@RequestHeader(value = "Authorization") String jwtToken,
                                            @RequestBody ReviewRequestDto reviewRequestDto) throws Exception
    {
        String userEmail = ExtractJWT.payloadJWTExtraction(jwtToken,"\"emailAddress\"");
        if(userEmail == null)
        {
            throw new Exception("userEmail is missing");
        }
        reviewsLogger.info("save the Review for given user and book");
        reviewService.saveBookReview(userEmail,reviewRequestDto);
        return new ResponseEntity<>("Book Review Saved", HttpStatus.CREATED);
    }

    @GetMapping("service/isUserReviewListed")
    @Operation(summary = "Check is the user given the book review")
    public ResponseEntity<?> isBookCheckedOutByUser(@RequestHeader(value = "Authorization") String jwtToken,
                                                    @RequestParam Long bookId) throws Exception
    {
        String userEmail = ExtractJWT.payloadJWTExtraction(jwtToken,"\"emailAddress\"");
        if(userEmail == null)
        {
            throw new Exception("userEmail is missing");
        }
        reviewsLogger.info("Check is the user given the book review");
        return new ResponseEntity<>(reviewService.isUserReviewListed(userEmail,bookId), HttpStatus.OK);
    }

    @GetMapping("service/all")
    @Operation(summary = "Get All The Reviews")
    public ResponseEntity<?> getAllReviews()
    {
        reviewsLogger.info("Get All The Reviews");
        return new ResponseEntity<>(reviewService.getAllReviews(), HttpStatus.OK);
    }

    @GetMapping("service/all/pageWise")
    @Operation(summary = "Get All The Reviews page wise")
    public ResponseEntity<?> getAllReviewsPageWise(@RequestParam int page,
                                                   @RequestParam int size)
    {
        reviewsLogger.info("Get All The Reviews page wise");
        return new ResponseEntity<>(reviewService.getAllReviewsPageWise(page,size), HttpStatus.OK);
    }

    @GetMapping("service/getByReviewId")
    @Operation(summary = "Get The Review for the given id")
    public ResponseEntity<?> getReviewByReviewId(@RequestParam Long reviewId)
    {
        reviewsLogger.info("Get The Review for the given id");
        return new ResponseEntity<>(reviewService.getReviewByReviewId(reviewId), HttpStatus.OK);
    }

    @GetMapping("service/getByBookId/pageWise")
    @Operation(summary = "Get The Reviews for the given Book Id page wise")
    public ResponseEntity<?> getBookCategoryContainingPageWise(@RequestParam Long bookId,
                                                               @RequestParam  int page,
                                                               @RequestParam  int size)
    {
        reviewsLogger.info("Get The Reviews for the given Book Id page wise");
        return new ResponseEntity<>(reviewService.findReviewsByBookId(bookId,page,size), HttpStatus.OK);
    }

}
