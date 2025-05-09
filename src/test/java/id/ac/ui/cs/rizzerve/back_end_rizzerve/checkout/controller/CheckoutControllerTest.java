package id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.dto.CheckoutRequest;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.model.Checkout;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.service.CheckoutService;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CheckoutController.class)
public class CheckoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public CheckoutService checkoutService() {
            return Mockito.mock(CheckoutService.class);
        }
    }

    @Test
    void testCreateCheckout() throws Exception {
        // Arrange
        Cart dummyCart = Cart.builder()
                .id(1L)
                .userId(1L)
                .build();

        Checkout dummyCheckout = Checkout.builder()
                .id(1L)
                .userId(1L)
                .cart(dummyCart)
                .totalPrice(100)
                .isSubmitted(true)
                .createdAt(LocalDateTime.now())
                .build();

        Mockito.when(checkoutService.createCheckout(any(Cart.class)))
                .thenReturn(dummyCheckout);

        CheckoutRequest request = new CheckoutRequest();
        request.setCart(dummyCart);

        // Act & Assert
        mockMvc.perform(post("/api/checkout")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.totalPrice").value(100))
                .andExpect(jsonPath("$.isSubmitted").value(true));
    }

    @Test
    void testCreateCheckout_InvalidCartId() throws Exception {
        CheckoutRequest invalidRequest = new CheckoutRequest(); // cartId is null

        mockMvc.perform(post("/api/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].defaultMessage").value("Cart ID cannot be null"));
    }
}
