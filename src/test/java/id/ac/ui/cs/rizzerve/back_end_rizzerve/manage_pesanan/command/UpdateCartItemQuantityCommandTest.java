// UpdateCartItemQuantityCommandTest.java
package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.command;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository.CartItemRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCartItemQuantityCommandTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    private Long userId = 1L;
    private Long menuId = 1L;
    private UpdateCartItemQuantityCommand command;

    @BeforeEach
    void setUp() {
        command = new UpdateCartItemQuantityCommand(cartRepository, cartItemRepository, userId, menuId, 1); // Increase by 1
    }

    @Test
    void testExecute_IncreaseQuantity() {
        // Given
        Cart cart = Cart.builder().id(1L).userId(userId).build();
        CartItem cartItem = CartItem.builder().id(1L).cartId(cart.getId()).menuId(menuId).quantity(1).build();

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(cart.getId(), menuId)).thenReturn(Optional.of(cartItem));

        // When
        command.execute();

        // Then
        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartItemRepository, times(1)).findByCartIdAndMenuId(cart.getId(), menuId);
        verify(cartItemRepository, times(1)).save(cartItem);
        assertEquals(2, cartItem.getQuantity());
    }

    @Test
    void testExecute_DecreaseQuantity() {
        // Given
        Cart cart = Cart.builder().id(1L).userId(userId).build();
        CartItem cartItem = CartItem.builder().id(1L).cartId(cart.getId()).menuId(menuId).quantity(2).build();

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(cart.getId(), menuId)).thenReturn(Optional.of(cartItem));

        UpdateCartItemQuantityCommand decreaseCommand = new UpdateCartItemQuantityCommand(
                cartRepository, cartItemRepository, userId, menuId, -1); // Decrease by 1

        // When
        decreaseCommand.execute();

        // Then
        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartItemRepository, times(1)).findByCartIdAndMenuId(cart.getId(), menuId);
        verify(cartItemRepository, times(1)).save(cartItem);
        assertEquals(1, cartItem.getQuantity());
    }

    @Test
    void testExecute_DecreaseToZero() {
        // Given
        Cart cart = Cart.builder().id(1L).userId(userId).build();
        CartItem cartItem = CartItem.builder().id(1L).cartId(cart.getId()).menuId(menuId).quantity(1).build();

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(cart.getId(), menuId)).thenReturn(Optional.of(cartItem));

        UpdateCartItemQuantityCommand decreaseCommand = new UpdateCartItemQuantityCommand(
                cartRepository, cartItemRepository, userId, menuId, -1); // Decrease by 1

        // When
        decreaseCommand.execute();

        // Then
        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartItemRepository, times(1)).findByCartIdAndMenuId(cart.getId(), menuId);
        verify(cartItemRepository, times(1)).delete(cartItem);
        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    void testExecute_CartNotFound() {
        // Given
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(IllegalStateException.class, () -> command.execute());
        assertEquals("Cart not found for user: " + userId, exception.getMessage());

        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartItemRepository, never()).findByCartIdAndMenuId(anyLong(), anyLong());
    }

    @Test
    void testExecute_ItemNotFound() {
        // Given
        Cart cart = Cart.builder().id(1L).userId(userId).build();
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(cart.getId(), menuId)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(IllegalStateException.class, () -> command.execute());
        assertEquals("Menu item not found in cart", exception.getMessage());

        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartItemRepository, times(1)).findByCartIdAndMenuId(cart.getId(), menuId);
    }
}