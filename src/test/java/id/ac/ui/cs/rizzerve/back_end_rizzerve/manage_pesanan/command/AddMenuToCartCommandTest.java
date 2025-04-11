// AddMenuToCartCommandTest.java
package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.command;

import static org.junit.jupiter.api.Assertions.*;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddMenuToCartCommandTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    private AddMenuToCartCommand command;
    private Long userId = 1L;
    private Long menuId = 1L;

    @BeforeEach
    void setUp() {
        command = new AddMenuToCartCommand(cartRepository, cartItemRepository, userId, menuId);
    }

    @Test
    void testExecute_CartExistsItemNew() {
        // Given
        Cart cart = Cart.builder().id(1L).userId(userId).build();
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(cart.getId(), menuId)).thenReturn(Optional.empty());

        // When
        command.execute();

        // Then
        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartItemRepository, times(1)).findByCartIdAndMenuId(cart.getId(), menuId);
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void testExecute_CartExistsItemExists() {
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
    void testExecute_CartDoesNotExist() {
        // Given
        Cart newCart = Cart.builder().id(1L).userId(userId).build();

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(newCart);

        // When
        command.execute();

        // Then
        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartRepository, times(1)).save(any(Cart.class));
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }
}