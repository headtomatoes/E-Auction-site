package com.gambler99.ebay_clone.repository;

import com.gambler99.ebay_clone.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

//dung data example cua thang Hung

@SpringBootTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test // find existing role
    void testFindByRoleName() {
        Optional<Role> role = roleRepository.findByRoleName("SELLER");
        assertThat(role).isPresent();
        assertThat(role.get().getRoleName()).isEqualTo("SELLER");
    }

    @Test // exists check
    void testExistsByRoleName() {
        boolean exists = roleRepository.existsByRoleName("BUYER");
        assertThat(exists).isTrue();
    }

    @Test
        // it tries to find a role that doesn’t exist in your database ("GHOST").
        // Then it checks that Optional<Role> is empty — meaning the role isn’t found.
    void testNonExistingRoleName() {
        Optional<Role> role = roleRepository.findByRoleName("GHOST");
        assertThat(role).isNotPresent();
    }
}
