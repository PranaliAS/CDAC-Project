package com.fullstack.library.management.backend.serviceImplementation;

import com.fullstack.library.management.backend.entity.History;
import com.fullstack.library.management.backend.service.HistoryService;
import com.fullstack.library.management.backend.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryServiceImplementation implements HistoryService
{
    @Autowired
    private HistoryRepository historyRepository;

    @Override
    public Page<History> getBookCheckOutHistoryForUserPageWise(String userEmail, int page, int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return historyRepository.findBooksByUserEmail(userEmail,pageable);
    }

    @Override
    public List<History> getAllHistory()
    {
        return historyRepository.findAll();
    }
}
