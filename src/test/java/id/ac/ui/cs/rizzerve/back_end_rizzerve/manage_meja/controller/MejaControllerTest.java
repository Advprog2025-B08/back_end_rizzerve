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
        when(mejaService.createMeja(1)).thenReturn(meja);

        ResponseEntity<?> response = mejaController.createMeja(meja);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Meja created successfully"));
    }

    @Test
    void testUpdateMeja() {
        Meja meja = new Meja();
        meja.setNomor(1);
        when(mejaService.updateMeja(1, 2)).thenReturn(Optional.of(meja));

        ResponseEntity<?> response = mejaController.updateMeja(1, meja);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Meja updated successfully"));
    }

    @Test
    void testDeleteMeja() {
        when(mejaService.deleteMeja(2)).thenReturn(true);

        ResponseEntity<?> response = mejaController.deleteMeja(2);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllMeja() {
        Meja meja = new Meja();
        meja.setNomor(1);
        Meja meja2 = new Meja();
        meja.setNomor(2);
        List<Meja> mejaList = Arrays.asList(meja, meja2);
        when(mejaService.getAllMeja()).thenReturn(mejaList);

        ResponseEntity<?> response = mejaController.getAllMeja();
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetMejaById() {
        Meja meja = new Meja();
        meja.setNomor(1);
        when(mejaService.getMejaById(1)).thenReturn(Optional.of(meja));

        ResponseEntity<?> response = mejaController.getMejaById(1);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testSetUserToMeja() {
        when(mejaService.setUserToMeja("123")).thenReturn("User successfully connected to meja 1!");

        ResponseEntity<?> response = mejaController.setUserToMeja("123");
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testRemoveUserFromMeja() {
        when(mejaService.removeUserFromMeja("123")).thenReturn("User successfully checked out of meja 1!");

        ResponseEntity<?> response = mejaController.removeUserFromMeja("123");
        assertEquals(200, response.getStatusCodeValue());
    }
}
