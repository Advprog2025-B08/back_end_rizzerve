package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository.CartItemRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository.CartRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.repository.MenuRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;
import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private Long userId;
    private Long menuId;
    private Long cartId;
    private Cart cart;
    private CartItem cartItem;
    private Menu menu;

    @BeforeEach
    void setUp() {
        userId = 1L;
        menuId = 1L;
        cartId = 1L;

        menu = new Menu();
        menu.setId(menuId);
        menu.setName("Test Menu");
        menu.setPrice(10000L);

        cart = Cart.builder()
                .id(cartId)
                .userId(userId)
                .items(new ArrayList<>())
                .build();

        cartItem = CartItem.builder()
                .id(1L)
                .cartId(cartId)
                .menuId(menuId)
                .quantity(1)
                .menu(menu)
                .build();
    }

    @Test
    void testGetOrCreateCart_ExistingCart() {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        Cart result = cartService.getOrCreateCart(userId);

        assertNotNull(result);
        assertEquals(cartId, result.getId());
        assertEquals(userId, result.getUserId());
        verify(cartRepository).findByUserId(userId);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void testGetOrCreateCart_NewCart() {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.getOrCreateCart(userId);

        assertNotNull(result);
        assertEquals(cartId, result.getId());
        assertEquals(userId, result.getUserId());
        verify(cartRepository).findByUserId(userId);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void testAddItemToCart_NewItem() {
        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(cartId, menuId)).thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(cartItemRepository.findByCartIdAndMenuIdWithMenu(cartId, menuId)).thenReturn(Optional.of(cartItem));

        CartItem result = cartService.addItemToCart(userId, menuId);

        assertNotNull(result);
        assertEquals(cartId, result.getCartId());
        assertEquals(menuId, result.getMenuId());
        assertEquals(1, result.getQuantity());
        verify(menuRepository).findById(menuId);
        verify(cartItemRepository).save(any(CartItem.class));
        verify(cartItemRepository).findByCartIdAndMenuIdWithMenu(cartId, menuId);
    }

    @Test
    void testAddItemToCart_ExistingItem() {
        CartItem existingItem = CartItem.builder()
                .id(1L)
                .cartId(cartId)
                .menuId(menuId)
                .quantity(2)
                .build();

        CartItem updatedItem = CartItem.builder()
                .id(1L)
                .cartId(cartId)
                .menuId(menuId)
                .quantity(3)
                .menu(menu)
                .build();

        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(cartId, menuId)).thenReturn(Optional.of(existingItem));
        when(cartItemRepository.save(existingItem)).thenReturn(updatedItem);
        when(cartItemRepository.findByCartIdAndMenuIdWithMenu(cartId, menuId)).thenReturn(Optional.of(updatedItem));

        CartItem result = cartService.addItemToCart(userId, menuId);

        assertNotNull(result);
        assertEquals(3, result.getQuantity());
        verify(cartItemRepository).save(existingItem);
        assertEquals(3, existingItem.getQuantity());
    }

    @Test
    void testAddItemToCart_MenuNotFound() {
        when(menuRepository.findById(menuId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> cartService.addItemToCart(userId, menuId));
        assertEquals("Menu not found with id: " + menuId, exception.getMessage());
        verify(menuRepository).findById(menuId);
        verify(cartRepository, never()).findByUserId(anyLong());
    }

    @Test
    void testAddItemToCart_FindWithMenuReturnsEmpty() {
        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(cartId, menuId)).thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(cartItemRepository.findByCartIdAndMenuIdWithMenu(cartId, menuId)).thenReturn(Optional.empty());

        CartItem result = cartService.addItemToCart(userId, menuId);

        assertNotNull(result);
        assertEquals(cartItem, result);
        verify(cartItemRepository).findByCartIdAndMenuIdWithMenu(cartId, menuId);
    }

    @Test
    void testUpdateCartItemQuantity_PositiveChange() {
        CartItem existingItem = CartItem.builder()
                .id(1L)
                .cartId(cartId)
                .menuId(menuId)
                .quantity(2)
                .build();

        CartItem updatedItem = CartItem.builder()
                .id(1L)
                .cartId(cartId)
                .menuId(menuId)
                .quantity(4)
                .menu(menu)
                .build();

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(cartId, menuId)).thenReturn(Optional.of(existingItem));
        when(cartItemRepository.save(existingItem)).thenReturn(updatedItem);
        when(cartItemRepository.findByCartIdAndMenuIdWithMenu(cartId, menuId)).thenReturn(Optional.of(updatedItem));

        CartItem result = cartService.updateCartItemQuantity(userId, menuId, 2);

        assertNotNull(result);
        assertEquals(4, result.getQuantity());
        assertEquals(4, existingItem.getQuantity());
        verify(cartItemRepository).save(existingItem);
        verify(cartItemRepository, never()).delete(any(CartItem.class));
    }

    @Test
    void testUpdateCartItemQuantity_NegativeChangeResultingInZero() {
        CartItem existingItem = CartItem.builder()
                .id(1L)
                .cartId(cartId)
                .menuId(menuId)
                .quantity(2)
                .build();

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(cartId, menuId)).thenReturn(Optional.of(existingItem));

        CartItem result = cartService.updateCartItemQuantity(userId, menuId, -2);

        assertNull(result);
        verify(cartItemRepository).delete(existingItem);
        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    void testUpdateCartItemQuantity_NegativeChangeResultingInNegative() {
        CartItem existingItem = CartItem.builder()
                .id(1L)
                .cartId(cartId)
                .menuId(menuId)
                .quantity(1)
                .build();

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(cartId, menuId)).thenReturn(Optional.of(existingItem));

        CartItem result = cartService.updateCartItemQuantity(userId, menuId, -3);

        assertNull(result);
        verify(cartItemRepository).delete(existingItem);
        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    void testUpdateCartItemQuantity_ItemNotFound() {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(cartId, menuId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> cartService.updateCartItemQuantity(userId, menuId, 1));
        assertEquals("Item not found in cart", exception.getMessage());
        verify(cartItemRepository, never()).save(any(CartItem.class));
        verify(cartItemRepository, never()).delete(any(CartItem.class));
    }

    @Test
    void testUpdateCartItemQuantity_FindWithMenuReturnsEmpty() {
        CartItem existingItem = CartItem.builder()
                .id(1L)
                .cartId(cartId)
                .menuId(menuId)
                .quantity(2)
                .build();

        CartItem savedItem = CartItem.builder()
                .id(1L)
                .cartId(cartId)
                .menuId(menuId)
                .quantity(3)
                .build();

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(cartId, menuId)).thenReturn(Optional.of(existingItem));
        when(cartItemRepository.save(existingItem)).thenReturn(savedItem);
        when(cartItemRepository.findByCartIdAndMenuIdWithMenu(cartId, menuId)).thenReturn(Optional.empty());

        CartItem result = cartService.updateCartItemQuantity(userId, menuId, 1);

        assertNotNull(result);
        assertEquals(savedItem, result);
        verify(cartItemRepository).findByCartIdAndMenuIdWithMenu(cartId, menuId);
    }

    @Test
    void testRemoveItemFromCart_Success() {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(cartId, menuId)).thenReturn(Optional.of(cartItem));

        cartService.removeItemFromCart(userId, menuId);

        verify(cartItemRepository).delete(cartItem);
    }

    @Test
    void testRemoveItemFromCart_ItemNotFound() {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(cartId, menuId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> cartService.removeItemFromCart(userId, menuId));
        assertEquals("Item not found in cart", exception.getMessage());
        verify(cartItemRepository, never()).delete(any(CartItem.class));
    }

    @Test
    void testGetCartItems_Success() {
        List<CartItem> cartItems = Arrays.asList(cartItem);
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findAllByCartIdWithMenu(cartId)).thenReturn(cartItems);

        List<CartItem> result = cartService.getCartItems(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(cartItem, result.get(0));
        verify(cartItemRepository).findAllByCartIdWithMenu(cartId);
    }

    @Test
    void testGetCartItems_EmptyCart() {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findAllByCartIdWithMenu(cartId)).thenReturn(new ArrayList<>());

        List<CartItem> result = cartService.getCartItems(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(cartItemRepository).findAllByCartIdWithMenu(cartId);
    }

    @Test
    void testClearCart_WithItems() {
        List<CartItem> items = Arrays.asList(cartItem);
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findAllByCartId(cartId)).thenReturn(items);

        cartService.clearCart(userId);

        verify(cartItemRepository).deleteAll(items);
    }

    @Test
    void testClearCart_EmptyCart() {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findAllByCartId(cartId)).thenReturn(new ArrayList<>());

        cartService.clearCart(userId);

        verify(cartItemRepository, never()).deleteAll(any());
    }

    @Test
    void testClearCart_CartNotFound() {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        cartService.clearCart(userId);

        verify(cartItemRepository, never()).findAllByCartId(anyLong());
        verify(cartItemRepository, never()).deleteAll(any());
    }

    @Test
    void testAddItemToCartAsync_Success() throws ExecutionException, InterruptedException {
        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(cartId, menuId)).thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(cartItemRepository.findByCartIdAndMenuIdWithMenu(cartId, menuId)).thenReturn(Optional.of(cartItem));

        CompletableFuture<CartItem> result = cartService.addItemToCartAsync(userId, menuId);

        assertNotNull(result);
        assertFalse(result.isCompletedExceptionally());
        assertEquals(cartItem, result.get());
    }

    @Test
    void testAddItemToCartAsync_Exception() throws ExecutionException, InterruptedException {
        when(menuRepository.findById(menuId)).thenThrow(new RuntimeException("Database error"));

        CompletableFuture<CartItem> result = cartService.addItemToCartAsync(userId, menuId);

        assertNotNull(result);
        assertTrue(result.isCompletedExceptionally());
        assertThrows(ExecutionException.class, result::get);
    }

    @Test
    void testUpdateCartItemQuantityAsync_Success() throws ExecutionException, InterruptedException {
        CartItem existingItem = CartItem.builder()
                .id(1L)
                .cartId(cartId)
                .menuId(menuId)
                .quantity(2)
                .build();

        CartItem updatedItem = CartItem.builder()
                .id(1L)
                .cartId(cartId)
                .menuId(menuId)
                .quantity(3)
                .menu(menu)
                .build();

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(cartId, menuId)).thenReturn(Optional.of(existingItem));
        when(cartItemRepository.save(existingItem)).thenReturn(updatedItem);
        when(cartItemRepository.findByCartIdAndMenuIdWithMenu(cartId, menuId)).thenReturn(Optional.of(updatedItem));

        CompletableFuture<CartItem> result = cartService.updateCartItemQuantityAsync(userId, menuId, 1);

        assertNotNull(result);
        assertFalse(result.isCompletedExceptionally());
        assertEquals(updatedItem, result.get());
    }

    @Test
    void testUpdateCartItemQuantityAsync_Exception() throws ExecutionException, InterruptedException {
        when(cartRepository.findByUserId(userId)).thenThrow(new RuntimeException("Database error"));

        CompletableFuture<CartItem> result = cartService.updateCartItemQuantityAsync(userId, menuId, 1);

        assertNotNull(result);
        assertTrue(result.isCompletedExceptionally());
        assertThrows(ExecutionException.class, result::get);
    }

    @Test
    void testRemoveItemFromCartAsync_Success() throws ExecutionException, InterruptedException {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(cartId, menuId)).thenReturn(Optional.of(cartItem));

        CompletableFuture<Void> result = cartService.removeItemFromCartAsync(userId, menuId);

        assertNotNull(result);
        assertFalse(result.isCompletedExceptionally());
        assertNull(result.get());
        verify(cartItemRepository).delete(cartItem);
    }

    @Test
    void testRemoveItemFromCartAsync_Exception() throws ExecutionException, InterruptedException {
        when(cartRepository.findByUserId(userId)).thenThrow(new RuntimeException("Database error"));

        CompletableFuture<Void> result = cartService.removeItemFromCartAsync(userId, menuId);

        assertNotNull(result);
        assertTrue(result.isCompletedExceptionally());
        assertThrows(ExecutionException.class, result::get);
    }

    @Test
    void testGetCartItemsAsync_Success() throws ExecutionException, InterruptedException {

        List<CartItem> cartItems = Arrays.asList(cartItem);
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findAllByCartIdWithMenu(cartId)).thenReturn(cartItems);

        CompletableFuture<List<CartItem>> result = cartService.getCartItemsAsync(userId);

        assertNotNull(result);
        assertFalse(result.isCompletedExceptionally());
        assertEquals(cartItems, result.get());
    }

    @Test
    void testGetCartItemsAsync_Exception() throws ExecutionException, InterruptedException {
        when(cartRepository.findByUserId(userId)).thenThrow(new RuntimeException("Database error"));
        CompletableFuture<List<CartItem>> result = cartService.getCartItemsAsync(userId);

        assertNotNull(result);
        assertTrue(result.isCompletedExceptionally());
        assertThrows(ExecutionException.class, result::get);
    }

    @Test
    void testClearCartAsync_Success() throws ExecutionException, InterruptedException {
        List<CartItem> items = Arrays.asList(cartItem);
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findAllByCartId(cartId)).thenReturn(items);
        CompletableFuture<Void> result = cartService.clearCartAsync(userId);

        assertNotNull(result);
        assertFalse(result.isCompletedExceptionally());
        assertNull(result.get());
        verify(cartItemRepository).deleteAll(items);
    }

    @Test
    void testClearCartAsync_Exception() throws ExecutionException, InterruptedException {
        when(cartRepository.findByUserId(userId)).thenThrow(new RuntimeException("Database error"));

        CompletableFuture<Void> result = cartService.clearCartAsync(userId);

        assertNotNull(result);
        assertTrue(result.isCompletedExceptionally());
        assertThrows(ExecutionException.class, result::get);
    }
}