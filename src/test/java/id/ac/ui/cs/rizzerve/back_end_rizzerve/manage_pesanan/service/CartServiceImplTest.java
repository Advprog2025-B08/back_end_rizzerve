package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository.CartItemRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository.CartRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.repository.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.annotation.AsyncResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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

    private Cart cart;
    private CartItem cartItem;
    private Menu menu;

    @BeforeEach
    void setUp() {
        cart = new Cart();
        cart.setId(1L);
        cart.setUserId(1L);
        cart.setItems(new ArrayList<>());

        menu = new Menu();
        menu.setId(1L);
        menu.setName("Test Menu");

        cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setCartId(1L);
        cartItem.setMenuId(1L);
        cartItem.setQuantity(1);
    }

    @Test
    void testGetOrCreateCart_ExistingCart() {
        when(cartRepository.findByUserId(anyLong())).thenReturn(Optional.of(cart));

        Cart result = cartService.getOrCreateCart(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(cartRepository, times(1)).findByUserId(1L);
        verify(cartRepository, never()).save(any());
    }

    @Test
    void testGetOrCreateCart_NewCart() {
        when(cartRepository.findByUserId(anyLong())).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.getOrCreateCart(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(cartRepository, times(1)).findByUserId(1L);
        verify(cartRepository, times(1)).save(any());
    }

    @Test
    void testAddItemToCart_NewItem() {
        when(cartRepository.findByUserId(anyLong())).thenReturn(Optional.of(cart));
        when(menuRepository.findById(anyLong())).thenReturn(Optional.of(menu));
        when(cartItemRepository.findByCartIdAndMenuId(anyLong(), anyLong())).thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        CartItem result = cartService.addItemToCart(1L, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getMenuId());
        verify(cartItemRepository, times(1)).save(any());
    }

    @Test
    void testUpdateCartItemQuantity_Increase() {
        when(cartRepository.findByUserId(anyLong())).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(anyLong(), anyLong())).thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        CartItem result = cartService.updateCartItemQuantity(1L, 1L, 2);

        assertNotNull(result);
        assertEquals(3, result.getQuantity());
        verify(cartItemRepository, times(1)).save(any());
    }

    @Test
    void testRemoveItemFromCart() {
        when(cartRepository.findByUserId(anyLong())).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndMenuId(anyLong(), anyLong())).thenReturn(Optional.of(cartItem));

        cartService.removeItemFromCart(1L, 1L);

        verify(cartItemRepository, times(1)).delete(any());
    }

    @Test
    void testGetCartItems() {
        List<CartItem> items = new ArrayList<>();
        items.add(cartItem);
        cart.setItems(items);

        when(cartRepository.findByUserId(anyLong())).thenReturn(Optional.of(cart));

        List<CartItem> result = cartService.getCartItems(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getMenuId());
    }

    @Test
    void testClearCart() {
        List<CartItem> items = new ArrayList<>();
        items.add(cartItem);
        cart.setItems(items);

        when(cartRepository.findByUserId(anyLong())).thenReturn(Optional.of(cart));

        cartService.clearCart(1L);

        assertTrue(cart.getItems().isEmpty());
        verify(cartRepository, times(1)).save(cart);
    }
}