package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.repository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUsername_whenUserExists_thenReturnUser() {
        User testUser = createValidUser();
        
        User savedUser = entityManager.persistAndFlush(testUser);

        Optional<User> foundUser = userRepository.findByUsername("testuser");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    public void testFindByUsername_whenUserDoesNotExist_thenReturnEmpty() {
        User testUser = createValidUser();
        entityManager.persistAndFlush(testUser);
        
        Optional<User> notFoundUser = userRepository.findByUsername("nonexistentuser");

        assertThat(notFoundUser).isEmpty();
    }
    
    private User createValidUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setRole("USER");
        
        return user;
    }
}