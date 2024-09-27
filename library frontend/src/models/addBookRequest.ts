class AddBookRequest {
  bookTitle: string;
  bookAuthor: string;
  bookDescription: string;
  copies: number;
  bookCategory: string;
  bookImage?: string;

  constructor(
    bookTitle: string,
    bookAuthor: string,
    bookDescription: string,
    copies: number,
    bookCategory: string
  ) {
    this.bookTitle = bookTitle;
    this.bookAuthor = bookAuthor;
    this.bookDescription = bookDescription;
    this.copies = copies;
    this.bookCategory = bookCategory;
  }
}

export default AddBookRequest;
