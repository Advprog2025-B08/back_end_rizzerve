package id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.builder;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.model.Checkout;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CheckoutBuilderTest {

    @Test
    void testCheckoutBuilderBuildsCorrectTotalAndItemList() {
        CartItem item1 = new CartItem("Nasi Goreng", 2, 15000);
        CartItem item2 = new CartItem("Es Teh", 1, 5000);
        Cart cart = new Cart(List.of(item1, item2));

        LocalDateTime now = LocalDateTime.now();
        Checkout checkout = CheckoutBuilder.builder()
                .fromCart(cart)
                .withTimestamp(now)
                .build();

        assertEquals(35000, checkout.getTotalPrice());
        assertEquals(2, checkout.getItems().size());
        assertEquals("Nasi Goreng", checkout.getItems().getFirst().getName());
        assertEquals(now, checkout.getTimestamp());
    }

    @Test
    void testBuilderThrowsExceptionIfCartIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            CheckoutBuilder.builder()
                    .fromCart(null)
                    .withTimestamp(LocalDateTime.now())
                    .build();
        });
    }
}
