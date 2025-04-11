package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.model.User;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.model.Menu;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.model.Meja;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MejaTest {

    private Meja meja;

    @BeforeEach
    void setUp() {
        User dummyUser = new User(1, "dummy@mail.com", "customer");

        Menu menu1 = new Menu("1", "Nasi Goreng", 2);
        Menu menu2 = new Menu("2", "Es Teh Manis", 1);

        Cart cart = new Cart();
        cart.setItems(List.of(menu1, menu2));

        meja = new Meja();
        meja.setId("meja-123");
        meja.setNomor("A1");
        meja.setUser(dummyUser);
        meja.setCart(cart);
    }

    @Test
    void testGetMejaId() {
        assertEquals("meja-123", meja.getId());
    }

    @Test
    void testGetNomorMeja() {
        assertEquals("A1", meja.getNomor());
    }

    @Test
    void testUserAssignedToMeja() {
        assertNotNull(meja.getUser());
        assertEquals("dummy@mail.com", meja.getUser().getEmail());
        assertEquals("customer", meja.getUser().getRole());
    }

    @Test
    void testCartLinkedToMeja() {
        assertNotNull(meja.getCart());
        assertEquals(2, meja.getCart().getItems().size());

        assertEquals("Nasi Goreng", meja.getCart().getItems().get(0).getNama());
        assertEquals(2, meja.getCart().getItems().get(0).getJumlah());

        assertEquals("Es Teh Manis", meja.getCart().getItems().get(1).getNama());
        assertEquals(1, meja.getCart().getItems().get(1).getJumlah());
    }
}
