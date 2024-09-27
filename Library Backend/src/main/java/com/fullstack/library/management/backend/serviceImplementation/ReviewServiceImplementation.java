package com.fullstack.library.management.backend.serviceImplementation;

import com.fullstack.library.management.backend.dto.ReviewRequestDto;
import com.fullstack.library.management.backend.entity.Review;
import com.fullstack.library.management.backend.repository.BookRepository;
import com.fullstack.library.management.backend.repository.ReviewRepository;
import com.fullstack.library.management.backend.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ReviewServiceImplementation implements ReviewService
{
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public void saveBookReview(String userEmail, ReviewRequestDto reviewRequest) throws Exception
    {
        Review validateBookReview = reviewRepository.findByUserEmailAndBookId(userEmail,reviewRequest.getBookId());
        if(validateBookReview != null)
            {
                throw new Exception("Review Already Created");
            }
        Review review = new Review();
        review.setBookId(reviewRequest.getBookId());
        review.setRating(reviewRequest.getRating());
        review.setUserEmail(userEmail);
        if(reviewRequest.getReviewDescription().isPresent())
        {
            review.setReviewDescription(reviewRequest.getReviewDescription().map(Object::toString).orElse(null));
        }
        review.setDate(Date.valueOf(LocalDate.now()));
        reviewRepository.save(review);
    }

    @Override
    public List<Review> getAllReviews()
    {
        return reviewRepository.findAll();
    }

    @Override
    public Page<Review> getAllReviewsPageWise(int page,int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return reviewRepository.findAll(pageable);
    }

    @Override
    public Review getReviewByReviewId(Long reviewId)
    {
        return reviewRepository.findById(reviewId).get();
    }

    @Override
    public Page<Review> findReviewsByBookId(Long bookId, int pageNo, int size)
    {
        Pageable reviewPageable = PageRequest.of(pageNo,size);
        return reviewRepository.findByBookId(bookId,reviewPageable);
    }

    @Override
    public Boolean isUserReviewListed(String userEmail, Long bookId)
    {
        Review validateBookReview = reviewRepository.findByUserEmailAndBookId(userEmail,bookId);

        if(validateBookReview != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }


}
