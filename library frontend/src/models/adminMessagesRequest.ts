class AdminMessagesRequest {
  questionId: number;
  messageResponse: string;

  constructor(questionId: number, messageResponse: string) {
    this.questionId = questionId;
    this.messageResponse = messageResponse;
  }
}

export default AdminMessagesRequest;
