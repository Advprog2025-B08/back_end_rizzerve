package id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.model.Checkout;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.repository.CheckoutRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CheckoutServiceImplTest {

    private CheckoutRepository checkoutRepository;
    private CheckoutService checkoutService;

    private Cart cart;
    private int expectedTotal;

    @BeforeEach
    void setUp() {
        checkoutRepository = mock(CheckoutRepository.class);
        checkoutService = new CheckoutServiceImpl(checkoutRepository);

        User user = User.builder()
                .id(1L)
                .username("dummyuser")
                .password("crazykiller")
                .build();

        CartItem item1 = CartItem.builder()
                .id(1L)
                .menuId(1L)
                .cartId(1L)
                .cart(cart)
                .quantity(2)
                .price(15000)
                .build();

        CartItem item2 = CartItem.builder()
                .id(2L)
                .cartId(1L)
                .cart(cart)
                .quantity(1)
                .price(5000)
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
    void testCreateCheckoutFromCart() {
        // Arrange
        when(checkoutRepository.save(any(Checkout.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Checkout checkout = checkoutService.createCheckout(cart);

        // Assert
        assertNotNull(checkout);
        assertEquals(cart.getUserId(), checkout.getUserId());
        assertEquals(expectedTotal, checkout.getTotalPrice());
        assertEquals(cart.getItems().size(), checkout.getCart().getItems().size());
        assertTrue(checkout.getIsSubmitted());
        assertNotNull(checkout.getCreatedAt());

        verify(checkoutRepository, times(1)).save(any(Checkout.class));
    }
}
