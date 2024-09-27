package com.fullstack.library.management.backend.controller;

import com.fullstack.library.management.backend.entity.Role;
import com.fullstack.library.management.backend.security.UserPrinciple;
import com.fullstack.library.management.backend.service.UserService;
import com.fullstack.library.management.backend.utils.ExtractJWT;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:3000","http://localhost:6060","http://localhost:9090"})
@RestController
@RequestMapping("api/user")
public class UserController
{
    Logger UserLogger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PutMapping("change/{role}")
    @Operation(summary = "user change the role")
    public ResponseEntity<?> changeRole(@AuthenticationPrincipal UserPrinciple userPrinciple, @PathVariable Role role)
    {
        UserLogger.info("user change the role");
        userService.changeRole(role, userPrinciple.getUsername());

        return ResponseEntity.ok(true);
    }

    @GetMapping("get/userEmail")
    @Operation(summary = "return a signed in user emailId")
    public ResponseEntity<?> returnUserEmail(@RequestHeader(value = "Authorization") String jwtToken) throws Exception
    {
        String userEmail = ExtractJWT.payloadJWTExtraction(jwtToken,"\"sub\"");
        if(userEmail == null)
        {
            throw new Exception("userEmail is missing");
        }
        UserLogger.info("return a signed in user emailId");
        return new ResponseEntity<>(userEmail, HttpStatus.OK);
    }
}
