package com.fullstack.library.management.backend.repository;

import com.fullstack.library.management.backend.entity.JwtRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtRefreshTokenRepository extends JpaRepository<JwtRefreshToken,String>
{
}
