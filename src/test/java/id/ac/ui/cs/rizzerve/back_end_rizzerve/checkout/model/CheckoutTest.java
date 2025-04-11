package id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CheckoutTest {
    @Test
    void testCalculateTotalPrice() {
        Checkout checkout = new Checkout();
        checkout.addItem("Nasi Goreng", 2, 15000);
        checkout.addItem("Es Teh", 1, 5000);

        assertEquals(35000, checkout.getTotalPrice());
    }
}
