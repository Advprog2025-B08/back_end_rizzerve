package id.ac.ui.cs.rizzerve.back_end_rizzerve.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartItemTest {
    private CartItem cartItem;
    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .name("Test Product")
                .build();

        cartItem = CartItem.builder()
                .id(1L)
                .product(product)
                .quantity(2)
                .build();
    }

    @Test
    void testGetId() {
        assertEquals(1L, cartItem.getId());
    }

    @Test
    void testGetProduct() {
        assertEquals(product, cartItem.getProduct());
        assertEquals("Test Product", cartItem.getProduct().getName());
        assertEquals(1L, cartItem.getProduct().getId());
    }

    @Test
    void testGetQuantity() {
        assertEquals(2, cartItem.getQuantity());
    }

    @Test
    void testSetId() {
        cartItem.setId(2L);
        assertEquals(2L, cartItem.getId());
    }

    @Test
    void testSetProduct() {
        Product newProduct = Product.builder()
                .id(2L)
                .name("New Product")
                .build();

        cartItem.setProduct(newProduct);

        assertEquals(newProduct, cartItem.getProduct());
        assertEquals("New Product", cartItem.getProduct().getName());
        assertEquals(2L, cartItem.getProduct().getId());
    }

    @Test
    void testSetQuantity() {
        cartItem.setQuantity(5);
        assertEquals(5, cartItem.getQuantity());
    }

    @Test
    void testEqualsAndHashCode() {
        CartItem sameCartItem = CartItem.builder()
                .id(1L)
                .product(product)
                .quantity(2)
                .build();

        CartItem differentId = CartItem.builder()
                .id(2L)
                .product(product)
                .quantity(2)
                .build();

        CartItem differentQuantity = CartItem.builder()
                .id(1L)
                .product(product)
                .quantity(3)
                .build();

        Product differentProduct = Product.builder()
                .id(2L)
                .name("Different Product")
                .build();

        CartItem differentProductCartItem = CartItem.builder()
                .id(1L)
                .product(differentProduct)
                .quantity(2)
                .build();

        // Test equals
        assertEquals(cartItem, cartItem); // Same instance
        assertEquals(cartItem, sameCartItem); // Same values
        assertNotEquals(cartItem, differentId); // Different id
        assertNotEquals(cartItem, differentQuantity); // Different quantity
        assertNotEquals(cartItem, differentProductCartItem); // Different product
        assertNotEquals(cartItem, null); // Null comparison
        assertNotEquals(cartItem, new Object()); // Different type

        // Test hashCode
        assertEquals(cartItem.hashCode(), sameCartItem.hashCode());
    }

    @Test
    void testBuilder() {
        CartItem builtCartItem = CartItem.builder()
                .id(3L)
                .product(product)
                .quantity(4)
                .build();

        assertEquals(3L, builtCartItem.getId());
        assertEquals(product, builtCartItem.getProduct());
        assertEquals(4, builtCartItem.getQuantity());
    }

    @Test
    void testNoArgsConstructor() {
        CartItem emptyCartItem = new CartItem();

        assertNull(emptyCartItem.getId());
        assertNull(emptyCartItem.getProduct());
        assertEquals(0, emptyCartItem.getQuantity());
    }

    @Test
    void testAllArgsConstructor() {
        CartItem constructedCartItem = new CartItem(3L, product, 4);

        assertEquals(3L, constructedCartItem.getId());
        assertEquals(product, constructedCartItem.getProduct());
        assertEquals(4, constructedCartItem.getQuantity());
    }

    @Test
    void testToString() {
        String cartItemString = cartItem.toString();

        assertTrue(cartItemString.contains("id=1"));
        assertTrue(cartItemString.contains("quantity=2"));
        assertTrue(cartItemString.contains("CartItem"));
    }
}