package id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.dto.CheckoutRequest;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.model.Checkout;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.service.CheckoutService;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CheckoutControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CheckoutService checkoutService;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CheckoutController checkoutController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(checkoutController).build();
    }

    @Test
    void testCreateCheckoutSuccess() throws Exception {
        CheckoutRequest request = new CheckoutRequest();
        request.setCartId(1L);

        Cart cart = Cart.builder().id(1L).build();

        Checkout checkout = Checkout.builder()
                .id(10L)
                .cart(cart)
                .totalPrice(100)
                .isSubmitted(false)
                .createdAt(LocalDateTime.now())
                .build();

        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(checkoutService.createCheckout(1L)).thenReturn(checkout);

        mockMvc.perform(post("/api/checkouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.totalPrice").value(100));
    }

    @Test
    public void testCreateCheckoutCartNotFound() throws Exception {
        // Setup mock cartRepository supaya findById return Optional.empty()
        when(cartRepository.findById(anyLong())).thenReturn(Optional.empty());

        String jsonRequest = """
            {
                "cartId": 123
            }
            """;

        mockMvc.perform(post("/api/checkouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cart not found"));
    }
}
