package com.fullstack.library.management.backend.service;

import com.fullstack.library.management.backend.entity.User;

public interface AuthenticationService {
    User signInAndReturnJwtRefreshAndAccessToken(User signInRequest);
}
