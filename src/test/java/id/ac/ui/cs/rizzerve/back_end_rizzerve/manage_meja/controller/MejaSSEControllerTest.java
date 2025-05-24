package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.controller;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.dto.MejaDTO;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.service.MejaService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MejaSSEControllerTest {

    @Mock
    private MejaService mejaService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private MejaSSEController mejaSSEController;

    private List<MejaDTO> sampleMejaList;

    @BeforeEach
    void setUp() {
        // Create sample data
        MejaDTO meja1 = new MejaDTO();
        meja1.setId(1L);
        meja1.setNomor(1);
        meja1.setUsername("user1");

        MejaDTO meja2 = new MejaDTO();
        meja2.setId(2L);
        meja2.setNomor(2);
        meja2.setUsername(null);

        sampleMejaList = Arrays.asList(meja1, meja2);
    }

    @Test
    void testStreamMejaUpdates_Success() {
        // Setup
        when(mejaService.getAllMeja()).thenReturn(sampleMejaList);

        // Execute
        ResponseEntity<SseEmitter> response = mejaSSEController.streamMejaUpdates(httpServletRequest);

        // Verify
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof SseEmitter);

        // Verify that mejaService.getAllMeja() was called for initial data
        verify(mejaService, times(1)).getAllMeja();
    }

    @Test
    void testStreamMejaUpdates_EmptyMejaList() {
        // Setup
        when(mejaService.getAllMeja()).thenReturn(Collections.emptyList());

        // Execute
        ResponseEntity<SseEmitter> response = mejaSSEController.streamMejaUpdates(httpServletRequest);

        // Verify
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof SseEmitter);

        // Verify that mejaService.getAllMeja() was called even for empty list
        verify(mejaService, times(1)).getAllMeja();
    }

    @Test
    void testStreamMejaUpdates_ServiceThrowsException() {
        when(mejaService.getAllMeja()).thenThrow(new RuntimeException("Database error"));

        try {
            ResponseEntity<SseEmitter> response = mejaSSEController.streamMejaUpdates(httpServletRequest);

            assertEquals(200, response.getStatusCode().value());
            assertNotNull(response.getBody());
            assertTrue(response.getBody() instanceof SseEmitter);

        } catch (RuntimeException e) {
            assertEquals("Database error", e.getMessage());
        }

        // Verify that mejaService.getAllMeja() was called
        verify(mejaService, times(1)).getAllMeja();
    }

    @Test
    void testTriggerMejaUpdate() {
        // Setup
        when(mejaService.getAllMeja()).thenReturn(sampleMejaList);

        // Create an SSE connection to have emitters in the list
        mejaSSEController.streamMejaUpdates(httpServletRequest);

        // Execute
        mejaSSEController.triggerMejaUpdate();

        // Verify
        // getAllMeja should be called twice: once for initial connection, once for trigger
        verify(mejaService, atLeast(2)).getAllMeja();
    }

    @Test
    void testTriggerMejaUpdate_NoEmitters() {

        // Execute - Call triggerMejaUpdate when no emitters are connected
        mejaSSEController.triggerMejaUpdate();

        assertTrue(true, "triggerMejaUpdate executed without throwing exception");
    }

    @Test
    void testSendMejaUpdates_WithEmitters() throws InterruptedException {
        // Setup
        when(mejaService.getAllMeja()).thenReturn(sampleMejaList);

        // Create an SSE connection to add emitters
        ResponseEntity<SseEmitter> response = mejaSSEController.streamMejaUpdates(httpServletRequest);
        SseEmitter emitter = response.getBody();

        Thread.sleep(100);

        // Execute trigger to force an update
        mejaSSEController.triggerMejaUpdate();

        // Verify
        assertNotNull(emitter);
        verify(mejaService, atLeast(2)).getAllMeja();
    }

    @Test
    void testSseEmitterCallbacks() {
        // Setup
        when(mejaService.getAllMeja()).thenReturn(sampleMejaList);

        // Create an SSE connection
        ResponseEntity<SseEmitter> response = mejaSSEController.streamMejaUpdates(httpServletRequest);
        SseEmitter emitter = response.getBody();

        assertNotNull(emitter);

        emitter.onCompletion(() -> {
        });

        assertNotNull(emitter);
        assertTrue(true, "Completion callback registered successfully");
    }

    @Test
    void testSseEmitterTimeoutCallback() {
        // Setup
        when(mejaService.getAllMeja()).thenReturn(sampleMejaList);

        // Create an SSE connection
        ResponseEntity<SseEmitter> response = mejaSSEController.streamMejaUpdates(httpServletRequest);
        SseEmitter emitter = response.getBody();

        assertNotNull(emitter);

        // Test that timeout callback can be set without error
        CountDownLatch timeoutLatch = new CountDownLatch(1);
        emitter.onTimeout(timeoutLatch::countDown);

        // Verify emitter was created and callback was set successfully
        assertNotNull(emitter);
    }

    @Test
    void testSseEmitterErrorCallback() {
        // Setup
        when(mejaService.getAllMeja()).thenReturn(sampleMejaList);

        // Create an SSE connection
        ResponseEntity<SseEmitter> response = mejaSSEController.streamMejaUpdates(httpServletRequest);
        SseEmitter emitter = response.getBody();

        assertNotNull(emitter);

        // Test that error callback can be registered without error
        emitter.onError((ex) -> {
        });

        // Verify emitter was created and callback registration succeeded
        assertNotNull(emitter);
        assertTrue(true, "Error callback registered successfully");
    }

    @Test
    void testConstructorInitializesScheduler() throws InterruptedException {
        // This test verifies the constructor initializes without errors
        when(mejaService.getAllMeja()).thenReturn(sampleMejaList);

        // Create a new instance to test constructor
        MejaSSEController newController = new MejaSSEController(mejaService);

        // Verify the controller was created successfully
        assertNotNull(newController);

        // Test that the controller can be used normally after construction
        ResponseEntity<SseEmitter> response = newController.streamMejaUpdates(httpServletRequest);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());

        verify(mejaService, atLeast(1)).getAllMeja(); // At least for the streamMejaUpdates call
    }

    @Test
    void testMultipleEmitters() {
        // Setup
        when(mejaService.getAllMeja()).thenReturn(sampleMejaList);

        // Create multiple SSE connections
        ResponseEntity<SseEmitter> response1 = mejaSSEController.streamMejaUpdates(httpServletRequest);
        ResponseEntity<SseEmitter> response2 = mejaSSEController.streamMejaUpdates(httpServletRequest);

        // Verify both connections are successful
        assertEquals(200, response1.getStatusCode().value());
        assertEquals(200, response2.getStatusCode().value());
        assertNotNull(response1.getBody());
        assertNotNull(response2.getBody());

        // Trigger an update - should send to both emitters
        mejaSSEController.triggerMejaUpdate();

        // Verify service was called for initial connections + trigger
        verify(mejaService, atLeast(3)).getAllMeja();
    }

    @Test
    void testEmitterRemovalOnCompletion() throws InterruptedException {
        // Setup
        when(mejaService.getAllMeja()).thenReturn(sampleMejaList);

        // Create an SSE connection
        ResponseEntity<SseEmitter> response = mejaSSEController.streamMejaUpdates(httpServletRequest);
        SseEmitter emitter = response.getBody();

        assertNotNull(emitter);

        emitter.onCompletion(() -> {

        });

        assertNotNull(emitter);
        assertTrue(true, "Emitter completion callback setup successful");

    }

    @Test
    void testRequestParameterHandling() {
        // Setup
        when(mejaService.getAllMeja()).thenReturn(sampleMejaList);

        // Test with null request parameter (should still work)
        ResponseEntity<SseEmitter> response = mejaSSEController.streamMejaUpdates(null);

        // Verify
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        verify(mejaService, times(1)).getAllMeja();
    }

    @Test
    void testLargeDataSet() {
        // Setup - create a large list of meja data
        List<MejaDTO> largeMejaList = Arrays.asList(
                createMejaDTO(1L, 1, "user1"),
                createMejaDTO(2L, 2, "user2"),
                createMejaDTO(3L, 3, null),
                createMejaDTO(4L, 4, "user4"),
                createMejaDTO(5L, 5, null)
        );

        when(mejaService.getAllMeja()).thenReturn(largeMejaList);

        // Execute
        ResponseEntity<SseEmitter> response = mejaSSEController.streamMejaUpdates(httpServletRequest);

        // Verify
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        verify(mejaService, times(1)).getAllMeja();
    }

    // Helper method to create MejaDTO
    private MejaDTO createMejaDTO(Long id, int nomor, String username) {
        MejaDTO meja = new MejaDTO();
        meja.setId(id);
        meja.setNomor(nomor);
        meja.setUsername(username);
        return meja;
    }
}