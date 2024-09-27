import { useEffect, useState } from "react";
import Books from "../../../models/booksEntity";
import { ADMIN_BASE_URL } from "../../../commons/constants";
import { AuthHeader } from "../../../services/base.service";

export const ChangeQuantityOfBook: React.FC<{
  book: Books;
  deleteBook: any;
}> = (props, key) => {
  const [quantity, setQuantity] = useState<number>(0);
  const [remaining, setRemaining] = useState<number>(0);

  useEffect(() => {
    const fetchBookInState = () => {
      props.book.copies ? setQuantity(props.book.copies) : setQuantity(0);
      props.book.copiesAvailable
        ? setRemaining(props.book.copiesAvailable)
        : setRemaining(0);
    };
    fetchBookInState();
  }, [props.book.copies, props.book.copiesAvailable]);

  async function increaseQuantity() {
    const url =
      ADMIN_BASE_URL + `/increase/book/quantity/?bookId=${props.book?.bookId}`;
    const requestOptions = {
      method: "PUT",
      headers: AuthHeader(),
    };

    const quantityUpdateResponse = await fetch(url, requestOptions);
    if (!quantityUpdateResponse.ok) {
      throw new Error("Something went wrong!");
    }
    setQuantity(quantity + 1);
    setRemaining(remaining + 1);
  }

  async function decreaseQuantity() {
    const url =
      ADMIN_BASE_URL + `/decrease/book/quantity/?bookId=${props.book?.bookId}`;
    const requestOptions = {
      method: "PUT",
      headers: AuthHeader(),
    };

    const quantityUpdateResponse = await fetch(url, requestOptions);
    if (!quantityUpdateResponse.ok) {
      throw new Error("Something went wrong!");
    }
    setQuantity(quantity - 1);
    setRemaining(remaining - 1);
  }

  async function deleteBook() {
    const url = ADMIN_BASE_URL + `/delete/book/?bookId=${props.book?.bookId}`;
    const requestOptions = {
      method: "DELETE",
      headers: AuthHeader(),
    };

    const updateResponse = await fetch(url, requestOptions);
    if (!updateResponse.ok) {
      throw new Error("Something went wrong!");
    }
    props.deleteBook();
  }

  return (
    <div className="card mt-3 shadow p-3 mb-3 bg-body rounded">
      <div className="row g-0">
        <div className="col-md-2">
          <div className="d-none d-lg-block">
            {props.book.bookImage ? (
              <img
                src={props.book.bookImage}
                width="123"
                height="196"
                alt="Book"
              />
            ) : (
              <img
                src={require("./../../../Images/BooksImages/book-luv2code-1000.png")}
                width="123"
                height="196"
                alt="Book"
              />
            )}
          </div>
          <div className="d-lg-none d-flex justify-content-center align-items-center">
            {props.book.bookImage ? (
              <img
                src={props.book.bookImage}
                width="123"
                height="196"
                alt="Book"
              />
            ) : (
              <img
                src={require("./../../../Images/BooksImages/book-luv2code-1000.png")}
                width="123"
                height="196"
                alt="Book"
              />
            )}
          </div>
        </div>
        <div className="col-md-6">
          <div className="card-body">
            <h5 className="card-title">{props.book.bookAuthor}</h5>
            <h4>{props.book.bookTitle}</h4>
            <p className="card-text"> {props.book.bookDescription} </p>
          </div>
        </div>
        <div className="mt-3 col-md-4">
          <div className="d-flex justify-content-center algin-items-center">
            <p>
              Total Quantity: <b>{quantity}</b>
            </p>
          </div>
          <div className="d-flex justify-content-center align-items-center">
            <p>
              Books Remaining: <b>{remaining}</b>
            </p>
          </div>
        </div>
        <div className="mt-3 col-md-1">
          <div className="d-flex justify-content-start">
            <button className="m-1 btn btn-md btn-danger" onClick={deleteBook}>
              Delete
            </button>
          </div>
        </div>
        <button
          className="m1 btn btn-md main-color text-white"
          onClick={increaseQuantity}
        >
          Add Quantity
        </button>
        <button
          className="m1 btn btn-md btn-warning"
          onClick={decreaseQuantity}
        >
          Decrease Quantity
        </button>
      </div>
    </div>
  );
};
