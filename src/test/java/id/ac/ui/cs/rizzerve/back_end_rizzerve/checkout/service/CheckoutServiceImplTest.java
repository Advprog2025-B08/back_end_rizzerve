package id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.model.Checkout;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.repository.CheckoutRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.repository.MenuRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository.CartItemRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository.CartRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.service.CartServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Remove;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CheckoutServiceImplTest {
    private CheckoutRepository checkoutRepository;
    private CheckoutServiceImpl checkoutService;
    private CartRepository cartRepository;
    private CartItemRepository cartItemRepository;
    private MenuRepository menuRepository;

    private Cart cart;
    private int expectedTotal;

    @BeforeEach
    void setUp() {
        checkoutRepository = mock(CheckoutRepository.class);
        cartRepository = mock(CartRepository.class);
        menuRepository = mock(MenuRepository.class);
        cartItemRepository = mock(CartItemRepository.class);

        checkoutService = new CheckoutServiceImpl(checkoutRepository, cartRepository);


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
                .items(new ArrayList<>(List.of(item1, item2)))
                .build();

        expectedTotal = 3;

        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        // Only calculate total items for now..
    }

    @Test
    void testCreateCheckoutFromCart() {
        // Arrange
        when(checkoutRepository.save(any(Checkout.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Checkout checkout = checkoutService.createCheckout(cart.getId());

        // Assert
        assertNotNull(checkout);
        assertEquals(cart.getUserId(), checkout.getUser().getId());
        assertEquals(expectedTotal, checkout.getTotalPrice());
        assertEquals(cart.getItems().size(), checkout.getCart().getItems().size());
        assertFalse(checkout.getIsSubmitted());
        assertNotNull(checkout.getCreatedAt());

        verify(checkoutRepository, times(1)).save(any(Checkout.class));
    }

    @Test
    void testUpdateCartItemQuantityIncreaseFromCheckout() {
        when(checkoutRepository.save(any(Checkout.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        // Arrange
        Checkout checkout = checkoutService.createCheckout(cart.getId());
        Cart checkoutCart = checkout.getCart();

        CartItem firstItem = checkoutCart.getItems().getFirst();
        int originalQuantity = firstItem.getQuantity();

        // Simulate increasing quantity
        firstItem.setQuantity(originalQuantity + 2);

        // Act
        when(cartRepository.save(any(Cart.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        cartRepository.save(checkoutCart);

        // Assert
        assertEquals(originalQuantity + 2, checkoutCart.getItems().getFirst().getQuantity());
    }

    @Test
    void testUpdateCartItemQuantityDecreaseToDeletionFromCheckout() {
        when(checkoutRepository.save(any(Checkout.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        // Arrange
        Checkout checkout = checkoutService.createCheckout(cart.getId());
        Cart checkoutCart = checkout.getCart();

        CartItem itemToRemove = checkoutCart.getItems().get(1); // ambil item dengan quantity 1
        assertEquals(1, itemToRemove.getQuantity());

        // Simulate quantity decrease to 0 (deletion)
        checkoutCart.getItems().removeIf(item -> item.getId().equals(itemToRemove.getId()));

        // Act
        when(cartRepository.save(any(Cart.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        cartRepository.save(checkoutCart);

        // Assert
        assertEquals(1, checkoutCart.getItems().size()); // awalnya 2, sekarang 1
        assertFalse(checkoutCart.getItems().contains(itemToRemove));
    }

    @Test
    void testUpdateCartItemWithInvalidItem() {
        when(checkoutRepository.save(any(Checkout.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        // Arrange
        Checkout checkout = checkoutService.createCheckout(cart.getId());
        Cart checkoutCart = checkout.getCart();

        Long invalidItemId = 999L;
        boolean found = checkoutCart.getItems().stream().anyMatch(item -> item.getId().equals(invalidItemId));

        // Act & Assert
        assertFalse(found, "Item dengan ID invalid tidak boleh ada di dalam cart.");
    }

    @Test
    void testDeleteCheckout() {
        // Arrange
        Long checkoutId = 10L;
        Checkout checkout = Checkout.builder()
                .id(checkoutId)
                .cart(cart)
                .user(cart.getUser())
                .isSubmitted(false)
                .totalPrice(expectedTotal)
                .createdAt(LocalDateTime.now())
                .build();

        when(checkoutRepository.findById(checkoutId)).thenReturn(Optional.of(checkout));
        doNothing().when(checkoutRepository).delete(checkout);

        // Act
        checkoutService.deleteCheckout(checkoutId);

        // Assert
        verify(checkoutRepository, times(1)).findById(checkoutId);
        verify(checkoutRepository).delete(checkout);
    }

}
