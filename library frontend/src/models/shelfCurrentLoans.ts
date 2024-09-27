import Books from "./booksEntity";

class ShelfCurrentLoans {
  book: Books;
  daysLeft: number;

  constructor(book: Books, daysLeft: number) {
    this.book = book;
    this.daysLeft = daysLeft;
  }
}

export default ShelfCurrentLoans;
