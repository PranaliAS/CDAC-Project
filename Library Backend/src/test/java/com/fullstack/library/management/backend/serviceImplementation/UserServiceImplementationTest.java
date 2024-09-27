package com.fullstack.library.management.backend.serviceImplementation;

import com.fullstack.library.management.backend.SpringBootLibraryManagementApplicationTests;
import com.fullstack.library.management.backend.entity.User;
import com.fullstack.library.management.backend.repository.UserRepository;
import com.fullstack.library.management.backend.mockEntityDtoData.mockData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,classes = SpringBootLibraryManagementApplicationTests.class)
@ActiveProfiles("test")
public class UserServiceImplementationTest extends mockData
{
    @Value("${sql.create.table.users}")
    private String createTableUsersQuery;

    @Value("${sql.insert.users}")
    private String insertDataUsersQuery;

    @Value("${sql.delete.users}")
    private String deleteUsersQuery;

    @Value("${sql.drop.users}")
    private String dropUsersQuery;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImplementation userServiceImplementation;

    @BeforeEach
    private void setDatabaseBeforeTransaction()
    {
        jdbcTemplate.execute(createTableUsersQuery);
        jdbcTemplate.execute(insertDataUsersQuery);
    }

    @Test
    @DisplayName("Get User By UserName : Positive")
    void getUserByUserNamePositiveTest()
    {
        // context
        when(userRepository.findByUserName("Nidhi")).thenReturn(Optional.of(mockUserData()));
        // event
        Optional<User> optionalUser = userServiceImplementation.findByUserName("Nidhi");
        // outcome
        assertTrue(optionalUser.isPresent());
    }

    @Test
    @DisplayName("Get User By UserName : Negative")
    void getUserByUserNameNegativeTest()
    {
        // context
        when(userRepository.findByUserName("Kirti")).thenReturn(Optional.of(mockUserData()));
        // event
        Optional<User> optionalUser = userServiceImplementation.findByUserName("Kirti");
        // outcome
        assertNotEquals("Kirti",optionalUser.get().getUserName());
    }


    @Test
    @DisplayName("Get all Users : Positive")
    void getAllUsersPositiveTest()
    {
        // context
        when(userRepository.findAll()).thenReturn(mockUserListPositiveData());
        // event
        List<User> savedUserList = userServiceImplementation.getAllUsers();
        // outcome
        assertNotNull(savedUserList);
    }

    @Test
    @DisplayName("Get all Users : Negative")
    void getAllUsersNegativeTest()
    {
        // context
        when(userRepository.findAll()).thenReturn(mockUserListNegativeData());
        // event
        List<User> savedUserList = userServiceImplementation.getAllUsers();
        // outcome
        assertNotEquals(savedUserList.size(),2);
    }

    @AfterEach
    private void setDatabaseAfterTransaction()
    {
        jdbcTemplate.execute(deleteUsersQuery);
        jdbcTemplate.execute(dropUsersQuery);
    }
}
