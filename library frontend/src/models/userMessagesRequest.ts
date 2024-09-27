class UserMessagesRequest {
  title: string;
  question: string;

  constructor(title: string, question: string) {
    this.title = title;
    this.question = question;
  }
}

export default UserMessagesRequest;
