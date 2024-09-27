package com.fullstack.library.management.backend.entityDto;

import com.fullstack.library.management.backend.SpringBootLibraryManagementApplicationTests;
import com.fullstack.library.management.backend.entity.Role;
import com.fullstack.library.management.backend.entity.User;
import com.fullstack.library.management.backend.mockEntityDtoData.mockData;
import com.fullstack.library.management.backend.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT,classes = SpringBootLibraryManagementApplicationTests.class)
@ActiveProfiles("test")
public class UserTest extends mockData
{
    @Value("${sql.create.table.users}")
    private String createTableUserQuery;

    @Value("${sql.insert.users}")
    private String insertDataUserQuery;

    @Value("${sql.delete.users}")
    private String deleteUserQuery;

    @Value("${sql.drop.users}")
    private String dropUserQuery;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    private void setDatabaseBeforeTransaction()
    {
        jdbcTemplate.execute(createTableUserQuery);
        jdbcTemplate.execute(insertDataUserQuery);
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("User Entity : Positive")
    void UserEntityPositiveTest()
    {
        User userEntity = mockUserData();
        assertEquals(userEntity.getUserId(),1);
        assertEquals(userEntity.getUserName(),"Nidhi");
        assertEquals(userEntity.getEmailAddress(),"nidhi@gmail.com");
        assertEquals(userEntity.getFullName(),"Nidhi Upadhyay");
        assertEquals(userEntity.getContactNumber(),"9313886634");
        Assertions.assertEquals(userEntity.getRole(), Role.USER);
    }

    @Test
    @DisplayName("User Entity : Negative")
    void UserEntityNegativeTest()
    {
        User userEntity = mockUserData();
        assertNotEquals(userEntity.getUserId(),3L);
        assertNotEquals(userEntity.getUserName(),"Shilpi");
        assertNotEquals(userEntity.getEmailAddress(),"shilpi@gmail.com");
        assertNotEquals(userEntity.getFullName(),"Shilpi Upadhyay");
        assertNotEquals(userEntity.getPassword(),passwordEncoder.encode("Shilpi@248"));
        assertNotEquals(userEntity.getContactNumber(),"9313886632");
        assertNotEquals(userEntity.getRole(),Role.ADMIN);
    }

    @AfterEach
    private void setDatabaseAfterTransaction()
    {
        jdbcTemplate.execute(deleteUserQuery);
        jdbcTemplate.execute(dropUserQuery);
    }
}
