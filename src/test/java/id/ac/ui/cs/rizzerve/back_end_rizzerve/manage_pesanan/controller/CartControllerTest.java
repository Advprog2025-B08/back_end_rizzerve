// CartControllerTest.java
package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.controller;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    private MockMvc mockMvc;

    private Long userId = 1L;
    private Long menuId = 1L;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }

    @Test
    void testAddMenuToCart() throws Exception {
        doNothing().when(cartService).addMenuToCart(userId, menuId);

        mockMvc.perform(post("/api/cart/{userId}/add/{menuId}", userId, menuId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(cartService, times(1)).addMenuToCart(userId, menuId);
    }

    @Test
    void testUpdateCartItemQuantity() throws Exception {
        int quantityChange = 1;
        doNothing().when(cartService).updateCartItemQuantity(userId, menuId, quantityChange);

        mockMvc.perform(put("/api/cart/{userId}/update/{menuId}", userId, menuId)
                        .param("quantityChange", String.valueOf(quantityChange))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(cartService, times(1)).updateCartItemQuantity(userId, menuId, quantityChange);
    }

    @Test
    void testRemoveMenuFromCart() throws Exception {
        doNothing().when(cartService).removeMenuFromCart(userId, menuId);

        mockMvc.perform(delete("/api/cart/{userId}/remove/{menuId}", userId, menuId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(cartService, times(1)).removeMenuFromCart(userId, menuId);
    }

    @Test
    void testClearCart() throws Exception {
        doNothing().when(cartService).clearCart(userId);

        mockMvc.perform(delete("/api/cart/{userId}/clear", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(cartService, times(1)).clearCart(userId);
    }

    @Test
    void testGetCartItems() throws Exception {
        List<CartItem> cartItems = Arrays.asList(
                CartItem.builder().id(1L).menuId(1L).quantity(2).build(),
                CartItem.builder().id(2L).menuId(2L).quantity(1).build()
        );

        when(cartService.getCartItems(userId)).thenReturn(cartItems);

        mockMvc.perform(get("/api/cart/{userId}/items", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].menuId").value(1))
                .andExpect(jsonPath("$[0].quantity").value(2))
                .andExpect(jsonPath("$[1].menuId").value(2))
                .andExpect(jsonPath("$[1].quantity").value(1));

        verify(cartService, times(1)).getCartItems(userId);
    }
}