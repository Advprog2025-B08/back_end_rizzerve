// ClearCartCommandTest.java
package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.command;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
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
class ClearCartCommandTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    private Long userId = 1L;
    private ClearCartCommand command;

    @BeforeEach
    void setUp() {
        command = new ClearCartCommand(cartRepository, cartItemRepository, userId);
    }

    @Test
    void testExecute_Success() {
        // Given
        Cart cart = Cart.builder().id(1L).userId(userId).build();
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        // When
        command.execute();

        // Then
        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartItemRepository, times(1)).deleteByCartId(cart.getId());
    }

    @Test
    void testExecute_CartNotFound() {
        // Given
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(IllegalStateException.class, () -> command.execute());
        assertEquals("Cart not found for user: " + userId, exception.getMessage());

        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartItemRepository, never()).deleteByCartId(anyLong());
    }
}