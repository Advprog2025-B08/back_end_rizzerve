package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class CartItemTest {

    private CartItem cartItem;
    private Cart cart;
    private Menu menu;

    @BeforeEach
    void setUp() {
        cart = new Cart();
        cart.setId(1L);
        cart.setUserId(1L);

        menu = new Menu();
        menu.setId(1L);
        menu.setName("Test Menu");
        menu.setPrice(10000L);

        cartItem = CartItem.builder()
                .id(1L)
                .quantity(2)
                .cartId(cart.getId())
                .menuId(menu.getId())
                .cart(cart)
                .menu(menu)
                .build();
    }

    @Test
    void testCartItemCreation() {
        assertNotNull(cartItem);
        assertEquals(1L, cartItem.getId());
        assertEquals(2, cartItem.getQuantity());
        assertEquals(1L, cartItem.getCartId());
        assertEquals(1L, cartItem.getMenuId());
    }

    @Test
    void testCartItemRelationships() {
        assertEquals(cart, cartItem.getCart());
        assertEquals(menu, cartItem.getMenu());
        assertEquals(cart.getId(), cartItem.getCartId());
        assertEquals(menu.getId(), cartItem.getMenuId());
    }

    @Test
    void testCartItemLombokAnnotations() {
        CartItem newItem = new CartItem();
        newItem.setId(2L);
        newItem.setQuantity(3);
        newItem.setCartId(2L);
        newItem.setMenuId(2L);
        newItem.setCart(cart);
        newItem.setMenu(menu);

        assertEquals(2L, newItem.getId());
        assertEquals(3, newItem.getQuantity());
        assertEquals(2L, newItem.getCartId());
        assertEquals(2L, newItem.getMenuId());
        assertEquals(cart, newItem.getCart());
        assertEquals(menu, newItem.getMenu());
    }

    @Test
    void testCartItemBuilder() {
        CartItem builtItem = CartItem.builder()
                .id(3L)
                .quantity(4)
                .cartId(3L)
                .menuId(3L)
                .cart(cart)
                .menu(menu)
                .build();

        assertEquals(3L, builtItem.getId());
        assertEquals(4, builtItem.getQuantity());
        assertEquals(3L, builtItem.getCartId());
        assertEquals(3L, builtItem.getMenuId());
        assertEquals(cart, builtItem.getCart());
        assertEquals(menu, builtItem.getMenu());
    }

    @Test
    void testJsonIgnoreAnnotation() throws NoSuchFieldException {
        Field cartField = CartItem.class.getDeclaredField("cart");
        JsonIgnore jsonIgnore = cartField.getAnnotation(JsonIgnore.class);
        assertNotNull(jsonIgnore, "@JsonIgnore annotation should be present on the cart field");
    }
}