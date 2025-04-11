package id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CheckoutTest {

    @Test
    void testCheckoutStoresItemsTotalAndTimestampCorrectly() {
        CartItem item1 = new CartItem("Ayam Bakar", 2, 20000);
        CartItem item2 = new CartItem("Jus Alpukat", 1, 10000);

        List<CartItem> items = List.of(item1, item2);
        int expectedTotal = 2 * 20000 + 1 * 10000;
        LocalDateTime now = LocalDateTime.now();

        Checkout checkout = new Checkout(items, expectedTotal, now);

        assertEquals(items, checkout.getItems());
        assertEquals(expectedTotal, checkout.getTotalPrice());
        assertEquals(now, checkout.getTimestamp());
    }

    @Test
    void testEmptyItemListAllowed() {
        List<CartItem> items = List.of();
        int expectedTotal = 0;
        LocalDateTime now = LocalDateTime.now();

        Checkout checkout = new Checkout(items, expectedTotal, now);

        assertTrue(checkout.getItems().isEmpty());
        assertEquals(0, checkout.getTotalPrice());
        assertEquals(now, checkout.getTimestamp());
    }
}
