package com.springboot.blog.repository;

import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    private User user;
    private Role role;

    @BeforeEach
    public void setUp(){
        role = new Role();
        role.setName("ROLE_USER");
        roleRepository.save(role);

        user = new User();
        user.setName("John");
        user.setUsername("JohnDoe");
        user.setEmail("john@gmail.com");
        user.setPassword("123");
        user.setRoles(Set.of(role));
        userRepository.save(user);
    }

    @Test
    public void testSaveUser(){
        assertThat(user.getId()).isNotNull();
        assertThat(user.getName()).isEqualTo("John");
        assertThat(user.getRoles()).isNotEmpty();
    }

    @Test
    public void testFindByUsername(){
        Optional<User> found=userRepository.findByUsername(user.getUsername());
        assertThat(found).isPresent();
        assertThat(user.getUsername()).isEqualTo("JohnDoe");
    }

    @Test
    public void testFindByEmail() {
        Optional<User> foundUser = userRepository.findByEmail("john@gmail.com");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("john@gmail.com");
    }
    @Test
    public void testFindByUsernameOrEmail() {
        Optional<User> foundUser = userRepository.findByUsernameOrEmail("JohnDoe", "john@gmail.com");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("JohnDoe");
    }

    @Test
    public void testExistsByUsername() {
        Boolean exists = userRepository.existsByUsername("JohnDoe");
        assertThat(exists).isTrue();
    }

    @Test
    public void testExistsByEmail() {
        Boolean exists = userRepository.existsByEmail("john@gmail.com");
        assertThat(exists).isTrue();
    }
}
