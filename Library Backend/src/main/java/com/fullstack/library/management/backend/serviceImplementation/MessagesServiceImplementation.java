package com.fullstack.library.management.backend.serviceImplementation;

import com.fullstack.library.management.backend.dto.AdminMessageRequest;
import com.fullstack.library.management.backend.dto.UserMessageRequest;
import com.fullstack.library.management.backend.entity.Messages;
import com.fullstack.library.management.backend.repository.MessagesRepository;
import com.fullstack.library.management.backend.service.MessagesService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessagesServiceImplementation implements MessagesService
{
    @Autowired
    private MessagesRepository messagesRepository;

    @Override
    public void postMessage(UserMessageRequest messagesRequest, String userEmail)
    {
        Messages messages = new Messages();
        BeanUtils.copyProperties(messagesRequest,messages);
        messages.setUserEmail(userEmail);
        messagesRepository.save(messages);
    }

    @Override
    public Page<Messages> findMessagesByUserPageWise(String userEmail, int page, int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return messagesRepository.findByUserEmail(userEmail,pageable);
    }

    @Override
    public Page<Messages> findMessagesByClosedPageWise(boolean closed, int page, int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return messagesRepository.findByClosed(closed,pageable);
    }

    @Override
    public void putMessage(AdminMessageRequest adminMessageRequest, String userEmail) throws Exception
    {
        Optional<Messages> messages = messagesRepository.findById(adminMessageRequest.getQuestionId());

        if(!messages.isPresent())
        {
            throw new Exception("Message not found");
        }

        messages.get().setAdminEmail(userEmail);
        messages.get().setMessageResponse(adminMessageRequest.getMessageResponse());
        messages.get().setClosed(true);
        messagesRepository.save(messages.get());
    }


}
