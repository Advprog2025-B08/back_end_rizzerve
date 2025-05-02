package id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CheckoutTest {

    @Test
    public void testCreateCheckoutWithValidData() {
        // Arrange
        Long userId = 1L;
        CartItem item1 = CartItem.builder()
                .id(1L)
                .quantity(2)
                .menuItem(MenuItem.builder()
                        .id(10L)
                        .name("Nasi Goreng")
                        .price(15000)
                        .build())
                .build();

        CartItem item2 = CartItem.builder()
                .id(2L)
                .quantity(1)
                .menuItem(MenuItem.builder()
                        .id(11L)
                        .name("Teh Manis")
                        .price(5000)
                        .build())
                .build();

        // Act
        Checkout checkout = Checkout.builder()
                .userId(userId)
                .items(List.of(item1, item2))
                .totalPrice(2 * 15000 + 1 * 5000)
                .isSubmitted(true)
                .createdAt(LocalDateTime.now())
                .build();

        // Assert
        assertEquals(userId, checkout.getUserId());
        assertEquals(2, checkout.getItems().size());
        assertEquals(35000, checkout.getTotalPrice());
        assertTrue(checkout.getIsSubmitted());
        assertNotNull(checkout.getCreatedAt());
    }

    @Test
    public void testCheckoutWithEmptyItemsShouldBeInvalid() {
        Checkout checkout = Checkout.builder()
                .userId(1L)
                .items(List.of())
                .totalPrice(0)
                .isSubmitted(false)
                .createdAt(LocalDateTime.now())
                .build();

        assertEquals(0, checkout.getItems().size());
        assertEquals(0, checkout.getTotalPrice());
        assertFalse(checkout.getIsSubmitted());
    }
}
