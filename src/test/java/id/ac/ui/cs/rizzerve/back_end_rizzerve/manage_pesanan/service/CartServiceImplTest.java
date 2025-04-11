// CartServiceImplTest.java
package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository.CartItemRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository.CartRepository;
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

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private Cart cart;
    private CartItem cartItem;
    private Long userId = 1L;
    private Long menuId = 1L;

    @BeforeEach
    void setUp() {
        cart = Cart.builder().id(1L).userId(userId).build();
        cartItem = CartItem.builder().id(1L).cartId(1L).menuId(menuId).quantity(1).build();
    }

    @Test
    void testAddMenuToCart_NewItem() {
        // Given
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(cart.getId(), menuId)).thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        // When
        cartService.addMenuToCart(userId, menuId);

        // Then
        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartItemRepository, times(1)).findByCartIdAndMenuId(cart.getId(), menuId);
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void testAddMenuToCart_ExistingItem() {
        // Given
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(cart.getId(), menuId)).thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);

        // When
        cartService.addMenuToCart(userId, menuId);

        // Then
        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartItemRepository, times(1)).findByCartIdAndMenuId(cart.getId(), menuId);
        verify(cartItemRepository, times(1)).save(cartItem);
        assertEquals(2, cartItem.getQuantity());
    }

    @Test
    void testUpdateCartItemQuantity_Increase() {
        // Given
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(cart.getId(), menuId)).thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);

        // When
        cartService.updateCartItemQuantity(userId, menuId, 1);

        // Then
        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartItemRepository, times(1)).findByCartIdAndMenuId(cart.getId(), menuId);
        verify(cartItemRepository, times(1)).save(cartItem);
        assertEquals(2, cartItem.getQuantity());
    }

    @Test
    void testUpdateCartItemQuantity_Decrease() {
        // Given
        cartItem.setQuantity(2);
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(cart.getId(), menuId)).thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);

        // When
        cartService.updateCartItemQuantity(userId, menuId, -1);

        // Then
        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartItemRepository, times(1)).findByCartIdAndMenuId(cart.getId(), menuId);
        verify(cartItemRepository, times(1)).save(cartItem);
        assertEquals(1, cartItem.getQuantity());
    }

    @Test
    void testUpdateCartItemQuantity_DecreaseToZero() {
        // Given
        cartItem.setQuantity(1);
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(cart.getId(), menuId)).thenReturn(Optional.of(cartItem));

        // When
        cartService.updateCartItemQuantity(userId, menuId, -1);

        // Then
        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartItemRepository, times(1)).findByCartIdAndMenuId(cart.getId(), menuId);
        verify(cartItemRepository, times(1)).delete(cartItem);
    }

    @Test
    void testRemoveMenuFromCart() {
        // Given
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(cart.getId(), menuId)).thenReturn(Optional.of(cartItem));

        // When
        cartService.removeMenuFromCart(userId, menuId);

        // Then
        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartItemRepository, times(1)).findByCartIdAndMenuId(cart.getId(), menuId);
        verify(cartItemRepository, times(1)).delete(cartItem);
    }

    @Test
    void testClearCart() {
        // Given
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        // When
        cartService.clearCart(userId);

        // Then
        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartItemRepository, times(1)).deleteByCartId(cart.getId());
    }

    @Test
    void testGetCartItems() {
        // Given
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(cart.getId())).thenReturn(cartItems);

        // When
        List<CartItem> result = cartService.getCartItems(userId);

        // Then
        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartItemRepository, times(1)).findByCartId(cart.getId());
        assertEquals(1, result.size());
        assertEquals(menuId, result.get(0).getMenuId());
    }

    @Test
    void testGetCartItems_NoCart() {
        // Given
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // When
        List<CartItem> result = cartService.getCartItems(userId);

        // Then
        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartItemRepository, never()).findByCartId(anyLong());
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetOrCreateCart_Existing() {
        // Given
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        // When
        Cart result = cartService.getOrCreateCart(userId);

        // Then
        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartRepository, never()).save(any(Cart.class));
        assertEquals(cart, result);
    }

    @Test
    void testGetOrCreateCart_New() {
        // Given
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // When
        Cart result = cartService.getOrCreateCart(userId);

        // Then
        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartRepository, times(1)).save(any(Cart.class));
        assertEquals(cart, result);
    }
}