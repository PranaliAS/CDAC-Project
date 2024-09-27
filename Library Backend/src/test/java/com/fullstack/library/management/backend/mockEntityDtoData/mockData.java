package com.fullstack.library.management.backend.mockEntityDtoData;

import com.fullstack.library.management.backend.dto.AddBookRequest;
import com.fullstack.library.management.backend.dto.AdminMessageRequest;
import com.fullstack.library.management.backend.dto.ReviewRequestDto;
import com.fullstack.library.management.backend.dto.ShelfCurrentLoansResponseDto;
import com.fullstack.library.management.backend.dto.UserMessageRequest;
import com.fullstack.library.management.backend.entity.Book;
import com.fullstack.library.management.backend.entity.CheckOut;
import com.fullstack.library.management.backend.entity.History;
import com.fullstack.library.management.backend.entity.JwtRefreshToken;
import com.fullstack.library.management.backend.entity.Messages;
import com.fullstack.library.management.backend.entity.Review;
import com.fullstack.library.management.backend.entity.Role;
import com.fullstack.library.management.backend.entity.User;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class mockData
{

    protected User mockUserData()
    {
        User mockUser = new User();
        mockUser.setUserId(1L);
        mockUser.setUserName("Nidhi");
        mockUser.setEmailAddress("nidhi@gmail.com");
        mockUser.setFullName("Nidhi Upadhyay");
        mockUser.setContactNumber("9313886634");
        mockUser.setPassword("Nidhi@2507");
        mockUser.setRole(Role.USER);
        return mockUser;
    }

    protected User mockAdminUserData()
    {
        User mockAdminUser = new User();
        mockAdminUser.setUserId(2L);
        mockAdminUser.setUserName("Admin");
        mockAdminUser.setEmailAddress("admin@gmail.com");
        mockAdminUser.setFullName("Admin");
        mockAdminUser.setPassword("admin@123");
        mockAdminUser.setContactNumber("9988771166");
        mockAdminUser.setRole(Role.ADMIN);
        return mockAdminUser;
    }

    protected List<User> mockUserListPositiveData()
    {
        List<User> mockUserListPositive = new ArrayList<>();
        User mockUserFirst = new User();
        User mockUserSecond = new User();
        mockUserFirst.setUserId(1L);
        mockUserSecond.setUserId(6L);
        mockUserFirst.setUserName("Nidhi");
        mockUserSecond.setUserName("Kirti");
        mockUserFirst.setEmailAddress("nidhi@gmail.com");
        mockUserSecond.setEmailAddress("kirtishekhar1997@gmail.com");
        mockUserFirst.setFullName("Nidhi Upadhyay");
        mockUserSecond.setFullName("Kirti Shekhar Pandey");
        mockUserFirst.setContactNumber("9313886634");
        mockUserSecond.setContactNumber("9149396562");
        mockUserListPositive.add(mockUserFirst);
        mockUserListPositive.add(mockUserSecond);
        return mockUserListPositive;
    }

    protected List<User> mockUserListNegativeData()
    {
        List<User> mockUserListNegative = new ArrayList<>();
        User mockUserFirst = new User();
        mockUserFirst.setUserId(1L);
        mockUserFirst.setUserName("Nidhi");
        mockUserFirst.setEmailAddress("nidhi@gmail.com");
        mockUserFirst.setFullName("Nidhi Upadhyay");
        mockUserFirst.setContactNumber("9313886634");
        mockUserListNegative.add(null);
        return mockUserListNegative;
    }

    protected Book mockBookData()
    {
        Book mockBook = new Book();
        mockBook.setBookId(1L);
        mockBook.setBookTitle("Spring Boot Microservices");
        mockBook.setBookCategory("Backend Developer Programming");
        mockBook.setBookAuthor("Prahlad");
        mockBook.setBookDescription("Good book for springboot and microservices concepts");
        mockBook.setCopies(9);
        mockBook.setCopiesAvailable(6);
        return mockBook;
    }

    protected AddBookRequest mockAddBookRequestData()
    {
        AddBookRequest mockAddBookRequest = new AddBookRequest();
        mockAddBookRequest.setBookTitle("Spring Boot Microservices");
        mockAddBookRequest.setBookCategory("Backend Developer Programming");
        mockAddBookRequest.setBookAuthor("Prahlad");
        mockAddBookRequest.setBookDescription("Good book for springboot and microservices concepts");
        mockAddBookRequest.setCopies(9);
        mockAddBookRequest.setBookImage("");
        return mockAddBookRequest;
    }

    protected List<Book> mockBookListPositiveData()
    {
        List<Book> mockBookListPositive = new ArrayList<>();
        Book mockBookFirst = new Book();
        Book mockBookNine = new Book();
        mockBookFirst.setBookId(1L);
        mockBookNine.setBookId(9L);
        mockBookFirst.setBookTitle("Spring Boot Microservices");
        mockBookNine.setBookTitle("Spring Boot Microservices Junit Using H2");
        mockBookFirst.setBookCategory("Backend Developer Programming");
        mockBookNine.setBookCategory("Backend Developer with database Programming");
        mockBookFirst.setBookAuthor("Prahlad");
        mockBookNine.setBookAuthor("Roopak");
        mockBookFirst.setBookDescription("Good book for springboot and microservices concepts");
        mockBookNine.setBookDescription("Good book for springboot and microservices along with testing concepts");
        mockBookFirst.setCopies(9);
        mockBookNine.setCopies(11);
        mockBookFirst.setCopiesAvailable(6);
        mockBookNine.setCopiesAvailable(9);
        mockBookListPositive.add(mockBookFirst);
        mockBookListPositive.add(mockBookNine);
        return mockBookListPositive;
    }

    protected List<Long> mockListOfBookIds()
    {
        List<Long> bookIdsList = new ArrayList<>();
        bookIdsList.add(1L);
        bookIdsList.add(9L);
        return bookIdsList;
    }

    protected List<Book> mockBookListNegativeData()
    {
        List<Book> mockBookListNegative = new ArrayList<>();
        Book mockBook = new Book();
        mockBook.setBookId(1L);
        mockBook.setBookTitle("Spring Boot Microservices");
        mockBook.setBookCategory("Backend Developer Programming");
        mockBook.setBookAuthor("Prahlad");
        mockBook.setBookDescription("Good book for springboot and microservices concepts");
        mockBook.setCopies(9);
        mockBook.setCopiesAvailable(6);
        mockBookListNegative.add(null);
        return mockBookListNegative;
    }

    protected JwtRefreshToken mockJwtRefreshTokenData()
    {
        JwtRefreshToken mockJwtRefreshToken = new JwtRefreshToken();
        mockJwtRefreshToken.setTokenId("1");
        mockJwtRefreshToken.setUserId(1L);
        mockJwtRefreshToken.setTokenCreationDate(LocalDateTime.now());
        mockJwtRefreshToken.setTokenExpirationDate(LocalDateTime.now());
        return mockJwtRefreshToken;
    }

    protected CheckOut mockCheckOutData()
    {
        CheckOut mockCheckOut = new CheckOut();
        mockCheckOut.setCheckoutId(1L);
        mockCheckOut.setBookId(1L);
        mockCheckOut.setUserEmail("kirtishekhar1997@gmail.com");
        mockCheckOut.setReturnDate(LocalDateTime.now().toString());
        mockCheckOut.setCheckoutDate(LocalDateTime.now().plusDays(9).toString());
        return mockCheckOut;
    }

    protected CheckOut mockCheckOutNullData()
    {
        CheckOut mockCheckOutNull = new CheckOut();
        return mockCheckOutNull;
    }

    protected List<CheckOut> mockCheckOutDataListPositiveData()
    {
        List<CheckOut> mockCheckOutDataListPositive = new ArrayList<>();
        CheckOut mockCheckOutFirst = new CheckOut();
        CheckOut mockCheckOutSecond = new CheckOut();
        mockCheckOutFirst.setCheckoutId(1L);
        mockCheckOutSecond.setCheckoutId(3L);
        mockCheckOutFirst.setBookId(1L);
        mockCheckOutSecond.setBookId(9L);
        mockCheckOutFirst.setUserEmail("kirtishekhar1997@gmail.com");
        mockCheckOutSecond.setUserEmail("mihircool@gmail.com");
        mockCheckOutFirst.setReturnDate(LocalDateTime.now().toString());
        mockCheckOutSecond.setReturnDate(LocalDateTime.now().toString());
        mockCheckOutFirst.setCheckoutDate(LocalDateTime.now().plusDays(9).toString());
        mockCheckOutSecond.setCheckoutDate(LocalDateTime.now().plusDays(11).toString());
        mockCheckOutDataListPositive.add(mockCheckOutFirst);
        mockCheckOutDataListPositive.add(mockCheckOutSecond);
        return mockCheckOutDataListPositive;
    }

    protected List<CheckOut> mockCheckOutDataListNegativeData()
    {
        List<CheckOut> mockCheckOutDataListNegative = new ArrayList<>();
        CheckOut mockCheckOutFirst = new CheckOut();
        mockCheckOutFirst.setCheckoutId(1L);
        mockCheckOutFirst.setBookId(1L);
        mockCheckOutFirst.setUserEmail("kirtishekhar1997@gmail.com");
        mockCheckOutFirst.setReturnDate(LocalDateTime.now().toString());
        mockCheckOutFirst.setCheckoutDate(LocalDateTime.now().plusDays(9).toString());
        return mockCheckOutDataListNegative;
    }

    protected ShelfCurrentLoansResponseDto mockShelfCurrentLoansResponseData()
    {
        ShelfCurrentLoansResponseDto mockShelfCurrentLoansResponse = new ShelfCurrentLoansResponseDto();
        mockShelfCurrentLoansResponse.setBook(mockBookData());
        mockShelfCurrentLoansResponse.setDaysLeft(LocalDateTime.now().getDayOfMonth());
        return mockShelfCurrentLoansResponse;
    }

    protected List<ShelfCurrentLoansResponseDto> mockShelfCurrentLoansResponseListPositiveData()
    {
        List<ShelfCurrentLoansResponseDto> mockShelfCurrentLoansResponseListPositive = new ArrayList<>();
        ShelfCurrentLoansResponseDto mockShelfCurrentLoansResponseFirst = new ShelfCurrentLoansResponseDto();
        ShelfCurrentLoansResponseDto mockShelfCurrentLoansResponseSecond = new ShelfCurrentLoansResponseDto();
        mockShelfCurrentLoansResponseFirst.setBook(mockBookData());
        mockShelfCurrentLoansResponseSecond.setBook(mockBookData());
        mockShelfCurrentLoansResponseFirst.setDaysLeft(LocalDateTime.now().getDayOfMonth());
        mockShelfCurrentLoansResponseSecond.setDaysLeft(LocalDateTime.now().getDayOfMonth());
        mockShelfCurrentLoansResponseListPositive.add(mockShelfCurrentLoansResponseFirst);
        mockShelfCurrentLoansResponseListPositive.add(mockShelfCurrentLoansResponseSecond);
        return mockShelfCurrentLoansResponseListPositive;
    }

    protected List<ShelfCurrentLoansResponseDto> mockShelfCurrentLoansResponseListNegativeData()
    {
        List<ShelfCurrentLoansResponseDto> mockShelfCurrentLoansResponseListNegative = new ArrayList<>();
        ShelfCurrentLoansResponseDto mockShelfCurrentLoansResponseFirst = new ShelfCurrentLoansResponseDto();
        ShelfCurrentLoansResponseDto mockShelfCurrentLoansResponseSecond = new ShelfCurrentLoansResponseDto();
        mockShelfCurrentLoansResponseFirst.setBook(mockBookData());
        mockShelfCurrentLoansResponseSecond.setBook(mockBookData());
        mockShelfCurrentLoansResponseFirst.setDaysLeft(LocalDateTime.now().getDayOfMonth());
        mockShelfCurrentLoansResponseSecond.setDaysLeft(LocalDateTime.now().getDayOfMonth());
        mockShelfCurrentLoansResponseListNegative.add(null);
        return mockShelfCurrentLoansResponseListNegative;
    }

    protected Review mockReviewData()
    {
        Review mockReview = new Review();
        mockReview.setReviewId(1L);
        mockReview.setUserEmail("kirtishekhar1997@gmail.com");
        mockReview.setRating(4.5);
        mockReview.setReviewDescription("Good and Nice backend developer concept book");
        mockReview.setDate(Date.valueOf(LocalDate.now()));
        mockReview.setBookId(1L);
        return mockReview;
    }

    protected List<Review> mockReviewListPositiveData()
    {
        List<Review> mockReviewListPositive = new ArrayList<>();
        Review mockReviewFirst = new Review();
        Review mockReviewSecond = new Review();
        mockReviewFirst.setReviewId(1L);
        mockReviewSecond.setReviewId(3L);
        mockReviewFirst.setUserEmail("kirtishekhar1997@gmail.com");
        mockReviewSecond.setUserEmail("nidhi@gmail.com");
        mockReviewFirst.setRating(4.5);
        mockReviewSecond.setRating(3.5);
        mockReviewFirst.setReviewDescription("Good and Nice backend developer concept book");
        mockReviewSecond.setReviewDescription("Good and Nice backend and frontend developer concept book");
        mockReviewFirst.setDate(Date.valueOf(LocalDate.now()));
        mockReviewSecond.setDate(Date.valueOf(LocalDate.now()));
        mockReviewFirst.setBookId(1L);
        mockReviewSecond.setBookId(16L);
        mockReviewListPositive.add(mockReviewFirst);
        mockReviewListPositive.add(mockReviewSecond);
        return mockReviewListPositive;
    }

    protected List<Review> mockReviewListNegativeData()
    {
        List<Review> mockReviewListNegative = new ArrayList<>();
        Review mockReviewFirst = new Review();
        mockReviewFirst.setReviewId(1L);
        mockReviewFirst.setUserEmail("kirtishekhar1997@gmail.com");
        mockReviewFirst.setRating(4.5);
        mockReviewFirst.setReviewDescription("Good and Nice backend developer concept book");
        mockReviewFirst.setDate(Date.valueOf(LocalDate.now()));
        mockReviewFirst.setBookId(1L);
        mockReviewListNegative.add(null);
        return mockReviewListNegative;
    }

    protected ReviewRequestDto mockReviewRequestData()
    {
        ReviewRequestDto mockReviewRequest = new ReviewRequestDto();
        mockReviewRequest.setRating(4.5);
        mockReviewRequest.setReviewDescription(Optional.of("Good and Nice backend developer concept book"));
        mockReviewRequest.setBookId(1L);
        return mockReviewRequest;
    }

    protected History mockHistoryData()
    {
        History mockHistory = new History();
        mockHistory.setHistoryId(1L);
        mockHistory.setReturnDate(LocalDateTime.now().toString());
        mockHistory.setCheckoutDate(LocalDateTime.now().plusDays(9).toString());
        mockHistory.setUserEmail("kirtishekhar1997@gmail.com");
        mockHistory.setBookTitle("Spring Boot Microservices");
        mockHistory.setBookAuthor("Prahlad");
        mockHistory.setBookDescription("Good book for springBoot and microservices concepts");
        return mockHistory;
    }

    protected List<History> mockHistoryListPositiveData()
    {
        List<History> mockHistoryListPositive = new ArrayList<>();
        History mockHistoryFirst = new History();
        History mockHistorySecond = new History();
        mockHistoryFirst.setHistoryId(1L);
        mockHistorySecond.setHistoryId(6L);
        mockHistoryFirst.setReturnDate(LocalDateTime.now().toString());
        mockHistorySecond.setReturnDate(LocalDateTime.now().toString());
        mockHistoryFirst.setCheckoutDate(LocalDateTime.now().plusDays(9).toString());
        mockHistorySecond.setCheckoutDate(LocalDateTime.now().plusDays(11).toString());
        mockHistoryFirst.setUserEmail("kirtishekhar1997@gmail.com");
        mockHistorySecond.setUserEmail("monucool@gmail.com");
        mockHistoryFirst.setBookTitle("Spring Boot Microservices");
        mockHistorySecond.setBookTitle("Angular React");
        mockHistoryFirst.setBookAuthor("Prahlad");
        mockHistorySecond.setBookAuthor("Roopak");
        mockHistoryFirst.setBookDescription("Good book for springBoot and microservices concepts");
        mockHistorySecond.setBookDescription("Good book for Frontend Development");
        mockHistoryListPositive.add(mockHistoryFirst);
        mockHistoryListPositive.add(mockHistorySecond);
        return mockHistoryListPositive;
    }

    protected List<History> mockHistoryListNegativeData()
    {
        List<History> mockHistoryListNegative = new ArrayList<>();
        History mockHistoryFirst = new History();
        mockHistoryFirst.setHistoryId(1L);
        mockHistoryFirst.setReturnDate(LocalDateTime.now().toString());
        mockHistoryFirst.setCheckoutDate(LocalDateTime.now().plusDays(9).toString());
        mockHistoryFirst.setUserEmail("kirtishekhar1997@gmail.com");
        mockHistoryFirst.setBookTitle("Spring Boot Microservices");
        mockHistoryFirst.setBookAuthor("Prahlad");
        mockHistoryFirst.setBookDescription("Good book for springBoot and microservices concepts");
        mockHistoryListNegative.add(null);
        return mockHistoryListNegative;
    }

    protected Messages mockMessagesData()
    {
        Messages mockMessages = new Messages();
        mockMessages.setMessagesId(1L);
        mockMessages.setUserEmail("kirtishekhar1997@gmail.com");
        mockMessages.setAdminEmail("admin@gmail.com");
        mockMessages.setClosed(true);
        mockMessages.setTitle("SpringBoot Microservices Query");
        mockMessages.setQuestion("Is This Course Valid for frontend Developer?");
        mockMessages.setMessageResponse("No This course is valid for only backend developer!!!");
        return mockMessages;
    }

    protected List<Messages> mockMessagesDataListPositiveData()
    {
        List<Messages> positiveMessagesData = new ArrayList<>();
        Messages mockMessagesFirst = new Messages();
        Messages mockMessagesSecond = new Messages();
        mockMessagesFirst.setMessagesId(1L);
        mockMessagesSecond.setMessagesId(9L);
        mockMessagesFirst.setUserEmail("kirtishekhar1997@gmail.com");
        mockMessagesSecond.setUserEmail("nidhi@gmail.com");
        mockMessagesFirst.setAdminEmail("admin@gmail.com");
        mockMessagesSecond.setAdminEmail("admin@gmail.com");
        mockMessagesFirst.setClosed(true);
        mockMessagesSecond.setClosed(false);
        mockMessagesFirst.setTitle("SpringBoot Microservices Query");
        mockMessagesSecond.setTitle("SpringBoot Microservices Gateway API Query");
        mockMessagesFirst.setQuestion("Is This Course Valid for frontend Developer?");
        mockMessagesSecond.setQuestion("Is This Course Gives full knowledge on springBoot microservices?");
        mockMessagesFirst.setMessageResponse("No This course is valid for only backend developer!!!");
        mockMessagesSecond.setMessageResponse("Yes This course gives full description for springBoot microservices!!!");
        positiveMessagesData.add(mockMessagesFirst);
        positiveMessagesData.add(mockMessagesSecond);
        return positiveMessagesData;
    }

    protected List<Messages> mockMessagesDataListNegativeData()
    {
        List<Messages> positiveMessagesData = new ArrayList<>();
        Messages mockMessagesFirst = new Messages();
        Messages mockMessagesSecond = new Messages();
        positiveMessagesData.add(null);
        return positiveMessagesData;
    }

    protected AdminMessageRequest mockAdminMessageRequestData()
    {
        AdminMessageRequest mockAdminMessageRequest = new AdminMessageRequest();
        mockAdminMessageRequest.setQuestionId(1L);
        mockAdminMessageRequest.setMessageResponse("No This course is valid for only backend developer!!!");
        return mockAdminMessageRequest;
    }

    protected UserMessageRequest mockUserMessageRequestData()
    {
        UserMessageRequest mockUserMessageRequest = new UserMessageRequest();
        mockUserMessageRequest.setTitle("SpringBoot Microservices Query");
        mockUserMessageRequest.setQuestion("Is This Course Valid for frontend Developer?");
        return mockUserMessageRequest;
    }
}
