package com.fullstack.library.management.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddBookRequest
{
    private String bookTitle;
    private String bookAuthor;
    private String bookDescription;
    private int copies;
    private String bookCategory;
    private String bookImage;
}
