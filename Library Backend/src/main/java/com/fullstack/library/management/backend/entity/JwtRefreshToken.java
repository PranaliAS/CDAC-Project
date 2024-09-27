package com.fullstack.library.management.backend.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "JwtRefreshToken")
public class JwtRefreshToken
{
    @Id
    @Column(name = "tokenId",nullable = false)
    private String tokenId;

    @Column(name = "userId",nullable = false)
    private Long userId;

    @Column(name = "tokenCreationDate",nullable = false)
    private LocalDateTime tokenCreationDate;

    @Column(name = "tokenExpirationDate",nullable = false)
    private LocalDateTime tokenExpirationDate;
}
