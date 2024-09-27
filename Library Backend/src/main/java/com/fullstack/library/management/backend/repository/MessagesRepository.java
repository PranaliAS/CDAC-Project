package com.fullstack.library.management.backend.repository;

import com.fullstack.library.management.backend.entity.Messages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface MessagesRepository extends JpaRepository<Messages,Long>
{
    Page<Messages> findByUserEmail(@RequestParam("userEmail") String userEmail,Pageable pageable);
    Page<Messages> findByClosed(@RequestParam("closed") boolean closed,Pageable pageable);
}
