package com.fullstack.library.management.backend.service;

import com.fullstack.library.management.backend.entity.History;
import org.springframework.data.domain.Page;

import java.util.List;

public interface HistoryService {
    Page<History> getBookCheckOutHistoryForUserPageWise(String userEmail, int page, int size);

    List<History> getAllHistory();
}
