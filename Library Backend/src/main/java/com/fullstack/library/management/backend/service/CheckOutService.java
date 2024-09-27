package com.fullstack.library.management.backend.service;

import com.fullstack.library.management.backend.dto.ShelfCurrentLoansResponseDto;
import com.fullstack.library.management.backend.entity.Book;
import com.fullstack.library.management.backend.entity.CheckOut;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CheckOutService {
    List<CheckOut> getAllCheckOutDetails();

    Page<CheckOut> getAllCheckoutDetailsPageWise(int page, int size);

    CheckOut getCheckOutDetailsByCheckOutId(Long checkoutId);

    Book checkOutBook(String userEmail, Long bookId) throws Exception;

    Boolean isBookCheckedOutByUser(String userEmail, Long bookId);

    int currentLoansCount(String userEmail);

    List<ShelfCurrentLoansResponseDto> currentLoans(String userEmail) throws Exception;

    void returnCheckedOutBook(String userEmail, Long bookId) throws Exception;

    void renewLoan(String userEmail, Long bookId) throws Exception;
}
