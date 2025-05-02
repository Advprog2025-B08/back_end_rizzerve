package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.repository;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.model.Meja;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MejaRepositoryTest {

    private MejaRepository mejaRepository;

    @BeforeEach
    void setUp() {
        mejaRepository = new MejaRepository();
    }

    @Test
    void testCreateMeja() {
        Meja meja = new Meja();
        meja.setNomor(1);
        mejaRepository.createMeja(meja.getNomor());
        assertEquals(1, mejaRepository.getAllMejas().size());
    }

    @Test
    void testGetMejaByNomor() {
        mejaRepository.createMeja(10);
        Meja meja = mejaRepository.getMejaByNomor(10);
        assertNotNull(meja);
        assertEquals(10, meja.getNomor());
    }

    @Test
    void testUpdateMeja() {
        mejaRepository.createMeja(5);
        mejaRepository.updateMeja(5, 7);
        Meja updatedMeja = mejaRepository.getMejaByNomor(7);
        assertNotNull(updatedMeja);
        assertEquals(7, updatedMeja.getNomor());
    }

    @Test
    void testDeleteMeja() {
        mejaRepository.createMeja(4);
        Meja meja = mejaRepository.getMejaByNomor(4);
        mejaRepository.deleteMeja(meja.getNomor());
        assertNull(mejaRepository.getMejaByNomor(4));
    }

    @Test
    void testCheckUniqueTrue() {
        assertTrue(mejaRepository.checkUnique(100));
    }

    @Test
    void testCheckUniqueFalse() {
        mejaRepository.createMeja(3);
        assertFalse(mejaRepository.checkUnique(3));
    }

    @Test
    void testSetAndDeleteUser() {
        mejaRepository.createMeja(12);
        Meja meja = mejaRepository.getMejaByNomor(12);

        User dummyUser = new User(1, "dummy@mail.com", "customer");
        mejaRepository.setUser(meja.getNomor(), dummyUser.getId());

        assertNotNull(mejaRepository.getMejaByNomor(12).getUser());

        mejaRepository.delUser(12);
        assertNull(mejaRepository.getMejaByNomor(12).getUser());
    }
}
