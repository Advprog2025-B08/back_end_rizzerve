package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.command;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RemoveFromCartCommandTest {

    @Mock
    private CartService cartService;

    private RemoveFromCartCommand command;
    private Long userId;
    private Long menuId;

    @BeforeEach
    void setUp() {
        userId = 1L;
        menuId = 1L;
        command = new RemoveFromCartCommand(cartService, userId, menuId);
    }

    @Test
    void testExecute() {
        doNothing().when(cartService).removeItemFromCart(userId, menuId);

        Void result = command.execute();

        assertNull(result);
        verify(cartService).removeItemFromCart(userId, menuId);
    }
}