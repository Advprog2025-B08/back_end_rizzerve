package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.repository;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.model.Meja;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class MejaRepositoryTest {

    @Autowired
    private MejaRepository mejaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testCreateMeja() {
        // Create a meja
        Meja meja = new Meja();
        meja.setId(UUID.randomUUID().toString());
        meja.setNomor(1);

        // Save it via entity manager
        entityManager.persist(meja);
        entityManager.flush();

        // Test fetching it via repository
        List<Meja> mejas = mejaRepository.getAllMejas();
        assertTrue(mejas.size() >= 1);
        assertTrue(mejas.stream().anyMatch(m -> m.getNomor() == 1));
    }

    @Test
    void testGetMejaByNomor() {
        // Create a meja
        Meja meja = new Meja();
        meja.setId(UUID.randomUUID().toString());
        meja.setNomor(10);

        // Save via entity manager
        entityManager.persist(meja);
        entityManager.flush();

        // Test repository method
        Meja foundMeja = mejaRepository.getMejaByNomor(10);
        assertNotNull(foundMeja);
        assertEquals(10, foundMeja.getNomor());
    }

    @Test
    void testUpdateMeja() {
        // Create a meja with nomor 5
        Meja meja = new Meja();
        meja.setId(UUID.randomUUID().toString());
        meja.setNomor(5);
        entityManager.persist(meja);
        entityManager.flush();

        // We can't directly test the repository updateMeja method since it requires service layer logic
        // Instead, we'll test that we can find, modify, and save a meja
        Meja retrievedMeja = mejaRepository.getMejaByNomor(5);
        retrievedMeja.setNomor(7);
        mejaRepository.save(retrievedMeja);

        // Verify the update worked
        assertNull(mejaRepository.getMejaByNomor(5)); // Old nomor should not exist
        Meja updatedMeja = mejaRepository.getMejaByNomor(7);
        assertNotNull(updatedMeja);
        assertEquals(7, updatedMeja.getNomor());
    }

    @Test
    void testDeleteMeja() {
        // Create a meja
        Meja meja = new Meja();
        meja.setId(UUID.randomUUID().toString());
        meja.setNomor(4);
        entityManager.persist(meja);
        entityManager.flush();

        // Verify it exists
        assertNotNull(mejaRepository.getMejaByNomor(4));

        // Delete it
        Meja foundMeja = mejaRepository.getMejaByNomor(4);
        mejaRepository.delete(foundMeja);

        // Verify it's gone
        assertNull(mejaRepository.getMejaByNomor(4));
    }

    @Test
    void testCheckUniqueTrue() {
        // No meja with nomor 100 should exist initially
        assertTrue(mejaRepository.checkUnique(100));
    }

    @Test
    void testCheckUniqueFalse() {
        // Create a meja with nomor 3
        Meja meja = new Meja();
        meja.setId(UUID.randomUUID().toString());
        meja.setNomor(3);
        entityManager.persist(meja);
        entityManager.flush();

        // Check that nomor 3 is not unique
        assertFalse(mejaRepository.checkUnique(3));
    }

    @Test
    void testSetAndDeleteUser() {
        // Create a meja
        Meja meja = new Meja();
        meja.setId(UUID.randomUUID().toString());
        meja.setNomor(12);
        entityManager.persist(meja);

        // Create a user
        User dummyUser = new User();
        dummyUser.setUsername("dummy");
        dummyUser.setPassword("password123");
        dummyUser.setRole("USER");
        entityManager.persist(dummyUser);
        entityManager.flush();

        // Get the persisted objects
        Meja persistedMeja = mejaRepository.getMejaByNomor(12);
        User persistedUser = userRepository.findByUsername("dummy").orElse(null);

        // Test setting user
        persistedMeja.setUser(persistedUser);
        mejaRepository.save(persistedMeja);

        // Verify the user was set
        Meja mejaWithUser = mejaRepository.getMejaByNomor(12);
        assertNotNull(mejaWithUser.getUser());
        assertEquals("dummy", mejaWithUser.getUser().getUsername());

        // Test removing user
        mejaWithUser.setUser(null);
        mejaRepository.save(mejaWithUser);

        // Verify the user was removed
        assertNull(mejaRepository.getMejaByNomor(12).getUser());
    }
}