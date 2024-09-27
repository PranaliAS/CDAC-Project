import { useEffect, useState } from "react";
import Books from "../../models/booksEntity";
import { SpinnerLoading } from "../Utils/SpinnerLoading";
import { StarsReview } from "../Utils/StarsReview";
import { CheckoutAndReviewBox } from "./CheckoutAndReviewBox";
import Review from "../../models/reviewEntity";
import { LatestReviews } from "./LatestReviews";
import ReviewRequest from "../../models/reviewRequest";
import {
  BOOK_BASE_URL,
  CHECKOUT_BASE_URL,
  REVIEW_BASE_URL,
} from "../../commons/constants";
import { AuthHeader } from "../../services/base.service";
import authenticationService from "../../services/authentication.service";

export const BookCheckoutPage = () => {
  const currentSignInUser = authenticationService.getCurrentUser();
  const [book, setBook] = useState<Books>();
  const [isLoading, setIsLoading] = useState(true);
  const [httpError, setHttpError] = useState(null);

  // Review State
  const [reviews, setReviews] = useState<Review[]>([]);
  const [totalStars, setTotalStars] = useState(0);
  const [isLoadingReview, setIsLoadingReview] = useState(true);

  const [isReviewLeft, setIsReviewLeft] = useState(false);
  const [isLoadingUserReview, setIsLoadingUserReview] = useState(true);

  // Loans Count State
  const [currentLoansCount, setCurrentLoansCount] = useState(0);
  const [isLoadingCurrentLoansCount, setIsLoadingCurrentLoansCount] =
    useState(true);

  // Is Book Check Out?
  const [isCheckedOut, setIsCheckedOut] = useState(false);
  const [isLoadingBookCheckedOut, setIsLoadingBookCheckedOut] = useState(true);

  const bookId = window.location.pathname.split("/")[2];

  useEffect(() => {
    const fetchBooks = async () => {
      const checkoutUrl: string =
        BOOK_BASE_URL + `/service/getById?bookId=${bookId}`;

      const response = await fetch(checkoutUrl);

      if (!response.ok) {
        throw new Error("Something went wrong");
      }

      const responseJson = await response.json();

      const loadedBooks: Books = {
        bookId: responseJson.bookId,
        bookTitle: responseJson.bookTitle,
        bookAuthor: responseJson.bookAuthor,
        bookDescription: responseJson.bookDescription,
        copies: responseJson.copies,
        copiesAvailable: responseJson.copiesAvailable,
        bookCategory: responseJson.bookCategory,
        bookImage: responseJson.bookImage,
      };

      setBook(loadedBooks);
      setIsLoading(false);
    };
    fetchBooks().catch((error: any) => {
      setIsLoading(false);
      setHttpError(error.message);
    });
  }, [bookId, isCheckedOut]);

  useEffect(() => {
    const fetchBookReviews = async () => {
      const reviewUrl: string =
        REVIEW_BASE_URL +
        `/service/getByBookId/pageWise?bookId=${bookId}&page=0&size=5`;

      const response = await fetch(reviewUrl);

      if (!response.ok) {
        throw new Error("Something went wrong");
      }

      const responseJson = await response.json();

      const responseReviewsData = responseJson.content;

      const loadedReviews: Review[] = [];

      let weightedStarReviews: number = 0;

      for (const key in responseReviewsData) {
        loadedReviews.push({
          reviewId: responseReviewsData[key].reviewId,
          userEmail: responseReviewsData[key].userEmail,
          date: responseReviewsData[key].date,
          rating: responseReviewsData[key].rating,
          bookId: responseReviewsData[key].bookId,
          reviewDescription: responseReviewsData[key].reviewDescription,
        });

        weightedStarReviews =
          weightedStarReviews + responseReviewsData[key].rating;
      }

      if (loadedReviews) {
        const RoundStarReviews = (
          Math.round((weightedStarReviews / loadedReviews.length) * 2) / 2
        ).toFixed(1);
        setTotalStars(Number(RoundStarReviews));
      }

      setReviews(loadedReviews);
      setIsLoadingReview(false);
    };

    fetchBookReviews().catch((error: any) => {
      setIsLoadingReview(false);
      setHttpError(error.message);
    });
  }, [bookId, isReviewLeft]);

  useEffect(() => {
    const fetchUserCurrentLoansCount = async () => {
      if (currentSignInUser && currentSignInUser?.userId) {
        const currentLoansCountUrl =
          CHECKOUT_BASE_URL + `/service/currentLoansCount`;
        const currentLoansCountRequestOptions = {
          method: "GET",
          headers: AuthHeader(),
        };

        const currentLoansCountResponse = await fetch(
          currentLoansCountUrl,
          currentLoansCountRequestOptions
        );

        if (!currentLoansCountResponse.ok) {
          throw new Error("Something Went Wrong");
        }

        const currentLoansCountResponseJson =
          await currentLoansCountResponse.json();
        setCurrentLoansCount(currentLoansCountResponseJson);
      }
      setIsLoadingCurrentLoansCount(false);
    };

    fetchUserCurrentLoansCount().catch((error: any) => {
      setIsLoadingCurrentLoansCount(false);
      setHttpError(error.message);
    });
  }, [currentSignInUser, isCheckedOut]);

  useEffect(() => {
    const fetchUserCheckedOutBook = async () => {
      if (currentSignInUser && currentSignInUser?.userId) {
        const userBookCheckOutUrl =
          CHECKOUT_BASE_URL +
          `/service/isBookCheckedOutByUser?bookId=${bookId}`;
        const userBookCheckoutRequestOptions = {
          method: "GET",
          headers: AuthHeader(),
        };

        const userBookCheckoutResponse = await fetch(
          userBookCheckOutUrl,
          userBookCheckoutRequestOptions
        );

        if (!userBookCheckoutResponse.ok) {
          throw new Error("Something Went Wrong");
        }

        const userBookCheckoutResponseJson =
          await userBookCheckoutResponse.json();
        setIsCheckedOut(userBookCheckoutResponseJson);
      }
      setIsLoadingBookCheckedOut(false);
    };
    fetchUserCheckedOutBook().catch((error: any) => {
      setIsLoadingBookCheckedOut(false);
      setHttpError(error.message);
    });
  }, [bookId, currentSignInUser]);

  useEffect(() => {
    const fetchUserReviewOnBook = async () => {
      if (currentSignInUser && currentSignInUser?.userId) {
        const userBookReviewUrl =
          REVIEW_BASE_URL +
          `/service/getByBookId/pageWise?bookId=${bookId}&page=0&size=5`;
        const userBookReviewRequestOptions = {
          method: "GET",
          headers: AuthHeader(),
        };
        const userBookReviewResponse = await fetch(
          userBookReviewUrl,
          userBookReviewRequestOptions
        );
        if (!userBookReviewResponse.ok) {
          throw new Error("Something went wrong");
        }
        const userBookReviewResponseJson = await userBookReviewResponse.json();
        setIsReviewLeft(userBookReviewResponseJson.content);
      }
      setIsLoadingUserReview(false);
    };
    fetchUserReviewOnBook().catch((error: any) => {
      setIsLoadingUserReview(false);
      setHttpError(error.message);
    });
  }, [bookId, currentSignInUser]);

  if (
    isLoading ||
    isLoadingReview ||
    isLoadingCurrentLoansCount ||
    isLoadingBookCheckedOut ||
    isLoadingUserReview
  ) {
    return <SpinnerLoading />;
  }

  if (httpError) {
    return (
      <div className="container m-5">
        <p>{httpError}</p>
      </div>
    );
  }

  async function checkOutBook() {
    const userBookCheckOutUrl =
      CHECKOUT_BASE_URL + `/service/bookCheckout?bookId=${bookId}`;
    const userBookCheckoutRequestOptions = {
      method: "PUT",
      headers: AuthHeader(),
    };

    const userBookCheckoutResponse = await fetch(
      userBookCheckOutUrl,
      userBookCheckoutRequestOptions
    );

    if (!userBookCheckoutResponse.ok) {
      throw new Error("Something Went Wrong");
    }
    setIsCheckedOut(true);
  }

  async function submitBookReview(
    starInput: number,
    reviewDescription: string
  ) {
    let bookId: number = 0;
    if (book?.bookId) {
      bookId = book.bookId;
    }

    const reviewRequestModel = new ReviewRequest(
      starInput,
      bookId,
      reviewDescription
    );
    const postReviewUrl = REVIEW_BASE_URL + `/service/save`;
    const postReviewRequestOptions = {
      method: "POST",
      header: AuthHeader(),
      body: JSON.stringify(reviewRequestModel),
    };
    const returnPostReviewResponse = await fetch(
      postReviewUrl,
      postReviewRequestOptions
    );
    if (!returnPostReviewResponse.ok) {
      throw new Error("Something went wrong");
    }
    setIsReviewLeft(true);
  }

  return (
    <div>
      <div className="container d-none d-lg-block">
        <div className="row mt-5">
          <div className="col-sm-2 col-md-2">
            {book?.bookImage ? (
              <img src={book?.bookImage} width="226" height="349" alt="Book" />
            ) : (
              <img
                src={require("./../../Images/BooksImages/book-luv2code-1000.png")}
                width="226"
                height="349"
                alt="Book"
              />
            )}
          </div>
          <div className="col-4 col-md-4 container">
            <div className="ml-2">
              <h2>{book?.bookTitle}</h2>
              <h5 className="text-primary">{book?.bookAuthor}</h5>
              <p className="lead">{book?.bookDescription}</p>
              <StarsReview rating={totalStars} size={32} />
            </div>
          </div>
          <CheckoutAndReviewBox
            book={book}
            mobile={false}
            currentLoansCount={currentLoansCount}
            isBookCheckedOut={isCheckedOut}
            checkOutBook={checkOutBook}
            isUserReviewLeft={isReviewLeft}
            submitBookReview={submitBookReview}
          />
        </div>
        <hr />
        <LatestReviews
          latestBookReviews={reviews}
          bookId={book?.bookId}
          mobile={false}
        />
      </div>
      <div className="container d-lg-none mt-5">
        <div className="d-flex justify-content-center align-items-center">
          {book?.bookImage ? (
            <img src={book?.bookImage} width="226" height="349" alt="Book" />
          ) : (
            <img
              src={require("./../../Images/BooksImages/book-luv2code-1000.png")}
              width="226"
              height="349"
              alt="Book"
            />
          )}
        </div>
        <div className="mt-4">
          <div className="ml-2">
            <h2>{book?.bookTitle}</h2>
            <h5 className="text-primary">{book?.bookAuthor}</h5>
            <p className="lead">{book?.bookDescription}</p>
            <StarsReview rating={4.5} size={32} />
          </div>
        </div>
        <CheckoutAndReviewBox
          book={book}
          mobile={true}
          currentLoansCount={currentLoansCount}
          isBookCheckedOut={isCheckedOut}
          checkOutBook={checkOutBook}
          isUserReviewLeft={isReviewLeft}
          submitBookReview={submitBookReview}
        />
        <hr />
        <LatestReviews
          latestBookReviews={reviews}
          bookId={book?.bookId}
          mobile={true}
        />
      </div>
    </div>
  );
};
