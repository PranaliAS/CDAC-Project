package com.fullstack.library.management.backend.serviceImplementation;

import com.fullstack.library.management.backend.entity.Role;
import com.fullstack.library.management.backend.repository.UserRepository;
import com.fullstack.library.management.backend.service.UserService;
import com.fullstack.library.management.backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user)
    {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        return  userRepository.save(user);
    }

    @Override
    public Optional<User> findByUserName(String userName)
    {
        return userRepository.findByUserName(userName);
    }

    @Override
    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void changeRole(Role changeRole, String userName)
    {
        userRepository.updateUserRole(userName,changeRole);
    }
}
