package com.fullstack.library.management.backend.service;

import com.fullstack.library.management.backend.entity.JwtRefreshToken;
import com.fullstack.library.management.backend.entity.User;

public interface JwtRefreshTokenService
{
    JwtRefreshToken createJwtRefreshToken(Long userId);

    User generateAccessTokenFromJwtRefreshToken(String refreshTokenId);
}
