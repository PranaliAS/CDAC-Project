package com.fullstack.library.management.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Messages")
public class Messages
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "messagesId")
    private Long messagesId;

    @Column(name = "userEmail")
    private String userEmail;

    @Column(name = "title")
    private String title;

    @Column(name = "question")
    private String question;

    @Column(name = "adminEmail")
    private String adminEmail;

    @Column(name = "messageResponse")
    private String messageResponse;

    @Column(name = "closed")
    private boolean closed;
}
