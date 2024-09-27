class Messages {
  title: string;
  question: string;
  userEmail?: string;
  adminEmail?: string;
  messageResponse?: string;
  closed?: boolean;
  messagesId?: number;

  constructor(
    title: string,
    question: string,
    userEmail: string,
    adminEmail: string,
    messageResponse: string,
    closed: boolean,
    messagesId: number
  ) {
    this.title = title;
    this.question = question;
    this.userEmail = userEmail;
    this.adminEmail = adminEmail;
    this.messageResponse = messageResponse;
    this.closed = closed;
    this.messagesId = messagesId;
  }
}

export default Messages;
