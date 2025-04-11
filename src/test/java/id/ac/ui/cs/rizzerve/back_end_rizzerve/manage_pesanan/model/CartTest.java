// CartTest.java
package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    @Test
    void testCartBuilder() {
        Long userId = 1L;
        List<CartItem> items = new ArrayList<>();
        items.add(CartItem.builder().id(1L).menuId(1L).quantity(2).build());

        Cart cart = Cart.builder()
                .id(1L)
                .userId(userId)
                .items(items)
                .build();

        assertEquals(1L, cart.getId());
        assertEquals(userId, cart.getUserId());
        assertEquals(1, cart.getItems().size());
        assertEquals(1L, cart.getItems().get(0).getMenuId());
        assertEquals(2, cart.getItems().get(0).getQuantity());
    }

    @Test
    void testCartSettersAndGetters() {
        Cart cart = new Cart();
        Long userId = 1L;
        List<CartItem> items = new ArrayList<>();
        items.add(CartItem.builder().id(1L).menuId(1L).quantity(2).build());

        cart.setId(1L);
        cart.setUserId(userId);
        cart.setItems(items);

        assertEquals(1L, cart.getId());
        assertEquals(userId, cart.getUserId());
        assertEquals(1, cart.getItems().size());
    }
}