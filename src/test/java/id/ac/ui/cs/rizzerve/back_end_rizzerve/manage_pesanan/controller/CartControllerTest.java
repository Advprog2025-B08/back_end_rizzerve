package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.controller;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.command.*;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    private MockMvc mockMvc;
    private CartItem sampleCartItem;
    private List<CartItem> sampleCartItems;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();

        // Setup sample data
        sampleCartItem = new CartItem();
        sampleCartItem.setId(1L);
        sampleCartItem.setUserId(1L);
        sampleCartItem.setMenuId(1L);
        sampleCartItem.setQuantity(2);

        CartItem item2 = new CartItem();
        item2.setId(2L);
        item2.setUserId(1L);
        item2.setMenuId(2L);
        item2.setQuantity(1);

        sampleCartItems = Arrays.asList(sampleCartItem, item2);
    }

    @Test
    void addItemToCart_Success() throws Exception {
        // Arrange
        Long userId = 1L;
        Long menuId = 1L;

        when(commandInvoker.executeCommand(any(AddToCartCommand.class)))
                .thenReturn(sampleCartItem);

        // Act
        DeferredResult<ResponseEntity<CartItem>> result = cartController.addItemToCart(userId, menuId);

        // Assert
        assertNotNull(result);

        // Wait for async result
        ResponseEntity<CartItem> response = result.getResult(5, TimeUnit.SECONDS);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sampleCartItem, response.getBody());

        verify(commandInvoker, times(1)).executeCommand(any(AddToCartCommand.class));
    }

    @Test
    void addItemToCart_Exception() throws Exception {
        // Arrange
        Long userId = 1L;
        Long menuId = 1L;
        RuntimeException exception = new RuntimeException("Service error");

        when(commandInvoker.executeCommand(any(AddToCartCommand.class)))
                .thenThrow(exception);

        // Act
        DeferredResult<ResponseEntity<CartItem>> result = cartController.addItemToCart(userId, menuId);

        // Assert
        assertNotNull(result);

        // Wait for async result and expect exception
        assertThrows(Exception.class, () -> {
            result.getResult(5, TimeUnit.SECONDS);
        });

        verify(commandInvoker, times(1)).executeCommand(any(AddToCartCommand.class));
    }

    @Test
    void updateCartItem_Success() throws Exception {
        // Arrange
        Long userId = 1L;
        Long menuId = 1L;
        int quantityChange = 2;

        when(commandInvoker.executeCommand(any(UpdateCartCommand.class)))
                .thenReturn(sampleCartItem);

        // Act
        DeferredResult<ResponseEntity<?>> result = cartController.updateCartItem(userId, menuId, quantityChange);

        // Assert
        assertNotNull(result);

        ResponseEntity<?> response = result.getResult(5, TimeUnit.SECONDS);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleCartItem, response.getBody());

        verify(commandInvoker, times(1)).executeCommand(any(UpdateCartCommand.class));
    }

    @Test
    void updateCartItem_ItemNotFound() throws Exception {
        // Arrange
        Long userId = 1L;
        Long menuId = 1L;
        int quantityChange = 2;

        when(commandInvoker.executeCommand(any(UpdateCartCommand.class)))
                .thenReturn(null);

        // Act
        DeferredResult<ResponseEntity<?>> result = cartController.updateCartItem(userId, menuId, quantityChange);

        // Assert
        assertNotNull(result);

        ResponseEntity<?> response = result.getResult(5, TimeUnit.SECONDS);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(commandInvoker, times(1)).executeCommand(any(UpdateCartCommand.class));
    }

    @Test
    void updateCartItem_Exception() throws Exception {
        // Arrange
        Long userId = 1L;
        Long menuId = 1L;
        int quantityChange = 2;
        RuntimeException exception = new RuntimeException("Update error");

        when(commandInvoker.executeCommand(any(UpdateCartCommand.class)))
                .thenThrow(exception);

        // Act
        DeferredResult<ResponseEntity<?>> result = cartController.updateCartItem(userId, menuId, quantityChange);

        // Assert
        assertNotNull(result);

        assertThrows(Exception.class, () -> {
            result.getResult(5, TimeUnit.SECONDS);
        });

        verify(commandInvoker, times(1)).executeCommand(any(UpdateCartCommand.class));
    }

    @Test
    void removeItemFromCart_Success() throws Exception {
        // Arrange
        Long userId = 1L;
        Long menuId = 1L;

        doNothing().when(commandInvoker).executeCommand(any(RemoveFromCartCommand.class));

        // Act
        DeferredResult<ResponseEntity<Void>> result = cartController.removeItemFromCart(userId, menuId);

        // Assert
        assertNotNull(result);

        ResponseEntity<Void> response = result.getResult(5, TimeUnit.SECONDS);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(commandInvoker, times(1)).executeCommand(any(RemoveFromCartCommand.class));
    }

    @Test
    void removeItemFromCart_Exception() throws Exception {
        // Arrange
        Long userId = 1L;
        Long menuId = 1L;
        RuntimeException exception = new RuntimeException("Remove error");

        doThrow(exception).when(commandInvoker).executeCommand(any(RemoveFromCartCommand.class));

        // Act
        DeferredResult<ResponseEntity<Void>> result = cartController.removeItemFromCart(userId, menuId);

        // Assert
        assertNotNull(result);

        assertThrows(Exception.class, () -> {
            result.getResult(5, TimeUnit.SECONDS);
        });

        verify(commandInvoker, times(1)).executeCommand(any(RemoveFromCartCommand.class));
    }

    @Test
    void getCartItems_Success() throws Exception {
        // Arrange
        Long userId = 1L;

        when(commandInvoker.executeCommand(any(GetCartItemsCommand.class)))
                .thenReturn(sampleCartItems);

        // Act
        DeferredResult<ResponseEntity<List<CartItem>>> result = cartController.getCartItems(userId);

        // Assert
        assertNotNull(result);

        ResponseEntity<List<CartItem>> response = result.getResult(5, TimeUnit.SECONDS);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleCartItems, response.getBody());
        assertEquals(2, response.getBody().size());

        verify(commandInvoker, times(1)).executeCommand(any(GetCartItemsCommand.class));
    }

    @Test
    void getCartItems_EmptyCart() throws Exception {
        // Arrange
        Long userId = 1L;
        List<CartItem> emptyList = Arrays.asList();

        when(commandInvoker.executeCommand(any(GetCartItemsCommand.class)))
                .thenReturn(emptyList);

        // Act
        DeferredResult<ResponseEntity<List<CartItem>>> result = cartController.getCartItems(userId);

        // Assert
        assertNotNull(result);

        ResponseEntity<List<CartItem>> response = result.getResult(5, TimeUnit.SECONDS);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());

        verify(commandInvoker, times(1)).executeCommand(any(GetCartItemsCommand.class));
    }

    @Test
    void getCartItems_Exception() throws Exception {
        // Arrange
        Long userId = 1L;
        RuntimeException exception = new RuntimeException("Get items error");

        when(commandInvoker.executeCommand(any(GetCartItemsCommand.class)))
                .thenThrow(exception);

        // Act
        DeferredResult<ResponseEntity<List<CartItem>>> result = cartController.getCartItems(userId);

        // Assert
        assertNotNull(result);

        assertThrows(Exception.class, () -> {
            result.getResult(5, TimeUnit.SECONDS);
        });

        verify(commandInvoker, times(1)).executeCommand(any(GetCartItemsCommand.class));
    }

    @Test
    void clearCart_Success() throws Exception {
        // Arrange
        Long userId = 1L;

        doNothing().when(commandInvoker).executeCommand(any(ClearCartCommand.class));

        // Act
        DeferredResult<ResponseEntity<Void>> result = cartController.clearCart(userId);

        // Assert
        assertNotNull(result);

        ResponseEntity<Void> response = result.getResult(5, TimeUnit.SECONDS);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(commandInvoker, times(1)).executeCommand(any(ClearCartCommand.class));
    }

    @Test
    void clearCart_Exception() throws Exception {
        // Arrange
        Long userId = 1L;
        RuntimeException exception = new RuntimeException("Clear cart error");

        doThrow(exception).when(commandInvoker).executeCommand(any(ClearCartCommand.class));

        // Act
        DeferredResult<ResponseEntity<Void>> result = cartController.clearCart(userId);

        // Assert
        assertNotNull(result);

        assertThrows(Exception.class, () -> {
            result.getResult(5, TimeUnit.SECONDS);
        });

        verify(commandInvoker, times(1)).executeCommand(any(ClearCartCommand.class));
    }

    @Test
    void constructor_ShouldInitializeFields() {
        // Act
        CartController controller = new CartController(cartService, commandInvoker);

        // Assert
        assertNotNull(controller);
    }
}