package com.fullstack.library.management.backend.entityDto;

import com.fullstack.library.management.backend.SpringBootLibraryManagementApplicationTests;
import com.fullstack.library.management.backend.entity.JwtRefreshToken;
import com.fullstack.library.management.backend.mockEntityDtoData.mockData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,classes = SpringBootLibraryManagementApplicationTests.class)
@ActiveProfiles("test")
public class JwtRefreshTokenTest extends mockData
{
    @Value("${sql.create.table.jwtrefreshtoken}")
    private String createTableJwtRefreshTokenQuery;

    @Value("${sql.insert.jwtrefreshtoken}")
    private String insertDataJwtRefreshTokenQuery;

    @Value("${sql.delete.jwtrefreshtoken}")
    private String deleteJwtRefreshTokenQuery;

    @Value("${sql.drop.jwtrefreshtoken}")
    private String dropJwtRefreshTokenQuery;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    private void setDatabaseBeforeTransaction()
    {
        jdbcTemplate.execute(createTableJwtRefreshTokenQuery);
        jdbcTemplate.execute(insertDataJwtRefreshTokenQuery);
    }
    @Test
    @DisplayName("JwtRefreshToken Entity : Positive")
    void JwtRefreshTokenEntityPositiveTest()
    {
        JwtRefreshToken jwtRefreshTokenEntity = mockJwtRefreshTokenData();
        assertEquals(jwtRefreshTokenEntity.getTokenId(),"1");
        assertEquals(jwtRefreshTokenEntity.getUserId(),1L);
        assertEquals(jwtRefreshTokenEntity.getTokenCreationDate(), LocalDateTime.now());
        assertEquals(jwtRefreshTokenEntity.getTokenExpirationDate(),LocalDateTime.now());
    }

    @Test
    @DisplayName("JwtRefreshToken Entity : Negative")
    void JwtRefreshTokenEntityNegativeTest()
    {
        JwtRefreshToken jwtRefreshTokenEntity = mockJwtRefreshTokenData();
        assertNotEquals(jwtRefreshTokenEntity.getTokenId(),"2");
        assertNotEquals(jwtRefreshTokenEntity.getUserId(),2L);
        assertNotEquals(jwtRefreshTokenEntity.getTokenCreationDate(),LocalDateTime.now().plusDays(9));
        assertNotEquals(jwtRefreshTokenEntity.getTokenExpirationDate(),LocalDateTime.now().plusDays(9));
    }

    @AfterEach
    private void setDatabaseAfterTransaction()
    {
        jdbcTemplate.execute(deleteJwtRefreshTokenQuery);
        jdbcTemplate.execute(dropJwtRefreshTokenQuery);
    }
}