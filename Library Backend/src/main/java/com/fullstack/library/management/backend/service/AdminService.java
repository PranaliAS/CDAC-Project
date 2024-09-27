package com.fullstack.library.management.backend.service;

import com.fullstack.library.management.backend.dto.AddBookRequest;

public interface AdminService {
    void postBook(AddBookRequest addBookRequest);

    void increaseBookQuantity(Long bookId) throws Exception;

    void decreaseBookQuantity(Long bookId) throws Exception;

    void deleteBook(Long bookId) throws Exception;
}
