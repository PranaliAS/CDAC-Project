package com.fullstack.library.management.backend.controller;

import com.fullstack.library.management.backend.dto.AdminMessageRequest;
import com.fullstack.library.management.backend.dto.UserMessageRequest;
import com.fullstack.library.management.backend.entity.Messages;
import com.fullstack.library.management.backend.service.MessagesService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"http://localhost:3000","http://localhost:6060","http://localhost:9090"})
@RestController
@RequestMapping("api/messages")
public class MessagesController
{
    Logger messagesLogger = LoggerFactory.getLogger(MessagesController.class);

    @Autowired
    private Environment environment;

    @Autowired
    private MessagesService messagesService;

    @GetMapping("service/port")
    public String getPort()
    {
        messagesLogger.info("Print Service Running On port number");
        return "Service Running On port number : " + environment.getProperty("local.server.port");
    }

    @PostMapping("service/postMessage")
    @Operation(summary = "Save the new message to the database")
    public ResponseEntity<?> postMessages(@RequestHeader(value = "Authorization") String jwtToken,
                                                        @RequestBody UserMessageRequest messagesRequest) throws Exception
    {
        String userEmail = ExtractJWT.payloadJWTExtraction(jwtToken,"\"emailAddress\"");
        if(userEmail == null)
        {
            throw new Exception("Save the new message to the database");
        }
        messagesLogger.info("Save the new message to the database");
        messagesService.postMessage(messagesRequest,userEmail);
        return new ResponseEntity<>("Message Posted Successfully", HttpStatus.CREATED);
    }

    @GetMapping("service/MessagesByUser/pageWise")
    @Operation(summary = "Get  The Messages By User Email Page Wise")
    public ResponseEntity<?> findMessagesByUserPageWise(@RequestHeader(value = "Authorization") String jwtToken,
                                                        @RequestParam int page,
                                                        @RequestParam int size) throws Exception
    {
        String userEmail = ExtractJWT.payloadJWTExtraction(jwtToken,"\"emailAddress\"");
        if(userEmail == null)
        {
            throw new Exception("Get  The Messages By User Email Page Wise");
        }
        messagesLogger.info("Get  The Messages By User Email Page Wise");
        return new ResponseEntity<>(messagesService.findMessagesByUserPageWise(userEmail,page,size), HttpStatus.OK);
    }

    @GetMapping("service/MessagesByClosed/pageWise")
    @Operation(summary = "Get The Messages By Closed Page Wise")
    public ResponseEntity<?> findMessagesByClosedPageWise(@RequestHeader(value = "Authorization") String jwtToken,
                                                          @RequestParam boolean closed,
                                                          @RequestParam int page,
                                                          @RequestParam int size) throws Exception
    {
        messagesLogger.info("Get The Messages By Closed Page Wise");
        return new ResponseEntity<>(messagesService.findMessagesByClosedPageWise(closed,page,size), HttpStatus.OK);
    }

    @PutMapping("service/updateMessage/response")
    @Operation(summary = "Update the existing message response by admin")
    public ResponseEntity<?> postAdminMessages(@RequestHeader(value = "Authorization") String jwtToken,
                                          @RequestBody AdminMessageRequest adminMessageRequest) throws Exception
    {
        String userEmail = ExtractJWT.payloadJWTExtraction(jwtToken,"\"emailAddress\"");
        if(userEmail == null)
        {
            throw new Exception("userEmail is missing");
        }
        messagesLogger.info("Update the existing message response by admin");
        messagesService.putMessage(adminMessageRequest,userEmail);
        return new ResponseEntity<>("Message Responded  and Closed Successfully", HttpStatus.CREATED);
    }

}
