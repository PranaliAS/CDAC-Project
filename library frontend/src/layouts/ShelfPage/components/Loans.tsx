import { useEffect, useState } from "react";
import ShelfCurrentLoans from "../../../models/shelfCurrentLoans";
import { SpinnerLoading } from "../../Utils/SpinnerLoading";
import { Link } from "react-router-dom";
import { LoansModal } from "./LoansModal";
import { AuthHeader } from "../../../services/base.service";
import { CHECKOUT_BASE_URL } from "../../../commons/constants";
import authenticationService from "../../../services/authentication.service";

export const Loans = () => {
  //auth and error
  const currentSignInUser = authenticationService.getCurrentUser();
  const [httpError, setHttpError] = useState(null);

  // Current Loans
  const [shelfCurrentLoans, setShelfCurrentLoans] = useState<
    ShelfCurrentLoans[]
  >([]);
  const [isLoadingUserLoans, setIsLoadingUserLoans] = useState(true);
  const [checkOut, setCheckOut] = useState(false);

  useEffect(() => {
    const fetchUserCurrentLoans = async () => {
      if (currentSignInUser && currentSignInUser?.userId) {
        const userCurrentLoansUrl = CHECKOUT_BASE_URL + `/service/currentLoans`;
        const requestUserCurrentLoansOptions = {
          method: "GET",
          headers: AuthHeader(),
        };
        const userShelfCurrentLoansResponse = await fetch(
          userCurrentLoansUrl,
          requestUserCurrentLoansOptions
        );

        if (!userShelfCurrentLoansResponse.ok) {
          throw new Error("Something went wrong!");
        }

        const userShelfCurrentLoansResponseJson =
          await userShelfCurrentLoansResponse.json();
        setShelfCurrentLoans(userShelfCurrentLoansResponseJson);
      }
      setIsLoadingUserLoans(false);
    };
    fetchUserCurrentLoans().catch((error: any) => {
      setIsLoadingUserLoans(false);
      setHttpError(error.message);
    });
  }, [checkOut, currentSignInUser]);

  if (isLoadingUserLoans) {
    return <SpinnerLoading />;
  }

  if (httpError) {
    return (
      <div className="container m-5">
        <p>{httpError}</p>
      </div>
    );
  }

  async function returnBook(bookId: number) {
    const checkedOutBookReturnUrl =
      CHECKOUT_BASE_URL + `/service/returnCheckOutBook?bookId=${bookId}`;
    const requestCheckedOutBookReturnOptions = {
      method: "PUT",
      headers: AuthHeader(),
    };
    const checkedOutBookReturnResponse = await fetch(
      checkedOutBookReturnUrl,
      requestCheckedOutBookReturnOptions
    );

    if (!checkedOutBookReturnResponse.ok) {
      throw new Error("Something went wrong!");
    }

    setCheckOut(!checkOut);
  }

  async function renewLoan(bookId: number) {
    const renewLoanUrl =
      CHECKOUT_BASE_URL + `/service/renewLoan?bookId=${bookId}`;
    const requestRenewLoanOptions = {
      method: "PUT",
      headers: AuthHeader(),
    };
    const renewLoanResponse = await fetch(
      renewLoanUrl,
      requestRenewLoanOptions
    );

    if (!renewLoanResponse.ok) {
      throw new Error("Something went wrong!");
    }

    setCheckOut(!checkOut);
  }

  return (
    <div>
      {/* Desktop */}
      <div className="d-none d-lg-block mt-2">
        {shelfCurrentLoans.length > 0 ? (
          <>
            <h5>Current Books: </h5>

            {shelfCurrentLoans.map((shelfCurrentLoan) => (
              <div key={shelfCurrentLoan.book.bookId}>
                <div className="row mt-3 mb-3">
                  <div className="col-4 col-md-4 container">
                    {shelfCurrentLoan.book?.bookImage ? (
                      <img
                        src={shelfCurrentLoan.book?.bookImage}
                        width="226"
                        height="349"
                        alt="Book"
                      />
                    ) : (
                      <img
                        src={require("./../../../Images/BooksImages/book-luv2code-1000.png")}
                        width="226"
                        height="349"
                        alt="Book"
                      />
                    )}
                  </div>
                  <div className="card col-3 col-md-3 container d-flex">
                    <div className="card-body">
                      <div className="mt-3">
                        <h4>Book Options</h4>
                        {shelfCurrentLoan.daysLeft > 0 && (
                          <p className="text-secondary">
                            Due in {shelfCurrentLoan.daysLeft} days.
                          </p>
                        )}
                        {shelfCurrentLoan.daysLeft === 0 && (
                          <p className="text-success">Due Today.</p>
                        )}
                        {shelfCurrentLoan.daysLeft < 0 && (
                          <p className="text-danger">
                            Past due by {shelfCurrentLoan.daysLeft} days.
                          </p>
                        )}
                        <div className="list-group mt-3">
                          <button
                            className="list-group-item list-group-item-action"
                            aria-current="true"
                            data-bs-toggle="modal"
                            data-bs-target={`#modal${shelfCurrentLoan.book.bookId}`}
                          >
                            Manage Book
                          </button>
                          <Link
                            to={"search"}
                            className="list-group-item list-group-item-action"
                          >
                            Search more books?
                          </Link>
                        </div>
                      </div>
                      <hr />
                      <p className="mt-3">
                        Help other find their adventure by reviewing your loan.
                      </p>
                      <Link
                        className="btn btn-primary"
                        to={`/checkout/${shelfCurrentLoan.book.bookId}`}
                      >
                        Leave a review
                      </Link>
                    </div>
                  </div>
                </div>
                <hr />
                <LoansModal
                  shelfCurrentLoan={shelfCurrentLoan}
                  mobile={false}
                  returnBook={returnBook}
                  renewLoan={renewLoan}
                />
              </div>
            ))}
          </>
        ) : (
          <>
            <h3 className="mt-3">Currently no loans</h3>
            <Link className="btn btn-primary" to={`search`}>
              Search for a new book
            </Link>
          </>
        )}
      </div>

      {/* Mobile */}
      <div className="container d-lg-none mt-2">
        {shelfCurrentLoans.length > 0 ? (
          <>
            <h5 className="mb-3">Current Loans: </h5>

            {shelfCurrentLoans.map((shelfCurrentLoan) => (
              <div key={shelfCurrentLoan.book.bookId}>
                <div className="d-flex justify-content-center align-items-center">
                  {shelfCurrentLoan.book?.bookImage ? (
                    <img
                      src={shelfCurrentLoan.book?.bookImage}
                      width="226"
                      height="349"
                      alt="Book"
                    />
                  ) : (
                    <img
                      src={require("./../../../Images/BooksImages/book-luv2code-1000.png")}
                      width="226"
                      height="349"
                      alt="Book"
                    />
                  )}
                </div>
                <div className="card d-flex mt-5 mb-3">
                  <div className="card-body container">
                    <div className="mt-3">
                      <h4>Loan Options</h4>
                      {shelfCurrentLoan.daysLeft > 0 && (
                        <p className="text-secondary">
                          Due in {shelfCurrentLoan.daysLeft} days.
                        </p>
                      )}
                      {shelfCurrentLoan.daysLeft === 0 && (
                        <p className="text-success">Due Today.</p>
                      )}
                      {shelfCurrentLoan.daysLeft < 0 && (
                        <p className="text-danger">
                          Past due by {shelfCurrentLoan.daysLeft} days.
                        </p>
                      )}
                      <div className="list-group mt-3">
                        <button
                          className="list-group-item list-group-item-action"
                          aria-current="true"
                          data-bs-toggle="modal"
                          data-bs-target={`#mobilemodal${shelfCurrentLoan.book.bookId}`}
                        >
                          Manage Loan
                        </button>
                        <Link
                          to={"search"}
                          className="list-group-item list-group-item-action"
                        >
                          Search more books?
                        </Link>
                      </div>
                    </div>
                    <hr />
                    <p className="mt-3">
                      Help other find their adventure by reviewing your loan.
                    </p>
                    <Link
                      className="btn btn-primary"
                      to={`/checkout/${shelfCurrentLoan.book.bookId}`}
                    >
                      Leave a review
                    </Link>
                  </div>
                </div>

                <hr />
                <LoansModal
                  shelfCurrentLoan={shelfCurrentLoan}
                  mobile={true}
                  returnBook={returnBook}
                  renewLoan={renewLoan}
                />
              </div>
            ))}
          </>
        ) : (
          <>
            <h3 className="mt-3">Currently no loans</h3>
            <Link className="btn btn-primary" to={`search`}>
              Search for a new book
            </Link>
          </>
        )}
      </div>
    </div>
  );
};
