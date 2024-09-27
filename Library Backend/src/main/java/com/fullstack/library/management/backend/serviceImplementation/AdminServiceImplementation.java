package com.fullstack.library.management.backend.serviceImplementation;

import com.fullstack.library.management.backend.dto.AddBookRequest;
import com.fullstack.library.management.backend.entity.Book;
import com.fullstack.library.management.backend.repository.BookRepository;
import com.fullstack.library.management.backend.repository.CheckOutRepository;
import com.fullstack.library.management.backend.repository.ReviewRepository;
import com.fullstack.library.management.backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AdminServiceImplementation implements AdminService
{
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CheckOutRepository checkOutRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public void postBook(AddBookRequest addBookRequest)
    {
        Book book = new Book();
        book.setBookTitle(addBookRequest.getBookTitle());
        book.setBookAuthor(addBookRequest.getBookAuthor());
        book.setBookDescription(addBookRequest.getBookDescription());
        book.setCopies(addBookRequest.getCopies());
        book.setCopiesAvailable(addBookRequest.getCopies());
        book.setBookCategory(addBookRequest.getBookCategory());
        book.setBookImage(addBookRequest.getBookImage());
        bookRepository.save(book);
    }

    @Override
    public void increaseBookQuantity(Long bookId) throws Exception
    {
        Optional<Book> book = bookRepository.findById(bookId);

        if(!book.isPresent())
        {
            throw new Exception("Book not found");
        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable() + 1);
        book.get().setCopies(book.get().getCopies() + 1);

        bookRepository.save(book.get());
    }

    @Override
    public void decreaseBookQuantity(Long bookId) throws Exception
    {
        Optional<Book> book = bookRepository.findById(bookId);

        if(!book.isPresent())
        {
            throw new Exception("Book not found");
        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable() - 1);
        book.get().setCopies(book.get().getCopies() - 1);

        bookRepository.save(book.get());
    }

    @Override
    public void deleteBook(Long bookId) throws Exception
    {
        Optional<Book> book = bookRepository.findById(bookId);

        if(!book.isPresent())
        {
            throw new Exception("Book not found");
        }

        bookRepository.delete(book.get());
        checkOutRepository.deleteAllByBookId(bookId);
        reviewRepository.deleteAllByBookId(bookId);
    }
}
