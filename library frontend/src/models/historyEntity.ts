class History {
  userEmail: string;
  checkoutDate: string;
  returnDate: string;
  bookTitle: string;
  bookAuthor: string;
  bookDescription: string;
  bookImage: string;
  historyId: number;

  constructor(
    userEmail: string,
    checkoutDate: string,
    returnDate: string,
    bookTitle: string,
    bookAuthor: string,
    bookDescription: string,
    bookImage: string,
    historyId: number
  ) {
    this.userEmail = userEmail;
    this.checkoutDate = checkoutDate;
    this.returnDate = returnDate;
    this.bookTitle = bookTitle;
    this.bookAuthor = bookAuthor;
    this.bookDescription = bookDescription;
    this.bookImage = bookImage;
    this.historyId = historyId;
  }
}

export default History;
