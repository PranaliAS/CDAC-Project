import { Link } from "react-router-dom";
import Books from "../../models/booksEntity";
import { LeaveAReview } from "../Utils/LeaveAReview";
import authenticationService from "../../services/authentication.service";

export const CheckoutAndReviewBox: React.FC<{
  book: Books | undefined;
  mobile: boolean;
  currentLoansCount: number;
  isBookCheckedOut: boolean;
  checkOutBook: any;
  isUserReviewLeft: boolean;
  submitBookReview: any;
}> = (props) => {
  const currentSignInUser = authenticationService.getCurrentUser();

  function buttonRender() {
    if (currentSignInUser && currentSignInUser?.userId) {
      if (!props.isBookCheckedOut && props.currentLoansCount < 5) {
        return (
          <button
            onClick={() => props.checkOutBook()}
            className="btn btn-success btn-lg"
          >
            CheckOut
          </button>
        );
      } else if (props.isBookCheckedOut) {
        return (
          <p>
            <b>Book Checked Out. Enjoy!</b>
          </p>
        );
      } else if (!props.isBookCheckedOut) {
        return (
          <p className="text-danger">
            <b>Too Many Books Checked Out.</b>
          </p>
        );
      } else {
        return (
          <Link to={"/login"} className="btn btn-success btn-lg">
            Sign In
          </Link>
        );
      }
    }
  }

  function reviewRender() {
    if (
      currentSignInUser &&
      currentSignInUser?.userId &&
      !props.isUserReviewLeft
    ) {
      return (
        <p>
          <LeaveAReview submitReview={props.submitBookReview} />
        </p>
      );
    } else if (
      currentSignInUser &&
      currentSignInUser?.userId &&
      props.isUserReviewLeft
    ) {
      return (
        <p>
          <b>Thank you for your review</b>
        </p>
      );
    } else {
      return (
        <div>
          <hr />
          <p>Sign In to be able to leave a review.</p>
        </div>
      );
    }
  }

  return (
    <div
      className={
        props.mobile ? "card d-flex mt-5" : "card col-3 container d-flex mb-5"
      }
    >
      <div className="card-body container">
        <div className="mt-3">
          <p>
            <b>{props.currentLoansCount}/5</b>
            Books Checked Out
          </p>
          <hr />
          {props.book &&
          props.book.copiesAvailable &&
          props.book.copiesAvailable > 0 ? (
            <h4 className="text-success">Available</h4>
          ) : (
            <h4 className="text-danger">Wait List</h4>
          )}
          <div className="row">
            <p className="col-6 lead">
              <b>{props.book?.copies} </b>
              copies
            </p>
            <p className="col-6 lead">
              <b>{props.book?.copiesAvailable} </b>
              available
            </p>
          </div>
        </div>
        {buttonRender()}
        <hr />
        <p className="mt-3">
          This number can change until placing order has been complete.
        </p>
        {reviewRender()}
      </div>
    </div>
  );
};
