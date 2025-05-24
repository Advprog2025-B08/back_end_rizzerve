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
    @Test
    void testEquals_SameObject() {
        assertTrue(meja.equals(meja));
    }

    @Test
    void testEquals_NullObject() {
        assertFalse(meja.equals(null));
    }

    @Test
    void testEquals_DifferentClass() {
        String notAMeja = "This is not a Meja";
        assertFalse(meja.equals(notAMeja));
    }

    @Test
    void testEquals_SameId() {
        Meja otherMeja = new Meja();
        otherMeja.setId(1L);
        otherMeja.setNomor(999);

        assertTrue(meja.equals(otherMeja));
    }

    @Test
    void testEquals_DifferentId() {
        Meja otherMeja = new Meja();
        otherMeja.setId(2L);
        otherMeja.setNomor(1);

        assertFalse(meja.equals(otherMeja));
    }

    @Test
    void testEquals_BothIdsNull() {
        Meja meja1 = new Meja();
        meja1.setNomor(1);

        Meja meja2 = new Meja();
        meja2.setNomor(2);

        assertTrue(meja1.equals(meja2));
    }

    @Test
    void testEquals_OneIdNull() {
        Meja mejaWithNullId = new Meja();
        mejaWithNullId.setNomor(1);

        assertFalse(meja.equals(mejaWithNullId));
        assertFalse(mejaWithNullId.equals(meja));
    }

    @Test
    void testHashCode_SameId() {
        Meja otherMeja = new Meja();
        otherMeja.setId(1L);

        assertEquals(meja.hashCode(), otherMeja.hashCode());
    }

    @Test
    void testHashCode_DifferentId() {
        Meja otherMeja = new Meja();
        otherMeja.setId(2L);

        assertNotEquals(meja.hashCode(), otherMeja.hashCode());
    }

    @Test
    void testHashCode_NullId() {
        Meja mejaWithNullId = new Meja();

        int hashCode = mejaWithNullId.hashCode();
        assertNotNull(hashCode);
    }

    @Test
    void testHashCode_Consistency() {
        int firstCall = meja.hashCode();
        int secondCall = meja.hashCode();

        assertEquals(firstCall, secondCall);
    }

    @Test
    void testToString_WithUserAndCart() {
        String result = meja.toString();

        assertTrue(result.contains("Meja{"));
        assertTrue(result.contains("id='1'"));
        assertTrue(result.contains("nomor=1"));
        assertTrue(result.contains("hasUser=true"));
        assertTrue(result.contains("hasCart=true"));
    }

    @Test
    void testToString_WithoutUser() {
        meja.setUser(null);

        String result = meja.toString();

        assertTrue(result.contains("Meja{"));
        assertTrue(result.contains("id='1'"));
        assertTrue(result.contains("nomor=1"));
        assertTrue(result.contains("hasUser=false"));
        assertTrue(result.contains("hasCart=false"));
    }

    @Test
    void testToString_WithUserButNoCart() {
        User user = new User();
        user.setUsername("useronly");

        meja.setUser(user);
        meja.setCart(null);

        String result = meja.toString();

        assertTrue(result.contains("Meja{"));
        assertTrue(result.contains("id='1'"));
        assertTrue(result.contains("nomor=1"));
        assertTrue(result.contains("hasUser=true"));
        assertTrue(result.contains("hasCart=false"));
    }

    @Test
    void testToString_NullId() {
        Meja mejaWithNullId = new Meja();
        mejaWithNullId.setNomor(5);

        String result = mejaWithNullId.toString();

        assertTrue(result.contains("Meja{"));
        assertTrue(result.contains("id='null'"));
        assertTrue(result.contains("nomor=5"));
        assertTrue(result.contains("hasUser=false"));
        assertTrue(result.contains("hasCart=false"));
    }


    @Test
    void testCompleteLifecycle() {
        Meja testMeja = new Meja();

        assertNull(testMeja.getId());
        assertNull(testMeja.getNomor());
        assertNull(testMeja.getUser());
        assertNull(testMeja.getCart());

        testMeja.setId(100L);
        testMeja.setNomor(10);

        assertEquals(100L, testMeja.getId());
        assertEquals(10, testMeja.getNomor());

        User user = new User();
        user.setUsername("lifecycle");

        Cart cart = new Cart();
        cart.setId(100L);

        testMeja.setUser(user);
        testMeja.setCart(cart);

        assertNotNull(testMeja.getUser());
        assertNotNull(testMeja.getCart());

        testMeja.validateState();
        assertNotNull(testMeja.getUser());
        assertNotNull(testMeja.getCart());

        testMeja.setUser(null);
        assertNull(testMeja.getUser());
        assertNull(testMeja.getCart());
    }
}
