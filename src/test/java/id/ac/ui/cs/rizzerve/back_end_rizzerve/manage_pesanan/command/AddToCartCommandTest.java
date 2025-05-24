package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.command;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddToCartCommandTest {

    @Mock
    private CartService cartService;

    private AddToCartCommand command;
    private Long userId;
    private Long menuId;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        userId = 1L;
        menuId = 1L;
        cartItem = CartItem.builder()
                .id(1L)
                .cartId(1L)
                .menuId(menuId)
                .quantity(1)
                .build();

        command = new AddToCartCommand(cartService, userId, menuId);
    }

    @Test
    void testExecute() {
        when(cartService.addItemToCart(userId, menuId)).thenReturn(cartItem);

        CartItem result = command.execute();

        assertEquals(cartItem, result);
        verify(cartService).addItemToCart(userId, menuId);
    }
}