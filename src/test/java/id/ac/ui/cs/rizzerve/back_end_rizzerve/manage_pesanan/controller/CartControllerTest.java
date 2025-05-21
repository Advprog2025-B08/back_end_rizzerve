package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.controller;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.command.*;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    @Mock
    private CartService cartService;

    @Mock
    private CartCommandInvoker commandInvoker;

    @InjectMocks
    private CartController cartController;

    @Captor
    private ArgumentCaptor<CartCommand> commandCaptor;

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
    }

    @Test
    void testAddItemToCart() {
        when(commandInvoker.executeCommand(any(CartCommand.class))).thenReturn(cartItem);

        ResponseEntity<CartItem> response = cartController.addItemToCart(userId, menuId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(cartItem, response.getBody());

        verify(commandInvoker).executeCommand(commandCaptor.capture());
        CartCommand capturedCommand = commandCaptor.getValue();
        assertTrue(capturedCommand instanceof AddToCartCommand);
    }

    @Test
    void testUpdateCartItemIncrease() {
        when(commandInvoker.executeCommand(any(CartCommand.class))).thenReturn(cartItem);

        ResponseEntity<?> response = cartController.updateCartItem(userId, menuId, 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cartItem, response.getBody());

        verify(commandInvoker).executeCommand(commandCaptor.capture());
        CartCommand capturedCommand = commandCaptor.getValue();
        assertTrue(capturedCommand instanceof UpdateCartCommand);
    }

    @Test
    void testUpdateCartItemDecreaseToDeletion() {
        when(commandInvoker.executeCommand(any(CartCommand.class))).thenReturn(null);

        ResponseEntity<?> response = cartController.updateCartItem(userId, menuId, -1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(commandInvoker).executeCommand(commandCaptor.capture());
        CartCommand capturedCommand = commandCaptor.getValue();
        assertTrue(capturedCommand instanceof UpdateCartCommand);
    }

    @Test
    void testRemoveItemFromCart() {
        when(commandInvoker.executeCommand(any(CartCommand.class))).thenReturn(null);

        ResponseEntity<Void> response = cartController.removeItemFromCart(userId, menuId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(commandInvoker).executeCommand(commandCaptor.capture());
        CartCommand capturedCommand = commandCaptor.getValue();
        assertTrue(capturedCommand instanceof RemoveFromCartCommand);
    }

    @Test
    void testGetCartItems() {
        List<CartItem> items = Arrays.asList(cartItem);
        when(commandInvoker.executeCommand(any(CartCommand.class))).thenReturn(items);

        ResponseEntity<List<CartItem>> response = cartController.getCartItems(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(items, response.getBody());
        assertEquals(1, response.getBody().size());

        verify(commandInvoker).executeCommand(commandCaptor.capture());
        CartCommand capturedCommand = commandCaptor.getValue();
        assertTrue(capturedCommand instanceof GetCartItemsCommand);
    }

    @Test
    void testClearCart() {
        when(commandInvoker.executeCommand(any(CartCommand.class))).thenReturn(null);

        ResponseEntity<Void> response = cartController.clearCart(userId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(commandInvoker).executeCommand(commandCaptor.capture());
        CartCommand capturedCommand = commandCaptor.getValue();
        assertTrue(capturedCommand instanceof ClearCartCommand);
    }
}