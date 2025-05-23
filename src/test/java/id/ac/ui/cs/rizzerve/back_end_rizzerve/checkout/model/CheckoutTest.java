package id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.model;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CheckoutTest {
    private Cart cart;
    private int expectedTotal;

    @BeforeEach
    public void setUp() {
        User user = new User();
        user.setId(1L);

        CartItem item1 = CartItem.builder()
                .id(1L)
                .menuId(1L)
                .cartId(1L)
                .cart(cart)
                .quantity(2)
                .build();

        CartItem item2 = CartItem.builder()
                .id(2L)
                .cartId(1L)
                .cart(cart)
                .quantity(1)
                .build();

        cart = Cart.builder()
                .id(1L)
                .userId(1L)
                .user(user)
                .items(List.of(item1, item2))
                .build();

        expectedTotal = 2 * 15000 + 5000;
    }

    @Test
    public void testCreateCheckoutWithValidCart() {
        Checkout checkout = Checkout.builder()
                .user(cart.getUser())
                .cart(cart)
                .totalPrice(expectedTotal)
                .isSubmitted(true)
                .createdAt(LocalDateTime.now())
                .build();

        assertNotNull(checkout.getCart());
        assertEquals(2, checkout.getCart().getItems().size());
        assertEquals(expectedTotal, checkout.getTotalPrice());
        assertEquals(1L, checkout.getUser().getId());
        assertTrue(checkout.getIsSubmitted());
        assertNotNull(checkout.getCreatedAt());
    }

    @Test
    public void testCreateCheckoutWithEmptyCart() {
        Cart emptyCart = Cart.builder()
                .id(2L)
                .userId(99L)
                .items(List.of())
                .build();

        Checkout checkout = Checkout.builder()
                .user(cart.getUser())
                .cart(emptyCart)
                .totalPrice(0)
                .isSubmitted(false)
                .createdAt(LocalDateTime.now())
                .build();

        assertEquals(0, checkout.getCart().getItems().size());
        assertEquals(0, checkout.getTotalPrice());
        assertFalse(checkout.getIsSubmitted());
    }

    @Test
    public void testCheckoutCreatedAtIsNotNull() {
        Checkout checkout = Checkout.builder()
                .user(cart.getUser())
                .cart(cart)
                .totalPrice(expectedTotal)
                .isSubmitted(false)
                .createdAt(LocalDateTime.now())
                .build();

        assertNotNull(checkout.getCreatedAt());
        assertTrue(checkout.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    public void testChangeIsSubmittedStatus() {
        Checkout checkout = Checkout.builder()
                .user(cart.getUser())
                .cart(cart)
                .totalPrice(expectedTotal)
                .isSubmitted(false)
                .createdAt(LocalDateTime.now())
                .build();

        assertFalse(checkout.getIsSubmitted());

        checkout.setIsSubmitted(true);
        assertTrue(checkout.getIsSubmitted());
    }
}
