package com.fullstack.library.management.backend.service;

import com.fullstack.library.management.backend.entity.Role;
import com.fullstack.library.management.backend.entity.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface UserService
{
    User saveUser(User user);

    Optional<User> findByUserName(String userName);

    List<User> getAllUsers();

    @Transactional
    void changeRole(Role changeRole, String userName);
}
