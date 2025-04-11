package id.ac.ui.cs.rizzerve.back_end_rizzerve.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.Product;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.User;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private User user;
    private Product product;
    private Cart cart;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("TestUser")
                .build();

        product = Product.builder()
                .id(1L)
                .name("Test Product")
                .build();

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(new ArrayList<>());
    }

    @Test
    void testGetOrCreateCartForUserExisting() {
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));

        Cart result = cartService.getOrCreateCartForUser(user.getId());

        assertEquals(cart, result);
        verify(cartRepository).findByUserId(user.getId());
        verify(cartRepository, never()).save(any());
    }

    @Test
    void testGetOrCreateCartForUserNew() {
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.empty());
        when(cartRepository.save(any())).thenAnswer(invocation -> {
            Cart savedCart = invocation.getArgument(0);
            savedCart.setId(1L);
            return savedCart;
        });

        Cart result = cartService.getOrCreateCartForUser(user.getId());

        assertNotNull(result);
        assertEquals(user.getId(), result.getUser().getId());
        verify(cartRepository).findByUserId(user.getId());
        verify(cartRepository).save(any());
    }

    @Test
    void testAddProductToCart() {
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(cartRepository.save(any())).thenReturn(cart);

        cartService.addProductToCart(user.getId(), product);

        verify(cartRepository).save(any());
    }

    @Test
    void testUpdateCartItemQuantity() {
        CartItem cartItem = CartItem.builder()
                .product(product)
                .quantity(1)
                .build();

        cart.getItems().add(cartItem);

        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(cartRepository.save(any())).thenReturn(cart);

        cartService.updateCartItemQuantity(user.getId(), product.getId(), 1);

        verify(cartRepository).save(cart);
        assertEquals(2, cart.getItems().get(0).getQuantity());
    }

    @Test
    void testUpdateCartItemQuantityRemove() {
        CartItem cartItem = CartItem.builder()
                .product(product)
                .quantity(1)
                .build();

        cart.getItems().add(cartItem);

        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(cartRepository.save(any())).thenReturn(cart);

        cartService.updateCartItemQuantity(user.getId(), product.getId(), -1);

        verify(cartRepository).save(cart);
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void testRemoveProductFromCart() {
        CartItem cartItem = CartItem.builder()
                .product(product)
                .quantity(2)
                .build();

        cart.getItems().add(cartItem);

        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(cartRepository.save(any())).thenReturn(cart);

        cartService.removeProductFromCart(user.getId(), product.getId());

        verify(cartRepository).save(cart);
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void testClearCart() {
        CartItem cartItem = CartItem.builder()
                .product(product)
                .quantity(2)
                .build();

        cart.getItems().add(cartItem);

        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(cartRepository.save(any())).thenReturn(cart);

        cartService.clearCart(user.getId());

        verify(cartRepository).save(cart);
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void testGetCartByUserId() {
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));

        Optional<Cart> result = cartService.getCartByUserId(user.getId());

        assertTrue(result.isPresent());
        assertEquals(cart, result.get());
        verify(cartRepository).findByUserId(user.getId());
    }

}