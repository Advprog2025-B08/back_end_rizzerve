package id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.dto.CheckoutResponse;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.model.Checkout;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.repository.CheckoutRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.repository.MenuRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.repository.UserRepository;
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
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CheckoutServiceImplTest {
    private CheckoutRepository checkoutRepository;
    private CheckoutServiceImpl checkoutService;
    private CartRepository cartRepository;
    private UserRepository userRepository;
    private CartItemRepository cartItemRepository;

    private Cart cart;
    private int expectedTotal;

    @BeforeEach
    void setUp() {
        checkoutRepository = mock(CheckoutRepository.class);
        cartRepository = mock(CartRepository.class);
        cartItemRepository = mock(CartItemRepository.class);

        checkoutService = new CheckoutServiceImpl(checkoutRepository, cartRepository, userRepository);

        User user = new User();
        user.setId(1L);

        Menu menu1 = new Menu();
        menu1.setId(1L);
        menu1.setName("Menu 1");
        menu1.setPrice(15000);

        Menu menu2 = new Menu();
        menu2.setId(2L);
        menu2.setName("Menu 2");
        menu2.setPrice(5000);

        CartItem item1 = CartItem.builder()
                .id(1L)
                .menuId(1L)
                .cartId(1L)
                .cart(cart)
                .quantity(2)
                .menuId(1L)
                .menu(menu1)
                .build();

        CartItem item2 = CartItem.builder()
                .id(2L)
                .cartId(1L)
                .cart(cart)
                .quantity(1)
                .menuId(2L)
                .menu(menu2)
                .build();

        cart = Cart.builder()
                .id(1L)
                .userId(1L)
                .user(user)
                .items(new ArrayList<>(List.of(item1, item2)))
                .build();

        expectedTotal = 35000;

        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
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

        when(cartRepository.save(any(Cart.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        cartRepository.save(checkoutCart);

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

    @Test
    void testFindByIdWithValidCheckoutId() {
        // Arrange
        Long checkoutId = 1L;
        Checkout checkout = new Checkout();
        checkout.setId(checkoutId);

        when(checkoutRepository.findById(checkoutId)).thenReturn(Optional.of(checkout));

        // Act
        Optional<Checkout> result = checkoutService.findById(checkoutId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(checkoutId, result.get().getId());
        verify(checkoutRepository, times(1)).findById(checkoutId);
    }

    @Test
    void testFindByIdWithInvalidCheckoutId() {
        // Arrange
        Long checkoutId = 1L;
        when(checkoutRepository.findById(checkoutId)).thenReturn(Optional.empty());

        // Act
        Optional<Checkout> result = checkoutService.findById(checkoutId);

        // Assert
        assertFalse(result.isPresent());
        verify(checkoutRepository, times(1)).findById(checkoutId);
    }

    @Test
    void testSubmitCheckoutWithValidCheckoutId() {
        Long checkoutId = 1L;
        Checkout checkout = new Checkout();
        checkout.setId(checkoutId);
        checkout.setIsSubmitted(false);

        when(checkoutRepository.findById(checkoutId)).thenReturn(Optional.of(checkout));
        when(checkoutRepository.save(any(Checkout.class))).thenReturn(checkout);

        Checkout result = checkoutService.submitCheckout(checkoutId);

        assertTrue(result.getIsSubmitted());
        verify(checkoutRepository, times(1)).findById(checkoutId);
        verify(checkoutRepository, times(1)).save(checkout);
    }

    @Test
    void testSubmitCheckoutWithInvalidCheckoutId() {
        Long checkoutId = 1L;
        when(checkoutRepository.findById(checkoutId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> checkoutService.submitCheckout(checkoutId));
        verify(checkoutRepository, times(1)).findById(checkoutId);
    }

    @Test
    void testCreateCheckoutWithInvalidCartId() {
        Long invalidCartId = 999L;
        when(cartRepository.findById(invalidCartId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> checkoutService.createCheckout(invalidCartId));
        verify(cartRepository, times(1)).findById(invalidCartId);
    }

    @Test
    void testDeleteCheckoutWithInvalidCheckoutId() {
        Long invalidCheckoutId = 999L;
        when(checkoutRepository.findById(invalidCheckoutId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> checkoutService.deleteCheckout(invalidCheckoutId));
        verify(checkoutRepository, times(1)).findById(invalidCheckoutId);
    }

    @Test
    void testSubmitCheckoutWithAlreadySubmittedCheckout() {
        Long checkoutId = 1L;
        Checkout checkout = new Checkout();
        checkout.setId(checkoutId);
        checkout.setIsSubmitted(true);

        when(checkoutRepository.findById(checkoutId)).thenReturn(Optional.of(checkout));

        assertThrows(IllegalStateException.class, () -> checkoutService.submitCheckout(checkoutId));
        verify(checkoutRepository, times(1)).findById(checkoutId);
    }

    @Test
    void testUpdateCartItemQuantityWithInvalidCartId() {
        Long invalidCartId = 999L;
        Long itemId = 1L;
        int deltaQuantity = 2;

        when(cartRepository.findById(invalidCartId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                checkoutService.updateCartItemQuantity(invalidCartId, itemId, deltaQuantity));
        verify(cartRepository, times(1)).findById(invalidCartId);
    }

    @Test
    void testCreateCheckoutWithEmptyCart() {
        Long cartId = 1L;
        Cart emptyCart = new Cart();
        emptyCart.setItems(List.of());

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(emptyCart));
    }

    @Test
    void testUpdateCartItemQuantityToResultInNegativeQuantity() {
        // Arrange
        Long cartId = 1L;
        Long itemId = 1L;
        int deltaQuantity = -5;

        CartItem cartItem = new CartItem();
        cartItem.setQuantity(2);

        Cart cart = new Cart();
        cart.setItems(List.of(cartItem));

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findById(itemId)).thenReturn(Optional.of(cartItem));

        assertThrows(NoSuchElementException.class, () ->
                checkoutService.updateCartItemQuantity(cartId, itemId, deltaQuantity));
    }
}
