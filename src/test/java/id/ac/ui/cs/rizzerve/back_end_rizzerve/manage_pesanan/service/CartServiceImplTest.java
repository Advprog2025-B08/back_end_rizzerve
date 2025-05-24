package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository.CartItemRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository.CartRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.repository.MenuRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    private Cart cart;
    private CartItem cartItem;
    private Menu menu;

    @BeforeEach
    void setUp() {
        userId = 1L;
        menuId = 1L;

        cart = Cart.builder()
                .id(1L)
                .userId(userId)
                .items(new ArrayList<>())
                .build();

        menu = new Menu();
        menu.setId(menuId);
        menu.setName("Test Menu");

        cartItem = CartItem.builder()
                .id(1L)
                .cartId(cart.getId())
                .menuId(menuId)
                .quantity(1)
                .cart(cart)
                .menu(menu)
                .build();
    }

    @Test
    void testGetOrCreateCartWhenCartExists() {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        Cart result = cartService.getOrCreateCart(userId);

        assertNotNull(result);
        assertEquals(cart.getId(), result.getId());
        assertEquals(cart.getUserId(), result.getUserId());

        verify(cartRepository).findByUserId(userId);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void testGetOrCreateCartWhenCartDoesNotExist() {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.getOrCreateCart(userId);

        assertNotNull(result);
        assertEquals(cart.getId(), result.getId());
        assertEquals(cart.getUserId(), result.getUserId());

        verify(cartRepository).findByUserId(userId);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void testAddItemToCartForNewItem() {
        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(cart.getId(), menuId)).thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        CartItem result = cartService.addItemToCart(userId, menuId);

        assertNotNull(result);
        assertEquals(cartItem.getId(), result.getId());
        assertEquals(cartItem.getCartId(), result.getCartId());
        assertEquals(cartItem.getMenuId(), result.getMenuId());
        assertEquals(cartItem.getQuantity(), result.getQuantity());

        verify(menuRepository).findById(menuId);
        verify(cartRepository).findByUserId(userId);
        verify(cartItemRepository).findByCartIdAndMenuId(cart.getId(), menuId);
        verify(cartItemRepository).save(any(CartItem.class));
    }

    @Test
    void testAddItemToCartForExistingItem() {
        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(cart.getId(), menuId)).thenReturn(Optional.of(cartItem));

        CartItem updatedItem = CartItem.builder()
                .id(cartItem.getId())
                .cartId(cartItem.getCartId())
                .menuId(cartItem.getMenuId())
                .quantity(cartItem.getQuantity() + 1)
                .cart(cart)
                .menu(menu)
                .build();

        when(cartItemRepository.save(any(CartItem.class))).thenReturn(updatedItem);

        CartItem result = cartService.addItemToCart(userId, menuId);

        assertNotNull(result);
        assertEquals(updatedItem.getId(), result.getId());
        assertEquals(updatedItem.getQuantity(), result.getQuantity());

        verify(menuRepository).findById(menuId);
        verify(cartRepository).findByUserId(userId);
        verify(cartItemRepository).findByCartIdAndMenuId(cart.getId(), menuId);
        verify(cartItemRepository).save(any(CartItem.class));
    }

    @Test
    void testAddItemToCartWithInvalidMenu() {
        when(menuRepository.findById(menuId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            cartService.addItemToCart(userId, menuId);
        });

        verify(menuRepository).findById(menuId);
        verify(cartRepository, never()).findByUserId(anyLong());
        verify(cartItemRepository, never()).findByCartIdAndMenuId(anyLong(), anyLong());
        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    void testUpdateCartItemQuantityIncrease() {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(cart.getId(), menuId)).thenReturn(Optional.of(cartItem));

        CartItem updatedItem = CartItem.builder()
                .id(cartItem.getId())
                .cartId(cartItem.getCartId())
                .menuId(cartItem.getMenuId())
                .quantity(cartItem.getQuantity() + 1)
                .cart(cart)
                .menu(menu)
                .build();

        when(cartItemRepository.save(any(CartItem.class))).thenReturn(updatedItem);

        CartItem result = cartService.updateCartItemQuantity(userId, menuId, 1);

        assertNotNull(result);
        assertEquals(updatedItem.getId(), result.getId());
        assertEquals(updatedItem.getQuantity(), result.getQuantity());

        verify(cartRepository).findByUserId(userId);
        verify(cartItemRepository).findByCartIdAndMenuId(cart.getId(), menuId);
        verify(cartItemRepository).save(any(CartItem.class));
        verify(cartItemRepository, never()).delete(any(CartItem.class));
    }

    @Test
    void testUpdateCartItemQuantityDecreaseToDeletion() {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(cart.getId(), menuId)).thenReturn(Optional.of(cartItem));

        CartItem result = cartService.updateCartItemQuantity(userId, menuId, -1);

        assertNull(result);

        verify(cartRepository).findByUserId(userId);
        verify(cartItemRepository).findByCartIdAndMenuId(cart.getId(), menuId);
        verify(cartItemRepository, never()).save(any(CartItem.class));
        verify(cartItemRepository).delete(any(CartItem.class));
    }

    @Test
    void testUpdateCartItemWithInvalidItem() {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(cart.getId(), menuId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            cartService.updateCartItemQuantity(userId, menuId, 1);
        });

        verify(cartRepository).findByUserId(userId);
        verify(cartItemRepository).findByCartIdAndMenuId(cart.getId(), menuId);
        verify(cartItemRepository, never()).save(any(CartItem.class));
        verify(cartItemRepository, never()).delete(any(CartItem.class));
    }

    @Test
    void testRemoveItemFromCart() {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(cart.getId(), menuId)).thenReturn(Optional.of(cartItem));

        cartService.removeItemFromCart(userId, menuId);

        verify(cartRepository).findByUserId(userId);
        verify(cartItemRepository).findByCartIdAndMenuId(cart.getId(), menuId);
        verify(cartItemRepository).delete(cartItem);
    }

    @Test
    void testRemoveItemFromCartWithInvalidItem() {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(cart.getId(), menuId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            cartService.removeItemFromCart(userId, menuId);
        });

        verify(cartRepository).findByUserId(userId);
        verify(cartItemRepository).findByCartIdAndMenuId(cart.getId(), menuId);
        verify(cartItemRepository, never()).delete(any(CartItem.class));
    }

    @Test
    void testGetCartItems() {
        cart.getItems().add(cartItem);
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        List<CartItem> result = cartService.getCartItems(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(cartItem, result.get(0));

        verify(cartRepository).findByUserId(userId);
    }

    @Test
    void testClearCart() {
        cart.getItems().add(cartItem);
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        cartService.clearCart(userId);

        assertTrue(cart.getItems().isEmpty());

        verify(cartRepository).findByUserId(userId);
        verify(cartRepository).save(cart);
    }
}