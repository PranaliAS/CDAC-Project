package com.fullstack.library.management.backend.service;

import com.fullstack.library.management.backend.dto.AdminMessageRequest;
import com.fullstack.library.management.backend.dto.UserMessageRequest;
import com.fullstack.library.management.backend.entity.Messages;
import org.springframework.data.domain.Page;

public interface MessagesService {
    void postMessage(UserMessageRequest messagesRequest, String userEmail);

    Page<Messages> findMessagesByUserPageWise(String userEmail, int page, int size);

    Page<Messages> findMessagesByClosedPageWise(boolean closed, int page, int size);

    void putMessage(AdminMessageRequest adminMessageRequest, String userEmail) throws Exception;
}
