package com.gambler99.ebay_clone.repository;

import com.gambler99.ebay_clone.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;

//dung data example cua thang Hung

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test // test find by username
    void testFindByUsername() {
        Optional<User> user = userRepository.findByUsername("seller1");
        assertThat(user).isPresent();
        assertThat(user.get().getEmail()).isEqualTo("seller@example.com");
    }

    @Test // test find by email
    void testFindByEmail() {
        Optional<User> user = userRepository.findByEmail("seller@example.com");
        assertThat(user).isPresent();
        assertThat(user.get().getUsername()).isEqualTo("seller1");
    }

    @Test // test if username exists
    void testExistsByUsername() {
        boolean exists = userRepository.existsByUsername("seller1");
        assertThat(exists).isTrue();
    }

    @Test // test if email exists
    void testExistsByEmail() {
        boolean exists = userRepository.existsByEmail("seller@example.com");
        assertThat(exists).isTrue();
    }

    @Test // test non-existing user
    void testNonExistingUsername() {
        Optional<User> user = userRepository.findByUsername("ghost123");
        assertThat(user).isNotPresent();
    }
}
