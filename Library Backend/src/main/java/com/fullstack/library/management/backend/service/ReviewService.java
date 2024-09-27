package com.fullstack.library.management.backend.service;

import com.fullstack.library.management.backend.dto.ReviewRequestDto;
import com.fullstack.library.management.backend.entity.Review;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReviewService {
    void saveBookReview(String userEmail, ReviewRequestDto reviewRequest) throws Exception;

    List<Review> getAllReviews();

    Page<Review> getAllReviewsPageWise(int page, int size);

    Review getReviewByReviewId(Long reviewId);

    Page<Review> findReviewsByBookId(Long bookId, int pageNo, int size);

    Boolean isUserReviewListed(String userEmail, Long bookId);
}
