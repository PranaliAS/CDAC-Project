package com.fullstack.library.management.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table(name = "CheckOut")
public class CheckOut
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checkoutId")
    private Long checkoutId;

    @Column(name = "userEmail")
    private String userEmail;

    @Column(name = "checkoutDate")
    private String checkoutDate;

    @Column(name = "returnDate")
    private String returnDate;

    @Column(name = "bookId")
    private Long bookId;

    public CheckOut(String userEmail,String checkoutDate,String returnDate,Long bookId)
    {
        this.userEmail = userEmail;
        this.checkoutDate = checkoutDate;
        this.returnDate = returnDate;
        this.bookId = bookId;
    }
}
