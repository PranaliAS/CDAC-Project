package com.fullstack.library.management.backend.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Users")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId",nullable = false)
    private Long userId;

    @Column(name = "userName",nullable = false)
    private String userName;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "fullName",nullable = false)
    private String fullName;

    @Column(name = "emailAddress",nullable = false)
    private String emailAddress;

    @Column(name = "contactNumber",nullable = false)
    private String contactNumber;

    @Column(name = "role",nullable = false)
    private Role role;

    @Transient
    private String jwtAccessToken;

    @Transient
    private String jwtRefreshToken;
}
