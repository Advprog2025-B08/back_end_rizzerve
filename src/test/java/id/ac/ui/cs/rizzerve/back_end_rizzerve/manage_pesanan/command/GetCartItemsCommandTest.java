package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.command;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCartItemsCommandTest {

    @Mock
    private CartService cartService;

    private GetCartItemsCommand command;
    private Long userId;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        userId = 1L;
        cartItem = CartItem.builder()
                .id(1L)
                .cartId(1L)
                .menuId(1L)
                .quantity(2)
                .build();

        command = new GetCartItemsCommand(cartService, userId);
    }

    @Test
    void testExecuteWithItems() {
        List<CartItem> items = Arrays.asList(cartItem);
        when(cartService.getCartItems(userId)).thenReturn(items);

        List<CartItem> result = command.execute();

        assertEquals(1, result.size());
        assertEquals(cartItem, result.get(0));
        verify(cartService).getCartItems(userId);
    }

    @Test
    void testExecuteWithEmptyCart() {
        when(cartService.getCartItems(userId)).thenReturn(Collections.emptyList());

        List<CartItem> result = command.execute();

        assertTrue(result.isEmpty());
        verify(cartService).getCartItems(userId);
    }
}