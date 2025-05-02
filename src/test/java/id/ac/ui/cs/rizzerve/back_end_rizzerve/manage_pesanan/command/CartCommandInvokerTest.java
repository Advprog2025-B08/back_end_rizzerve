package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.command;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartCommandInvokerTest {

    @Mock
    private CartCommand command;

    private CartCommandInvoker invoker;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        invoker = new CartCommandInvoker();
        cartItem = CartItem.builder()
                .id(1L)
                .cartId(1L)
                .menuId(1L)
                .quantity(1)
                .build();
    }

    @Test
    void testExecuteCommand() {
        when(command.execute()).thenReturn(cartItem);

        Object result = invoker.executeCommand(command);

        assertEquals(cartItem, result);
        verify(command).execute();
    }
}