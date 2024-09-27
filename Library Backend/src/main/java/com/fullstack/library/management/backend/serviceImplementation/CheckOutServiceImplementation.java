package com.fullstack.library.management.backend.serviceImplementation;

import com.fullstack.library.management.backend.dto.ShelfCurrentLoansResponseDto;
import com.fullstack.library.management.backend.entity.Book;
import com.fullstack.library.management.backend.entity.CheckOut;
import com.fullstack.library.management.backend.entity.History;
import com.fullstack.library.management.backend.repository.BookRepository;
import com.fullstack.library.management.backend.repository.CheckOutRepository;
import com.fullstack.library.management.backend.repository.HistoryRepository;
import com.fullstack.library.management.backend.service.CheckOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class CheckOutServiceImplementation implements CheckOutService
{
    @Autowired
    private CheckOutRepository checkOutRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private HistoryRepository historyRepository;

    @Override
    public List<CheckOut> getAllCheckOutDetails()
    {
        return checkOutRepository.findAll();
    }

    @Override
    public Page<CheckOut> getAllCheckoutDetailsPageWise(int page,int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return checkOutRepository.findAll(pageable);
    }

    @Override
    public CheckOut getCheckOutDetailsByCheckOutId(Long checkoutId)
    {
        return checkOutRepository.findById(checkoutId).get();
    }

    @Override
    public Book checkOutBook(String userEmail, Long bookId) throws Exception
    {
        Optional<Book> findBook = bookRepository.findById(bookId);

        CheckOut validateCheckOutBook = checkOutRepository.findByUserEmailAndBookId(userEmail,bookId);

        if(!findBook.isPresent() || validateCheckOutBook != null || findBook.get().getCopiesAvailable() <= 0)
        {
            throw new Exception("Book doesn't exist or already checked out by user");
        }

        findBook.get().setCopiesAvailable(findBook.get().getCopiesAvailable() -1);
        bookRepository.save(findBook.get());

        CheckOut checkOut = new CheckOut(
                userEmail,
                LocalDateTime.now().toString(),
                LocalDateTime.now().plusDays(7).toString(),
                findBook.get().getBookId()
        );

        checkOutRepository.save(checkOut);

        return findBook.get();
    }

    @Override
    public Boolean isBookCheckedOutByUser(String userEmail, Long bookId)
    {
        CheckOut validateCheckOutBook = checkOutRepository.findByUserEmailAndBookId(userEmail,bookId);

        if(validateCheckOutBook != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public int currentLoansCount(String userEmail)
    {
        return checkOutRepository.findBooksByUserEmail(userEmail).size();
    }

    @Override
    public List<ShelfCurrentLoansResponseDto> currentLoans(String userEmail) throws Exception
    {
        List<ShelfCurrentLoansResponseDto> shelfCurrentLoansResponseList = new ArrayList<>();
        List<CheckOut> checkOutList = checkOutRepository.findBooksByUserEmail(userEmail);

        List<Long> bookIdList = new ArrayList<>();

        for(CheckOut i:checkOutList)
        {
            bookIdList.add(i.getBookId());
        }

        List<Book> bookList = bookRepository.findBooksByBookIds(bookIdList);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for(Book book:bookList)
        {
            Optional<CheckOut> checkOutOptional = checkOutList.stream()
                    .filter(x -> x.getBookId() == book.getBookId()).findFirst();

            if(checkOutOptional.isPresent())
            {
                Date d1 = sdf.parse(checkOutOptional.get().getReturnDate());
                Date d2 = sdf.parse(LocalDate.now().toString());

                TimeUnit time = TimeUnit.DAYS;

                long difference_In_Time = time.convert(d1.getTime() - d2.getTime(),
                        TimeUnit.MILLISECONDS);

                shelfCurrentLoansResponseList.add(new ShelfCurrentLoansResponseDto(book,(int) difference_In_Time));
            }
        }

        return shelfCurrentLoansResponseList;
    }

    @Override
    public void returnCheckedOutBook(String userEmail, Long bookId) throws Exception
    {
        Optional<Book> book = bookRepository.findById(bookId);

        CheckOut validateCheckOut = checkOutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if(!book.isPresent() || validateCheckOut == null)
        {
            throw new Exception("Book does not exist or not checked out by user");
        }

        book.get().setCopiesAvailable((book.get().getCopiesAvailable() + 1));

        bookRepository.save(book.get());

        checkOutRepository.deleteById(validateCheckOut.getCheckoutId());

        History history = new History(
                userEmail,
                validateCheckOut.getCheckoutDate(),
                LocalDate.now().toString(),
                book.get().getBookTitle(),
                book.get().getBookAuthor(),
                book.get().getBookDescription(),
                book.get().getBookImage());

        historyRepository.save(history);
    }

    @Override
    public void renewLoan(String userEmail, Long bookId) throws Exception
    {
        CheckOut validateCheckOut = checkOutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if(validateCheckOut == null)
        {
            throw new Exception("Book does not exist or not checked out by user");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date d1 = sdf.parse(validateCheckOut.getReturnDate());
        Date d2 = sdf.parse(LocalDate.now().toString());

        if(d1.compareTo(d2) > 0 || d1.compareTo(d2) == 0)
        {
            validateCheckOut.setReturnDate(LocalDate.now().plusDays(7).toString());
            checkOutRepository.save(validateCheckOut);
        }
    }
}
