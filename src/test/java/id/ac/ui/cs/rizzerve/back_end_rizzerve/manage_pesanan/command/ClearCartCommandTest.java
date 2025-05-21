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
class ClearCartCommandTest {

    @Mock
    private CartService cartService;

    private ClearCartCommand command;
    private Long userId;

    @BeforeEach
    void setUp() {
        userId = 1L;
        command = new ClearCartCommand(cartService, userId);
    }

    @Test
    void testExecute() {
        doNothing().when(cartService).clearCart(userId);

        Void result = command.execute();

        assertNull(result);
        verify(cartService).clearCart(userId);
    }
}