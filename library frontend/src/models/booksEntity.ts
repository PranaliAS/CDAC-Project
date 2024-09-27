class Books {
  bookTitle: string;
  bookAuthor?: string;
  bookDescription?: string;
  copies?: number;
  copiesAvailable?: number;
  bookCategory?: string;
  bookImage?: string;
  bookId: number;

  constructor(
    bookTitle: string,
    bookAuthor: string,
    bookDescription: string,
    copies: number,
    copiesAvailable: number,
    bookCategory: string,
    bookImage: string,
    bookId: number
  ) {
    this.bookTitle = bookTitle;
    this.bookAuthor = bookAuthor;
    this.bookDescription = bookDescription;
    this.copies = copies;
    this.copiesAvailable = copiesAvailable;
    this.bookCategory = bookCategory;
    this.bookImage = bookImage;
    this.bookId = bookId;
  }
}

export default Books;
