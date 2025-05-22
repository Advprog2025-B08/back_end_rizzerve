package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.model;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MejaTest {

    private Meja meja;

    @BeforeEach
    void setUp() {
        User dummyUser = new User();
        dummyUser.setUsername("testuser");
        dummyUser.setPassword("password123");
        dummyUser.setRole("USER");

        Menu menu1 = new Menu();
        menu1.setId(101L);
        menu1.setName("Nasi Goreng");
        menu1.setIsActive(true);
        menu1.setDisplayOrder(1);
        Menu menu2 = new Menu();
        menu2.setId(102L);
        menu2.setName("Es Teh Manis");
        menu2.setIsActive(true);
        menu2.setDisplayOrder(2);

        Cart cart = Cart.builder()
                .id(1L)
                .userId(1L)
                .user(dummyUser)
                .items(new ArrayList<>())
                .build();

        CartItem cartItem1 = CartItem.builder()
                .id(1L)
                .menuId(menu1.getId())
                .quantity(2)
                .cartId(1L)
                .build();
        CartItem cartItem2 = CartItem.builder()
                .id(1L)
                .menuId(menu2.getId())
                .quantity(4)
                .cartId(1L)
                .build();
        List<CartItem> items = new ArrayList<>();
        items.add(cartItem1);
        items.add(cartItem2);
        cart.setItems(items);

        meja = new Meja();
        meja.setId(1L);
        meja.setNomor(1);
        meja.setUser(dummyUser);
        meja.setCart(cart);
    }

    @Test
    void testGetMejaId() {
        assertEquals(1, meja.getId());
    }

    @Test
    void testGetNomorMeja() {
        assertEquals(1, meja.getNomor());
    }

    @Test
    void testUserAssignedToMeja() {
        assertNotNull(meja.getUser());
        assertEquals("testuser", meja.getUser().getUsername());
        assertEquals("USER", meja.getUser().getRole());
    }

    @Test
    void testCartLinkedToMeja() {
        assertNotNull(meja.getCart());
        assertEquals(2, meja.getCart().getItems().size());

        assertEquals(2, meja.getCart().getItems().get(0).getQuantity());
        assertEquals(101L, meja.getCart().getItems().get(0).getMenuId());

        assertEquals(4, meja.getCart().getItems().get(1).getQuantity());
        assertEquals(102L, meja.getCart().getItems().get(1).getMenuId());
    }
}
