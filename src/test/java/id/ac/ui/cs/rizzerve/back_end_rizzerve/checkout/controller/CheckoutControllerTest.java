package id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.dto.CheckoutRequest;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.dto.CheckoutResponse;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.model.Checkout;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.service.CheckoutService;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CheckoutControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CheckoutService checkoutService;

    @InjectMocks
    private CheckoutController checkoutController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(checkoutController).build();
        objectMapper = new ObjectMapper();
    }

    // 1. Test Create Checkout (POST /api/checkouts)
    @Test
    void testCreateCheckoutSuccess() throws Exception {
        CheckoutRequest request = new CheckoutRequest();
        request.setCartId(1L);

        Checkout mockCheckout = Checkout.builder()
                .id(1L)
                .cart(Cart.builder().id(1L).build())
                .totalPrice(5000)
                .isSubmitted(false)
                .createdAt(LocalDateTime.now())
                .build();

        when(checkoutService.createCheckout(1L)).thenReturn(mockCheckout);

        mockMvc.perform(post("/api/checkouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verify(checkoutService, times(1)).createCheckout(1L);
    }

    @Test
    void testGetCheckoutsByUserIdSuccess() throws Exception {
        User user = new User();
        user.setId(100L);

        Checkout mockCheckouts = Checkout.builder()
                .id(1L)
                .user(user)
                .cart(Cart.builder().id(101L).build())
                .totalPrice(5000)
                .isSubmitted(false)
                .createdAt(LocalDateTime.now())
                .build();

        when(checkoutService.findCheckoutsByUserId(100L)).thenReturn(mockCheckouts);

        mockMvc.perform(get("/api/checkouts").param("userId", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testCreateCheckoutCartNotFound() throws Exception {
        CheckoutRequest request = new CheckoutRequest();
        request.setCartId(1L);

        when(checkoutService.createCheckout(1L))
                .thenThrow(new NoSuchElementException("Cart not found"));

        try{
            mockMvc.perform(post("/api/checkouts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Cart not found"));
        } catch (ServletException e){
            assertTrue(e.getMessage().contains("Cart not found"));
        }

    }

    @Test
    void testSubmitCheckoutSuccess() throws Exception {
        Checkout response = Checkout.builder()
                .id(1L)
                .cart(Cart.builder().id(2L).build())
                .totalPrice(7000)
                .isSubmitted(true)
                .createdAt(LocalDateTime.now())
                .build();

        when(checkoutService.submitCheckout(1L)).thenReturn(response);

        mockMvc.perform(put("/api/checkouts/1/submit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.isSubmitted").value(true));
    }

    @Test
    void testSubmitCheckoutNotFound() throws Exception {
        when(checkoutService.submitCheckout(1L))
                .thenThrow(new NoSuchElementException("Checkout not found"));

        mockMvc.perform(put("/api/checkouts/1/submit"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Checkout not found"));
    }

    @Test
    void testCancelCheckoutSuccess() throws Exception {
        doNothing().when(checkoutService).deleteCheckout(1L);

        mockMvc.perform(delete("/api/checkouts/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Checkout canceled"));
    }

    @Test
    void testCancelCheckoutNotFound() throws Exception {
        doThrow(new NoSuchElementException("Checkout not found"))
                .when(checkoutService).deleteCheckout(1L);

        mockMvc.perform(delete("/api/checkouts/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Checkout not found"));
    }

    @Test
    void testDeleteProcessedCheckoutSuccess() throws Exception {
        doNothing().when(checkoutService).deleteCheckoutAfterProcessing(1L);

        mockMvc.perform(delete("/api/checkouts/1/processed"))
                .andExpect(status().isOk())
                .andExpect(content().string("Checkout has been successfully processed and removed from the system."));
    }

    @Test
    void testDeleteProcessedCheckoutNotFound() throws Exception {
        doThrow(new NoSuchElementException("Checkout not found"))
                .when(checkoutService).deleteCheckoutAfterProcessing(1L);

        mockMvc.perform(delete("/api/checkouts/1/processed"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Checkout not found"));
    }

    @Test
    void testGetCheckoutDetailsSuccess() throws Exception {
        Checkout response = Checkout.builder()
                .id(1L)
                .cart(Cart.builder().id(2L).build())
                .totalPrice(8000)
                .isSubmitted(false)
                .build();

        when(checkoutService.findById(1L)).thenReturn(Optional.ofNullable(response));

        mockMvc.perform(get("/api/checkouts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.cartId").value(2L))
                .andExpect(jsonPath("$.totalPrice").value(8000))
                .andExpect(jsonPath("$.isSubmitted").value(false));
    }

    @Test
    void testGetCheckoutDetailsNotFound() throws Exception {
        when(checkoutService.findById(1L))
                .thenThrow(new NoSuchElementException("Checkout not found"));

        try{
            mockMvc.perform(get("/api/checkouts/1"))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Checkout not found"));
        }catch(ServletException e){
            assertTrue(e.getMessage().contains("Checkout not found"));
        }

    }

    @Test
    void testGetAllSubmittedCheckoutsSuccess() throws Exception {
        CheckoutResponse response1 = CheckoutResponse.builder()
                .id(1L)
                .cartId(1L)
                .totalPrice(4000)
                .isSubmitted(true)
                .createdAt(LocalDateTime.now().toString())
                .build();

        CheckoutResponse response2 = CheckoutResponse.builder()
                .id(2L)
                .cartId(2L)
                .totalPrice(6000)
                .isSubmitted(true)
                .createdAt(LocalDateTime.now().toString())
                .build();

        when(checkoutService.getSubmittedCheckouts()).thenReturn(List.of(response1, response2));

        mockMvc.perform(get("/api/checkouts/submitted"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].totalPrice").value(4000))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].totalPrice").value(6000));
    }
}