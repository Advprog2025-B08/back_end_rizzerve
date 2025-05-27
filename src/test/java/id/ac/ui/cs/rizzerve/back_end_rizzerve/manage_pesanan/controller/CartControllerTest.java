package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.controller;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.command.CartCommandInvoker;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.service.CartService;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    @Mock
    private CartService cartService;

    @Mock
    private CartCommandInvoker commandInvoker;

    @InjectMocks
    private CartController cartController;

    private CartItem testCartItem;
    private Menu testMenu;
    private Long testUserId;
    private Long testMenuId;

    @BeforeEach
    void setUp() {
        testUserId = 1L;
        testMenuId = 1L;

        testMenu = new Menu();
        testMenu.setId(testMenuId);
        testMenu.setName("Test Menu");
        testMenu.setPrice((long) 10000.0);

        testCartItem = CartItem.builder()
                .id(1L)
                .cartId(1L)
                .menuId(testMenuId)
                .quantity(1)
                .menu(testMenu)
                .build();
    }

    @Test
    void addItemToCart_Success() throws Exception {
        CompletableFuture<CartItem> future = CompletableFuture.completedFuture(testCartItem);
        when(cartService.addItemToCartAsync(testUserId, testMenuId)).thenReturn(future);

        DeferredResult<ResponseEntity<CartItem>> result = cartController.addItemToCart(testUserId, testMenuId);

        ResponseEntity<CartItem> response = (ResponseEntity<CartItem>) result.getResult();

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testCartItem, response.getBody());
        verify(cartService).addItemToCartAsync(testUserId, testMenuId);
    }

    @Test
    void addItemToCart_Exception() throws Exception {
        CompletableFuture<CartItem> future = CompletableFuture.failedFuture(new RuntimeException("Test exception"));
        when(cartService.addItemToCartAsync(testUserId, testMenuId)).thenReturn(future);

        DeferredResult<ResponseEntity<CartItem>> result = cartController.addItemToCart(testUserId, testMenuId);

        ResponseEntity<?> response = (ResponseEntity<?>) result.getResult();

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Error adding item to cart"));
        verify(cartService).addItemToCartAsync(testUserId, testMenuId);
    }

    @Test
    void updateCartItem_Success_WithItem() throws Exception {
        int quantityChange = 1;
        CartItem updatedItem = CartItem.builder()
                .id(1L)
                .cartId(1L)
                .menuId(testMenuId)
                .quantity(2)
                .menu(testMenu)
                .build();

        CompletableFuture<CartItem> future = CompletableFuture.completedFuture(updatedItem);
        when(cartService.updateCartItemQuantityAsync(testUserId, testMenuId, quantityChange)).thenReturn(future);

        DeferredResult<ResponseEntity<?>> result = cartController.updateCartItem(testUserId, testMenuId, quantityChange);

        ResponseEntity<?> response = (ResponseEntity<?>) result.getResult();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedItem, response.getBody());
        verify(cartService).updateCartItemQuantityAsync(testUserId, testMenuId, quantityChange);
    }

    @Test
    void updateCartItem_Success_ItemRemoved() throws Exception {
        int quantityChange = -1;
        CompletableFuture<CartItem> future = CompletableFuture.completedFuture(null);
        when(cartService.updateCartItemQuantityAsync(testUserId, testMenuId, quantityChange)).thenReturn(future);

        DeferredResult<ResponseEntity<?>> result = cartController.updateCartItem(testUserId, testMenuId, quantityChange);

        ResponseEntity<?> response = (ResponseEntity<?>) result.getResult();

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(cartService).updateCartItemQuantityAsync(testUserId, testMenuId, quantityChange);
    }

    @Test
    void updateCartItem_Exception() throws Exception {
        int quantityChange = 1;
        CompletableFuture<CartItem> future = CompletableFuture.failedFuture(new RuntimeException("Test exception"));
        when(cartService.updateCartItemQuantityAsync(testUserId, testMenuId, quantityChange)).thenReturn(future);

        DeferredResult<ResponseEntity<?>> result = cartController.updateCartItem(testUserId, testMenuId, quantityChange);

        ResponseEntity<?> response = (ResponseEntity<?>) result.getResult();

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Error updating cart item"));
        verify(cartService).updateCartItemQuantityAsync(testUserId, testMenuId, quantityChange);
    }

    @Test
    void removeItemFromCart_Success() throws Exception {
        CompletableFuture<Void> future = CompletableFuture.completedFuture(null);
        when(cartService.removeItemFromCartAsync(testUserId, testMenuId)).thenReturn(future);

        DeferredResult<ResponseEntity<Void>> result = cartController.removeItemFromCart(testUserId, testMenuId);

        ResponseEntity<?> response = (ResponseEntity<?>) result.getResult();

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(cartService).removeItemFromCartAsync(testUserId, testMenuId);
    }

    @Test
    void removeItemFromCart_Exception() throws Exception {
        CompletableFuture<Void> future = CompletableFuture.failedFuture(new RuntimeException("Test exception"));
        when(cartService.removeItemFromCartAsync(testUserId, testMenuId)).thenReturn(future);

        DeferredResult<ResponseEntity<Void>> result = cartController.removeItemFromCart(testUserId, testMenuId);

        ResponseEntity<?> response = (ResponseEntity<?>) result.getResult();

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Error removing item from cart"));
        verify(cartService).removeItemFromCartAsync(testUserId, testMenuId);
    }

    @Test
    void getCartItems_Success() throws Exception {
        List<CartItem> cartItems = Arrays.asList(testCartItem);
        CompletableFuture<List<CartItem>> future = CompletableFuture.completedFuture(cartItems);
        when(cartService.getCartItemsAsync(testUserId)).thenReturn(future);

        DeferredResult<ResponseEntity<List<CartItem>>> result = cartController.getCartItems(testUserId);

        ResponseEntity<List<CartItem>> response = (ResponseEntity<List<CartItem>>) result.getResult();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cartItems, response.getBody());
        assertEquals(1, response.getBody().size());
        verify(cartService).getCartItemsAsync(testUserId);
    }

    @Test
    void getCartItems_Exception() throws Exception {
        CompletableFuture<List<CartItem>> future = CompletableFuture.failedFuture(new RuntimeException("Test exception"));
        when(cartService.getCartItemsAsync(testUserId)).thenReturn(future);

        DeferredResult<ResponseEntity<List<CartItem>>> result = cartController.getCartItems(testUserId);

        ResponseEntity<?> response = (ResponseEntity<?>) result.getResult();

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Error getting cart items"));
        verify(cartService).getCartItemsAsync(testUserId);
    }

    @Test
    void clearCart_Success() throws Exception {
        CompletableFuture<Void> future = CompletableFuture.completedFuture(null);
        when(cartService.clearCartAsync(testUserId)).thenReturn(future);

        DeferredResult<ResponseEntity<Void>> result = cartController.clearCart(testUserId);

        ResponseEntity<?> response = (ResponseEntity<?>) result.getResult();

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(cartService).clearCartAsync(testUserId);
    }

    @Test
    void clearCart_Exception() throws Exception {
        CompletableFuture<Void> future = CompletableFuture.failedFuture(new RuntimeException("Test exception"));
        when(cartService.clearCartAsync(testUserId)).thenReturn(future);

        DeferredResult<ResponseEntity<Void>> result = cartController.clearCart(testUserId);

        ResponseEntity<?> response = (ResponseEntity<?>) result.getResult();

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Error clearing cart"));
        verify(cartService).clearCartAsync(testUserId);
    }

    @Test
    void constructor_ShouldInitializeFields() {
        CartController controller = new CartController(cartService, commandInvoker);
        assertNotNull(controller);
    }

    @Test
    void addItemToCart_AsyncTimeout() throws Exception {
        CompletableFuture<CartItem> slowFuture = new CompletableFuture<>();
        when(cartService.addItemToCartAsync(testUserId, testMenuId)).thenReturn(slowFuture);

        DeferredResult<ResponseEntity<CartItem>> result = cartController.addItemToCart(testUserId, testMenuId);

        assertNotNull(result);
        assertFalse(result.hasResult());
        verify(cartService).addItemToCartAsync(testUserId, testMenuId);
    }

    @Test
    void updateCartItem_AsyncTimeout() throws Exception {
        int quantityChange = 1;
        CompletableFuture<CartItem> slowFuture = new CompletableFuture<>();
        when(cartService.updateCartItemQuantityAsync(testUserId, testMenuId, quantityChange)).thenReturn(slowFuture);

        DeferredResult<ResponseEntity<?>> result = cartController.updateCartItem(testUserId, testMenuId, quantityChange);

        assertNotNull(result);
        assertFalse(result.hasResult());
        verify(cartService).updateCartItemQuantityAsync(testUserId, testMenuId, quantityChange);
    }

    @Test
    void removeItemFromCart_AsyncTimeout() throws Exception {
        CompletableFuture<Void> slowFuture = new CompletableFuture<>();
        when(cartService.removeItemFromCartAsync(testUserId, testMenuId)).thenReturn(slowFuture);

        DeferredResult<ResponseEntity<Void>> result = cartController.removeItemFromCart(testUserId, testMenuId);

        assertNotNull(result);
        assertFalse(result.hasResult());
        verify(cartService).removeItemFromCartAsync(testUserId, testMenuId);
    }

    @Test
    void getCartItems_AsyncTimeout() throws Exception {
        CompletableFuture<List<CartItem>> slowFuture = new CompletableFuture<>();
        when(cartService.getCartItemsAsync(testUserId)).thenReturn(slowFuture);

        DeferredResult<ResponseEntity<List<CartItem>>> result = cartController.getCartItems(testUserId);

        assertNotNull(result);
        assertFalse(result.hasResult());
        verify(cartService).getCartItemsAsync(testUserId);
    }

    @Test
    void clearCart_AsyncTimeout() throws Exception {
        CompletableFuture<Void> slowFuture = new CompletableFuture<>();
        when(cartService.clearCartAsync(testUserId)).thenReturn(slowFuture);

        DeferredResult<ResponseEntity<Void>> result = cartController.clearCart(testUserId);

        assertNotNull(result);
        assertFalse(result.hasResult());
        verify(cartService).clearCartAsync(testUserId);
    }
}