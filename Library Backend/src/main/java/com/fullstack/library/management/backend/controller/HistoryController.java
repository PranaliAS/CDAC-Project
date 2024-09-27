package com.fullstack.library.management.backend.controller;

import com.fullstack.library.management.backend.service.HistoryService;
import com.fullstack.library.management.backend.utils.ExtractJWT;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"http://localhost:3000","http://localhost:6060","http://localhost:9090"})
@RestController
@RequestMapping("api/history")
public class HistoryController
{
    Logger historyLogger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private Environment environment;

    @Autowired
    private HistoryService historyService;

    @GetMapping("service/port")
    public String getPort()
    {
        historyLogger.info("Print Service Running On port number");
        return "Service Running On port number : " + environment.getProperty("local.server.port");
    }

    @GetMapping("service/all")
    @Operation(summary = "Get All The History")
    public ResponseEntity<?> getAllHistory()
    {
        historyLogger.info("Get All The History");
        return new ResponseEntity<>(historyService.getAllHistory(), HttpStatus.OK);
    }

    @GetMapping("service/all/byUser/pageWise")
    @Operation(summary = "Get All The History page wise")
    public ResponseEntity<?> getAllHistoryForUserPageWise(@RequestHeader(value = "Authorization") String jwtToken,
                                                 @RequestParam int page,
                                                 @RequestParam int size) throws Exception
    {
        String userEmail = ExtractJWT.payloadJWTExtraction(jwtToken,"\"emailAddress\"");
        if(userEmail == null)
        {
            throw new Exception("userEmail is missing");
        }
        historyLogger.info("Get All The History FOr Given User page  wise");
        return new ResponseEntity<>(historyService.getBookCheckOutHistoryForUserPageWise(userEmail,page,size), HttpStatus.OK);
    }

}
