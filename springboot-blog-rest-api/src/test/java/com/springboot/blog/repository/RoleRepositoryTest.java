package com.springboot.blog.repository;

import com.springboot.blog.entity.Role;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class RoleRepositoryTest {
    @Autowired
    RoleRepository roleRepository;

    private Role role;

    @BeforeEach
    public void setUp(){
        role = new Role();
        role.setName("ROLE_USER");
        roleRepository.save(role);

    }

    @Test
    public void testSaveRole(){
        assertThat(role.getId()).isNotNull();
        assertThat(role.getName()).isEqualTo("ROLE_USER");
    }

    @Test
    public void testFindByName(){
        Optional<Role> found=roleRepository.findByName("ROLE_USER");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("ROLE_USER");
    }

    @Test
    public void testFindByNameNotFound(){
        Optional<Role> found=roleRepository.findByName("ROLE_ADMIN");
        assertThat(found).isNotPresent();
    }


}
