package com.fullstack.library.management.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Book")
public class Book
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookId")
    private Long bookId;

    @Column(name = "bookTitle")
    private String bookTitle;

    @Column(name = "bookAuthor")
    private String bookAuthor;

    @Column(name = "bookDescription")
    private String bookDescription;

    @Column(name = "copies")
    private int copies;

    @Column(name = "copiesAvailable")
    private int copiesAvailable;

    @Column(name = "bookCategory")
    private String bookCategory;

    @Column(name = "bookImage")
    private String bookImage;

    public Book(Long bookId,String bookTitle,String bookAuthor,String bookDescription,int copies,int copiesAvailable,String bookCategory)
    {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookDescription = bookDescription;
        this.copies = copies;
        this.copiesAvailable = copiesAvailable;
        this.bookCategory = bookCategory;
    }
}
