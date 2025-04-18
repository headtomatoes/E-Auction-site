package com.gambler99.ebay_clone.repository;

import com.gambler99.ebay_clone.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;




import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @Test
    void testFindByRoleName() {
        Role role = new Role();
        role.setRoleName("ADMIN");

        when(roleRepository.findByRoleName("ADMIN")).thenReturn(Optional.of(role));

        Optional<Role> foundRole = roleRepository.findByRoleName("ADMIN");

        assertTrue(foundRole.isPresent());
        assertEquals("ADMIN", foundRole.get().getRoleName());
    }

    @Test
    void testExistsByRoleName() {
        when(roleRepository.existsByRoleName("ADMIN")).thenReturn(true);

        boolean exists = roleRepository.existsByRoleName("ADMIN");

        assertTrue(exists);
    }
}
