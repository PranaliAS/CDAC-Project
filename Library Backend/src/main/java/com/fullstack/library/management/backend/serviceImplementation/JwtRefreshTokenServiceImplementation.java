package com.fullstack.library.management.backend.serviceImplementation;

import com.fullstack.library.management.backend.service.JwtRefreshTokenService;
import com.fullstack.library.management.backend.entity.JwtRefreshToken;
import com.fullstack.library.management.backend.entity.User;
import com.fullstack.library.management.backend.jwtsecurity.JwtProvider;
import com.fullstack.library.management.backend.repository.JwtRefreshTokenRepository;
import com.fullstack.library.management.backend.repository.UserRepository;
import com.fullstack.library.management.backend.security.UserPrinciple;
import com.fullstack.library.management.backend.service.JwtRefreshTokenService;
import com.fullstack.library.management.backend.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.UUID;

@Service
public class JwtRefreshTokenServiceImplementation implements JwtRefreshTokenService
{
    @Value("${app.jwt.refresh-expiration-in-ms}")
    private Long REFRESH_TOKEN_EXPIRATION_IN_MS;

    @Autowired
    private JwtRefreshTokenRepository jwtRefreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public JwtRefreshToken createJwtRefreshToken(Long userId)
    {
        JwtRefreshToken jwtRefreshToken = new JwtRefreshToken();

        jwtRefreshToken.setTokenId(UUID.randomUUID().toString());
        jwtRefreshToken.setUserId(userId);
        jwtRefreshToken.setTokenCreationDate(LocalDateTime.now());
        jwtRefreshToken.setTokenExpirationDate(LocalDateTime.now().plus(REFRESH_TOKEN_EXPIRATION_IN_MS, ChronoUnit.MILLIS));

        return jwtRefreshTokenRepository.save(jwtRefreshToken);
    }

    @Override
    public User generateAccessTokenFromJwtRefreshToken(String refreshTokenId)
    {
        JwtRefreshToken jwtRefreshToken = jwtRefreshTokenRepository.findById(refreshTokenId).orElseThrow();

        if(jwtRefreshToken.getTokenExpirationDate().isBefore(LocalDateTime.now()))
        {
            throw new RuntimeException("Jwt Refresh Token is not valid");
        }

        User user = userRepository.findById(jwtRefreshToken.getUserId()).orElseThrow();

        UserPrinciple userPrinciple = UserPrinciple.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .password(user.getPassword())
                .build();
        String jwtAccessToken = jwtProvider.generateToken(userPrinciple);

        user.setJwtAccessToken(jwtAccessToken);
        user.setJwtRefreshToken(refreshTokenId);

        return user;
    }
}
