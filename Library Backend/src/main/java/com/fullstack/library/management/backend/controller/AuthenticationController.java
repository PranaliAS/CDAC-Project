package com.fullstack.library.management.backend.controller;

import com.fullstack.library.management.backend.entity.User;
import com.fullstack.library.management.backend.service.AuthenticationService;
import com.fullstack.library.management.backend.service.JwtRefreshTokenService;
import com.fullstack.library.management.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:3000","http://localhost:6060","http://localhost:9090"})
@RestController
@RequestMapping("api/authentication")
public class AuthenticationController
{
    Logger AuthenticationLogger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private JwtRefreshTokenService jwtRefreshTokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("signUp")
    @Operation(summary = "signUp the user")
    public ResponseEntity<?> signUp(@RequestBody User user)
    {
        AuthenticationLogger.info("signUp the user");
        if(userService.findByUserName(user.getUserName()).isPresent())
        {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(userService.saveUser(user),HttpStatus.CREATED);
    }

    @PostMapping("signIn")
    @Operation(summary = "signIn the user")
    public ResponseEntity<?> signIn(@RequestBody User user)
    {
        AuthenticationLogger.info("signIn the user");
        return new ResponseEntity<>(authenticationService.signInAndReturnJwtRefreshAndAccessToken(user),HttpStatus.OK);
    }

    @PostMapping("jwtRefreshToken")
    @Operation(summary = "generate new Jwt Refresh Token")
    public ResponseEntity<?> jwtRefreshToken(@RequestParam String jwtToken)
    {
        AuthenticationLogger.info("generate new Jwt Refresh Token");
        return new ResponseEntity<>(jwtRefreshTokenService.generateAccessTokenFromJwtRefreshToken(jwtToken),HttpStatus.OK);
    }
}
