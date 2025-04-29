package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.controller;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.model.Meja;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.service.MejaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MejaControllerTest {

    private MejaService mejaService;
    private MejaController mejaController;

    @BeforeEach
    void setUp() {
        mejaService = Mockito.mock(MejaService.class);
        mejaController = new MejaController(mejaService);
    }

    @Test
    void testCreateMeja() {
        Meja meja = new Meja();
        meja.setNomor(1);
        doReturn(meja).when(mejaService).createMeja(1);

        ResponseEntity<?> response = mejaController.createMeja(meja);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().toString().contains("Meja created successfully"));
    }

    @Test
    void testUpdateMeja() {
        Meja meja = new Meja();
        meja.setNomor(1);

        Optional<Meja> optionalMeja = Optional.of(meja);
        doReturn(optionalMeja).when(mejaService).updateMeja(1, 2);

        ResponseEntity<?> response = mejaController.updateMeja(1, meja);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().toString().contains("Meja updated successfully"));
    }

    @Test
    void testDeleteMeja() {
        doReturn(true).when(mejaService).deleteMeja(2);

        ResponseEntity<?> response = mejaController.deleteMeja(2);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void testGetAllMeja() {
        Meja meja1 = new Meja();
        meja1.setNomor(1);
        Meja meja2 = new Meja();
        meja2.setNomor(2);
        List<Meja> mejaList = Arrays.asList(meja1, meja2);
        doReturn(mejaList).when(mejaService).getAllMeja();

        ResponseEntity<?> response = mejaController.getAllMeja();
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void testgetMejaByNomor() {
        Meja meja = new Meja();
        meja.setNomor(1);

        Optional<Meja> optionalMeja = Optional.of(meja);
        doReturn(optionalMeja).when(mejaService).getMejaByNomor(1);

        ResponseEntity<?> response = mejaController.getMejaByNomor(1);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void testSetUserToMeja() {
        String successMessage = "User successfully connected to meja 1!";
        doReturn(successMessage).when(mejaService).setUserToMeja(1, 123);

        ResponseEntity<?> response = mejaController.setUserToMeja("1", "123");
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void testRemoveUserFromMeja() {
        String successMessage = "User successfully checked out of meja 1!";
        doReturn(successMessage).when(mejaService).removeUserFromMeja(123);

        ResponseEntity<?> response = mejaController.removeUserFromMeja("123");
        assertEquals(200, response.getStatusCode().value());
    }
}