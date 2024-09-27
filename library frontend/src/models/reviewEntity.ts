class Reviews {
  userEmail: string;
  date: string;
  rating: number;
  bookId: number;
  reviewDescription?: string;
  reviewId: number;
  constructor(
    userEmail: string,
    date: string,
    rating: number,
    bookId: number,
    reviewDescription: string,
    reviewId: number
  ) {
    this.userEmail = userEmail;
    this.date = date;
    this.rating = rating;
    this.bookId = bookId;
    this.reviewDescription = reviewDescription;
    this.reviewId = reviewId;
  }
}

export default Reviews;
