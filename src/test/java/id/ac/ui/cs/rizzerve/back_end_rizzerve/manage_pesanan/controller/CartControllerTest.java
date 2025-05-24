//package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.controller;
//
//import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.command.*;
//import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
//import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.service.CartService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class CartControllerTest {
//
//    @Mock
//    private CartService cartService;
//
//    @Mock
//    private CartCommandInvoker commandInvoker;
//
//    @InjectMocks
//    private CartController cartController;
//
//    private CartItem sampleCartItem;
//    private CartItem item2;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        sampleCartItem = new CartItem();
//        sampleCartItem.setMenuId(1L);
//        sampleCartItem.setQuantity(2);
//
//        item2 = new CartItem();
//        item2.setMenuId(2L);
//        item2.setQuantity(1);
//    }
//
//    @Test
//    void testAddItemToCart() throws Exception {
//        when(commandInvoker.executeCommand(any(AddToCartCommand.class)))
//                .thenReturn(sampleCartItem);
//
//        DeferredResult<ResponseEntity<CartItem>> result = cartController.addItemToCart(1L, 1L);
//        result.setResultHandler(r -> {
//            ResponseEntity<CartItem> response = (ResponseEntity<CartItem>) r;
//            assertEquals(HttpStatus.CREATED, response.getStatusCode());
//            assertEquals(sampleCartItem, response.getBody());
//        });
//
//        // Simulate async completion
//        result.setResult(new ResponseEntity<>(sampleCartItem, HttpStatus.CREATED));
//    }
//
//    @Test
//    void testUpdateCartItem() throws Exception {
//        when(commandInvoker.executeCommand(any(UpdateCartCommand.class)))
//                .thenReturn(sampleCartItem);
//
//        DeferredResult<ResponseEntity<?>> result = cartController.updateCartItem(1L, 1L, 1);
//        result.setResultHandler(r -> {
//            ResponseEntity<?> response = (ResponseEntity<?>) r;
//            assertEquals(HttpStatus.OK, response.getStatusCode());
//            assertEquals(sampleCartItem, response.getBody());
//        });
//
//        result.setResult(new ResponseEntity<>(sampleCartItem, HttpStatus.OK));
//    }
//
//    @Test
//    void testUpdateCartItemNotFound() throws Exception {
//        when(commandInvoker.executeCommand(any(UpdateCartCommand.class)))
//                .thenReturn(null);
//
//        DeferredResult<ResponseEntity<?>> result = cartController.updateCartItem(1L, 1L, 1);
//        result.setResultHandler(r -> {
//            ResponseEntity<?> response = (ResponseEntity<?>) r;
//            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
//            assertNull(response.getBody());
//        });
//
//        result.setResult(new ResponseEntity<>(HttpStatus.NO_CONTENT));
//    }
//
//    @Test
//    void testRemoveItemFromCart() throws Exception {
//        doNothing().when(commandInvoker).executeCommand(any(RemoveFromCartCommand.class));
//
//        DeferredResult<ResponseEntity<Void>> result = cartController.removeItemFromCart(1L, 1L);
//        result.setResultHandler(r -> {
//            ResponseEntity<Void> response = (ResponseEntity<Void>) r;
//            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
//        });
//
//        result.setResult(new ResponseEntity<>(HttpStatus.NO_CONTENT));
//    }
//
//    @Test
//    void testGetCartItems() throws Exception {
//        List<CartItem> items = Arrays.asList(sampleCartItem, item2);
//        when(commandInvoker.executeCommand(any(GetCartItemsCommand.class)))
//                .thenReturn(items);
//
//        DeferredResult<ResponseEntity<List<CartItem>>> result = cartController.getCartItems(1L);
//        result.setResultHandler(r -> {
//            ResponseEntity<List<CartItem>> response = (ResponseEntity<List<CartItem>>) r;
//            assertEquals(HttpStatus.OK, response.getStatusCode());
//            assertEquals(2, response.getBody().size());
//        });
//
//        result.setResult(new ResponseEntity<>(items, HttpStatus.OK));
//    }
//
//    @Test
//    void testClearCart() throws Exception {
//        doNothing().when(commandInvoker).executeCommand(any(ClearCartCommand.class));
//
//        DeferredResult<ResponseEntity<Void>> result = cartController.clearCart(1L);
//        result.setResultHandler(r -> {
//            ResponseEntity<Void> response = (ResponseEntity<Void>) r;
//            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
//        });
//
//        result.setResult(new ResponseEntity<>(HttpStatus.NO_CONTENT));
//    }
//
//    @Test
//    void testErrorHandling() throws Exception {
//        when(commandInvoker.executeCommand(any(AddToCartCommand.class)))
//                .thenThrow(new RuntimeException("Test error"));
//
//        DeferredResult<ResponseEntity<CartItem>> result = cartController.addItemToCart(1L, 1L);
//        result.setErrorHandler(e -> {
//            assertTrue(e instanceof RuntimeException);
//            assertEquals("Test error", e.getMessage());
//        });
//
//        result.setErrorResult(new RuntimeException("Test error"));
//    }
//}