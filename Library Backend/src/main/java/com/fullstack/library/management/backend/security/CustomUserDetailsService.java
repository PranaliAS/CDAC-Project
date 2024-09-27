package com.fullstack.library.management.backend.security;

import com.fullstack.library.management.backend.entity.User;
import com.fullstack.library.management.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.fullstack.library.management.backend.utils.SecurityUtils;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService
{
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = userService.findByUserName(username).
                orElseThrow(() -> new UsernameNotFoundException("User name not found with User Name : " + username));
        Set<GrantedAuthority> userAuthorities = Set.of(SecurityUtils.convertToAuthority(user.getRole().name()));

        // User Details
        return UserPrinciple.builder()
                .user(user)
                .userId(user.getUserId())
                .userName(user.getUserName())
                .password(user.getPassword())
                .authorities(userAuthorities)
                .build();
    }
}
