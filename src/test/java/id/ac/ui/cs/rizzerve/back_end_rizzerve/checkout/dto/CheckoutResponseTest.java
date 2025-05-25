package id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CheckoutResponseTest {

    @Test
    void testBuilder() {
        CheckoutResponse response = CheckoutResponse.builder()
                .id(1L)
                .cartId(2L)
                .userId(3L)
                .totalPrice(10000)
                .isSubmitted(true)
                .createdAt("2023-11-01")
                .build();

        assertEquals(1L, response.getId());
        assertEquals(2L, response.getCartId());
        assertEquals(3L, response.getUserId());
        assertEquals(10000, response.getTotalPrice());
        assertEquals(true, response.getIsSubmitted());
        assertEquals("2023-11-01", response.getCreatedAt());
    }
}
