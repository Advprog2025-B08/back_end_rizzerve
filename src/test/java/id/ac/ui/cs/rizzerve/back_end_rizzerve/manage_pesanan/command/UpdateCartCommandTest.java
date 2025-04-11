package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.command;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateCartCommandTest {

    @Mock
    private CartService cartService;

    private UpdateCartCommand command;
    private Long userId;
    private Long menuId;
    private int quantityChange;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        userId = 1L;
        menuId = 1L;
        quantityChange = 1;
        cartItem = CartItem.builder()
                .id(1L)
                .cartId(1L)
                .menuId(menuId)
                .quantity(2)
                .build();

        command = new UpdateCartCommand(cartService, userId, menuId, quantityChange);
    }

    @Test
    void testExecuteWithIncrease() {
        when(cartService.updateCartItemQuantity(userId, menuId, quantityChange)).thenReturn(cartItem);

        CartItem result = command.execute();

        assertEquals(cartItem, result);
        verify(cartService).updateCartItemQuantity(userId, menuId, quantityChange);
    }

    @Test
    void testExecuteWithDecreaseToZero() {
        when(cartService.updateCartItemQuantity(userId, menuId, -2)).thenReturn(null);

        UpdateCartCommand decreaseCommand = new UpdateCartCommand(cartService, userId, menuId, -2);
        CartItem result = decreaseCommand.execute();

        assertNull(result);
        verify(cartService).updateCartItemQuantity(userId, menuId, -2);
    }
}