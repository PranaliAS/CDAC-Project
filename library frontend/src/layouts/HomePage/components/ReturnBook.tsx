import { FC } from "react";
import Books from "../../../models/booksEntity";
import { Link } from "react-router-dom";

export const ReturnBook: FC<{ book: Books }> = (props) => {
  return (
    <div className="col-xs-6 col-sm-6 col-md-4 col-lg-3 mb-3">
      <div className="text-center">
        {props.book.bookImage ? (
          <img src={props.book.bookImage} width="151" height="233" alt="book" />
        ) : (
          <img src={props.book.bookImage} width="151" height="233" alt="book" />
        )}
        <h6 className="mt-2">{props.book.bookTitle}</h6>
        <p>{props.book.bookAuthor}</p>
        <></>
        <Link
          className="btn main-color text-white"
          to={`/checkout/${props.book.bookId}`}
        >
          Reserve
        </Link>
      </div>
    </div>
  );
};
