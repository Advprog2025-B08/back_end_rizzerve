package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;

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
        menu1.setName("Nasi Goreng");
        menu1.setIsActive(true);
        menu1.setDisplayOrder(1);
        Menu menu2 = new Menu();
        menu2.setName("Es Teh Manis");
        menu2.setIsActive(true);
        menu2.setDisplayOrder(2);

        Cart cart = new Cart();
        cart.setItems(List.of(menu1, menu2));

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

        assertEquals("Nasi Goreng", meja.getCart().getItems().get(0).getName());
        assertEquals(1, meja.getCart().getItems().get(0).getDisplayOrder());

        assertEquals("Es Teh Manis", meja.getCart().getItems().get(1).getName());
        assertEquals(2, meja.getCart().getItems().get(1).getDisplayOrder());
    }
}
