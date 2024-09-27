package com.fullstack.library.management.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table(name = "History")
public class History
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "historyId")
    private Long historyId;

    @Column(name = "userEmail")
    private String userEmail;

    @Column(name = "checkoutDate")
    private String checkoutDate;

    @Column(name = "returnDate")
    private String returnDate;

    @Column(name = "bookTitle")
    private String bookTitle;

    @Column(name = "bookAuthor")
    private String bookAuthor;

    @Column(name = "bookDescription")
    private String bookDescription;

    @Column(name = "bookImage")
    private String bookImage;

    public History(String userEmail,String checkoutDate,String returnDate,String bookTitle,String bookAuthor,String bookDescription,
                   String bookImage)
    {
        this.userEmail = userEmail;
        this.checkoutDate = checkoutDate;
        this.returnDate = returnDate;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookDescription = bookDescription;
        this.bookImage = bookImage;
    }
}
