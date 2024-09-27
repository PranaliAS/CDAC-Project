package com.fullstack.library.management.backend.repository;

import com.fullstack.library.management.backend.entity.Role;
import com.fullstack.library.management.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long>
{
    Optional<User> findByUserName(String userName);

    @Modifying
    @Query("update User set role = :role where userName = :userName")
    void updateUserRole(@Param("userName") String userName, @Param("role") Role role);
}
