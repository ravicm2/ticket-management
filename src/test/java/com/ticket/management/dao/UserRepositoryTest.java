package com.ticket.management.dao;

import com.ticket.management.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserRepositoryTest {


    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveUser() {
        // Arrange
        UserEntity user = new UserEntity("Ash", "Ravi", "Ash.Ravi@gmail.com", "12345","London", null);

        // Act
        UserEntity savedUser = userRepository.save(user);
        entityManager.flush(); // Flush changes to the database
        entityManager.clear(); // Clear EntityManager to ensure fresh data from the database

        // Assert
        assertNotNull(savedUser.getId());
        assertEquals("Ash", savedUser.getFirstName());
        assertEquals("Ravi", savedUser.getLastName());
        assertEquals("Ash.Ravi@gmail.com", savedUser.getEmail());
    }

    @Test
    public void testFindUserById() {
        // Arrange
        UserEntity user = new UserEntity("Ravi", "Ash", "Ash.Ravi@gmail.com", "12345","London", null);
        UserEntity savedUser = entityManager.persistAndFlush(user);

        // Act
        Optional<UserEntity> optionalUser = userRepository.findById(savedUser.getId());

        // Assert
        assertTrue(optionalUser.isPresent());
        assertEquals("Ravi", optionalUser.get().getFirstName());
        assertEquals("Ash", optionalUser.get().getLastName());
        assertEquals("Ash.Ravi@gmail.com", optionalUser.get().getEmail());
    }

    }
