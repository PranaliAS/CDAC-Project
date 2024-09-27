package com.fullstack.library.management.backend.repository;

import com.fullstack.library.management.backend.entity.CheckOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CheckOutRepository extends JpaRepository<CheckOut,Long>
{
    CheckOut findByUserEmailAndBookId(String userEmail, Long bookId);

    List<CheckOut> findBooksByUserEmail(String userEmail);

    @Modifying
    @Query("delete from CheckOut where bookId in :bookId")
    void deleteAllByBookId(@Param("bookId") Long bookId);

}
