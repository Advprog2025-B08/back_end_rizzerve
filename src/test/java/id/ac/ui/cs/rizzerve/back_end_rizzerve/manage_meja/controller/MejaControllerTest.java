package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.controller;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.model.Meja;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.dto.*;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.dto.UsernameDTO;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.service.MejaService;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MejaControllerTest {

    private MejaService mejaService;
    private MejaSSEController sseController;
    private MejaController mejaController;

    @BeforeEach
    void setUp() {
        mejaService = Mockito.mock(MejaService.class);
        sseController = Mockito.mock(MejaSSEController.class);
        mejaController = new MejaController(mejaService, sseController);

        User dummyUser = new User();
        dummyUser.setUsername("testuser");
        dummyUser.setPassword("password123");
        dummyUser.setRole("USER");
    }

    @Test
    void testCreateMeja() {
        Meja meja = new Meja();
        meja.setNomor(1);
        meja.setId(1L);

        doReturn(meja).when(mejaService).createMeja(1);

        ResponseEntity<?> response = mejaController.createMeja(meja);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().toString().contains("Meja created successfully"));

        // Verify that SSE update is triggered
        verify(sseController, times(1)).triggerMejaUpdate();
    }

    @Test
    void testCreateMejaInvalidNomor() {
        Meja meja = new Meja();
        meja.setNomor(null);

        ResponseEntity<?> response = mejaController.createMeja(meja);

        assertEquals(400, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Param nomor is invalid"));

        // Verify that SSE update is NOT triggered for failed creation
        verify(sseController, never()).triggerMejaUpdate();
    }

    @Test
    void testCreateMejaDuplicate() {
        Meja meja = new Meja();
        meja.setNomor(1);

        doReturn(null).when(mejaService).createMeja(1);

        ResponseEntity<?> response = mejaController.createMeja(meja);

        assertEquals(400, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Meja dengan id tersebut sudah ada!"));

        // Verify that SSE update is NOT triggered for failed creation
        verify(sseController, never()).triggerMejaUpdate();
    }

    @Test
    void testUpdateMeja() {
        Meja meja = new Meja();
        meja.setNomor(2);
        meja.setId(1L);

        MejaDTO mejaDto = new MejaDTO();
        mejaDto.setNomor(2);
        mejaDto.setId(1L);

        Meja existingMeja = new Meja();
        existingMeja.setNomor(1);
        existingMeja.setId(1L);

        MejaDTO existingMejaDto = new MejaDTO();
        existingMejaDto.setNomor(1);
        existingMejaDto.setId(1L);

        doReturn(existingMejaDto).when(mejaService).getMejaByNomor(1);
        doReturn(meja).when(mejaService).updateMeja(1, 2);

        ResponseEntity<?> response = mejaController.updateMeja(1, meja);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().toString().contains("Meja updated successfully"));

        // Verify that SSE update is triggered
        verify(sseController, times(1)).triggerMejaUpdate();
    }

    @Test
    void testUpdateMejaNotFound() {
        Meja meja = new Meja();
        meja.setNomor(2);

        doReturn(null).when(mejaService).getMejaByNomor(1);

        ResponseEntity<?> response = mejaController.updateMeja(1, meja);

        assertEquals(400, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Request param meja_id is not found"));

        // Verify that SSE update is NOT triggered for failed update
        verify(sseController, never()).triggerMejaUpdate();
    }

    @Test
    void testDeleteMeja() {
        Meja existingMeja = new Meja();
        existingMeja.setNomor(2);
        existingMeja.setId(1L);

        MejaDTO mejaDto = new MejaDTO();
        mejaDto.setNomor(2);
        mejaDto.setId(1L);

        doReturn(mejaDto).when(mejaService).getMejaByNomor(2);
        doReturn(true).when(mejaService).deleteMeja(2);

        ResponseEntity<?> response = mejaController.deleteMeja(2);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().toString().contains("Meja deleted successfully"));

        // Verify that SSE update is triggered
        verify(sseController, times(1)).triggerMejaUpdate();
    }

    @Test
    void testDeleteMejaNotFound() {
        doReturn(null).when(mejaService).getMejaByNomor(2);

        ResponseEntity<?> response = mejaController.deleteMeja(2);

        assertEquals(400, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Request param meja_id is not found"));

        // Verify that SSE update is NOT triggered for failed deletion
        verify(sseController, never()).triggerMejaUpdate();
    }

    @Test
    void testGetAllMeja() {
        MejaDTO meja1 = new MejaDTO();
        meja1.setNomor(1);
        meja1.setId(1L);

        MejaDTO meja2 = new MejaDTO();
        meja2.setNomor(2);
        meja2.setId(2L);

        List<MejaDTO> mejaList = Arrays.asList(meja1, meja2);

        doReturn(mejaList).when(mejaService).getAllMeja();

        ResponseEntity<?> response = mejaController.getAllMeja();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());

        // Read operations don't trigger SSE updates
        verify(sseController, never()).triggerMejaUpdate();
    }

    @Test
    void testGetAllMejaEmpty() {
        doReturn(Arrays.asList()).when(mejaService).getAllMeja();

        ResponseEntity<?> response = mejaController.getAllMeja();

        assertEquals(400, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("No meja found"));
    }

    @Test
    void testGetMejaByNomor() {
        MejaDTO mejaDto = new MejaDTO();
        mejaDto.setNomor(1);
        mejaDto.setId(1L);

        doReturn(mejaDto).when(mejaService).getMejaByNomor(1);

        ResponseEntity<?> response = mejaController.getMejaByNomor(1);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());

        // Read operations don't trigger SSE updates
        verify(sseController, never()).triggerMejaUpdate();
    }

    @Test
    void testGetMejaByNomorNotFound() {
        doReturn(null).when(mejaService).getMejaByNomor(1);

        ResponseEntity<?> response = mejaController.getMejaByNomor(1);

        assertEquals(400, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Request param meja_id is not found"));
    }

    @Test
    void testSetUserToMeja() {
        Meja resultMeja = new Meja();
        resultMeja.setNomor(1);
        resultMeja.setId(1L);

        doReturn(resultMeja).when(mejaService).setUserToMeja(1, "testuser");

        UsernameDTO usernameDto = new UsernameDTO();
        usernameDto.setUsername("testuser");

        ResponseEntity<?> response = mejaController.setUserToMeja("1", usernameDto);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().toString().contains("User set to meja successfully"));

        // Verify that SSE update is triggered
        verify(sseController, times(1)).triggerMejaUpdate();
    }

    @Test
    void testSetUserToMejaFailed() {
        doReturn(null).when(mejaService).setUserToMeja(1, "testuser");

        UsernameDTO usernameDto = new UsernameDTO();
        usernameDto.setUsername("testuser");

        ResponseEntity<?> response = mejaController.setUserToMeja("1", usernameDto);

        assertEquals(400, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Failed to set user to meja"));

        // Verify that SSE update is NOT triggered for failed operation
        verify(sseController, never()).triggerMejaUpdate();
    }

    @Test
    void testRemoveUserFromMeja() {
        doReturn(true).when(mejaService).removeUserFromMeja(1);

        ResponseEntity<?> response = mejaController.removeUserFromMeja("1");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().toString().contains("Order completed successfully"));

        // Verify that SSE update is triggered
        verify(sseController, times(1)).triggerMejaUpdate();
    }

    @Test
    void testRemoveUserFromMejaFailed() {
        doReturn(false).when(mejaService).removeUserFromMeja(1);

        ResponseEntity<?> response = mejaController.removeUserFromMeja("1");

        assertEquals(400, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Error completing order"));

        // Verify that SSE update is NOT triggered for failed operation
        verify(sseController, never()).triggerMejaUpdate();
    }
}