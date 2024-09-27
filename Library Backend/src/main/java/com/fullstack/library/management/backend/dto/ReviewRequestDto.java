package com.fullstack.library.management.backend.dto;

import lombok.Data;
import java.util.Optional;

@Data
public class ReviewRequestDto
{
    private double rating;

    private Long bookId;

    private Optional<String> reviewDescription;
}
