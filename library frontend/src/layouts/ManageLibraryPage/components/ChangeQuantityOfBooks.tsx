import { useState, useEffect } from "react";
import Books from "../../../models/booksEntity";
import { Pagination } from "../../Utils/Pagination";
import { SpinnerLoading } from "../../Utils/SpinnerLoading";
import { ChangeQuantityOfBook } from "./ChangeQuantityOfBook";
import { BOOK_BASE_URL } from "../../../commons/constants";

export const ChangeQuantityOfBooks = () => {
  const [books, setBooks] = useState<Books[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [httpError, setHttpError] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [booksPerPage] = useState(5);
  const [totalAmountOfBooks, setTotalAmountOfBooks] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [bookDelete, setBookDelete] = useState(false);

  useEffect(() => {
    const fetchBooks = async () => {
      let fetchBooksPageWiseUrl: string =
        BOOK_BASE_URL +
        `/service/all/pageWise?page=${currentPage - 1}&size=${booksPerPage}`;

      const response = await fetch(fetchBooksPageWiseUrl);

      if (!response.ok) {
        throw new Error("Something went wrong");
      }

      const responseJson = await response.json();

      const responseBooksData = responseJson.content;

      setTotalAmountOfBooks(responseJson.totalElements);
      setTotalPages(responseJson.totalPages);

      const loadedBooks: Books[] = [];

      for (const key in responseBooksData) {
        loadedBooks.push({
          bookId: responseBooksData[key].bookId,
          bookTitle: responseBooksData[key].bookTitle,
          bookAuthor: responseBooksData[key].bookAuthor,
          bookDescription: responseBooksData[key].bookDescription,
          copies: responseBooksData[key].copies,
          copiesAvailable: responseBooksData[key].copiesAvailable,
          bookCategory: responseBooksData[key].bookCategory,
          bookImage: responseBooksData[key].bookImage,
        });
      }

      setBooks(loadedBooks);
      setIsLoading(false);
    };
    fetchBooks().catch((error: any) => {
      setIsLoading(false);
      setHttpError(error.message);
    });
  }, [booksPerPage, currentPage, bookDelete]);

  const deleteBook = () => setBookDelete(!bookDelete);

  if (isLoading) {
    return <SpinnerLoading />;
  }

  if (httpError) {
    return (
      <div className="container m-5">
        <p>{httpError}</p>
      </div>
    );
  }

  const indexOfLastBook: number = currentPage * booksPerPage;
  const indexOfFirstBook: number = indexOfLastBook - booksPerPage;
  let lastItem =
    booksPerPage * currentPage <= totalAmountOfBooks
      ? booksPerPage * currentPage
      : totalAmountOfBooks;
  const paginate = (pageNumber: number) => setCurrentPage(pageNumber);

  return (
    <div className="container mt-5">
      {totalAmountOfBooks > 0 ? (
        <>
          <div className="mt-3">
            <h3>Number of results: ({totalAmountOfBooks})</h3>
          </div>
          <p>
            {indexOfFirstBook + 1} to {lastItem} of {totalAmountOfBooks} items:
          </p>
          {books.map((book) => (
            <ChangeQuantityOfBook
              book={book}
              key={book.bookId}
              deleteBook={deleteBook}
            />
          ))}
        </>
      ) : (
        <h5>Add a book before changing quantity</h5>
      )}
      {totalPages > 1 && (
        <Pagination
          currentPage={currentPage}
          totalPages={totalPages}
          paginate={paginate}
        />
      )}
    </div>
  );
};
