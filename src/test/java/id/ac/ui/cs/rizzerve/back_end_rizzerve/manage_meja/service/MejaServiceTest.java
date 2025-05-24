package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.model.Meja;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.dto.*;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.repository.MejaRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.mediator.RestaurantMediator;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MejaServiceTest {

    @Mock
    private MejaRepository mejaRepository;

    @Mock
    private RestaurantMediator mediator;

    @InjectMocks
    private MejaServiceImpl mejaService;

    @Test
    void testCreateMeja() {
        // Setup
        int nomor = 1;
        when(mejaRepository.checkUnique(nomor)).thenReturn(true);

        Meja newMeja = new Meja();
        newMeja.setNomor(nomor);

        when(mejaRepository.save(any(Meja.class))).thenReturn(newMeja);

        // Execute
        mejaService.createMeja(nomor);

        // Verify
        verify(mejaRepository).checkUnique(nomor);
        verify(mejaRepository).save(any(Meja.class));
    }

    @Test
    void testUpdateMeja() {
        // Setup
        int oldNomor = 1;
        int newNomor = 2;

        Meja existingMeja = new Meja();
        existingMeja.setId(1L);
        existingMeja.setNomor(oldNomor);

        when(mejaRepository.getMejaByNomor(oldNomor)).thenReturn(existingMeja);
        when(mejaRepository.checkUnique(newNomor)).thenReturn(true);
        when(mejaRepository.save(any(Meja.class))).thenReturn(existingMeja);

        // Execute
        mejaService.updateMeja(oldNomor, newNomor);

        // Verify
        verify(mejaRepository).getMejaByNomor(oldNomor);
        verify(mejaRepository).checkUnique(newNomor);
        verify(mejaRepository).save(any(Meja.class));
    }

    @Test
    void testDeleteMeja() {
        // Setup
        int nomor = 1;

        Meja existingMeja = new Meja();
        existingMeja.setId(1L);
        existingMeja.setNomor(nomor);

        when(mejaRepository.getMejaByNomor(nomor)).thenReturn(existingMeja);

        // Execute
        boolean result = mejaService.deleteMeja(nomor);

        // Verify
        assertTrue(result);
        verify(mejaRepository).getMejaByNomor(nomor);
        verify(mejaRepository).delete(existingMeja);
    }

    @Test
    void testGetAllMeja() {
        // Setup
        Meja meja1 = new Meja();
        meja1.setId(1L);
        meja1.setNomor(1);

        Meja meja2 = new Meja();
        meja2.setId(2L);
        meja2.setNomor(2);

        List<Meja> dummyList = List.of(meja1, meja2);
        when(mejaRepository.getAllMejas()).thenReturn(dummyList);

        // Execute
        List<MejaDTO> result = mejaService.getAllMeja();

        // Verify
        assertEquals(2, result.size());
        verify(mejaRepository).getAllMejas();
    }

    @Test
    void testGetMejaByNomor() {
        // Setup
        int nomor = 1;

        Meja meja = new Meja();
        meja.setId(1L);
        meja.setNomor(nomor);

        when(mejaRepository.getMejaByNomor(nomor)).thenReturn(meja);

        // Execute
        MejaDTO result = mejaService.getMejaByNomor(nomor);

        // Verify
        assertEquals(nomor, result.getNomor());
        verify(mejaRepository).getMejaByNomor(nomor);
    }

    @Test
    void testSetUserToMeja() {
        // Setup
        int mejaNum = 1;
        String username = "testuser";

        Meja meja = new Meja();
        meja.setId(1L);
        meja.setNomor(mejaNum);

        User user = new User();
        user.setId(1L);
        user.setUsername(username);

        when(mejaRepository.getMejaByNomor(mejaNum)).thenReturn(meja);
        when(mediator.findUserByUsername(username)).thenReturn(user);
        when(mejaRepository.save(any(Meja.class))).thenReturn(meja);

        // Execute
        Meja result = mejaService.setUserToMeja(mejaNum, username);

        // Verify
        assertNotNull(result);
        verify(mejaRepository).getMejaByNomor(mejaNum);
        verify(mediator).findUserByUsername(username);
        verify(mejaRepository).save(any(Meja.class));
    }

    @Test
    void testRemoveUserFromMeja() {
        // Setup
        int mejaNum = 1;

        Meja meja = new Meja();
        meja.setId(1L);
        meja.setNomor(mejaNum);
        meja.setUser(new User());

        when(mejaRepository.getMejaByNomor(mejaNum)).thenReturn(meja);

        // Execute
        boolean result = mejaService.removeUserFromMeja(mejaNum);

        // Verify
        assertTrue(result);
        verify(mejaRepository).getMejaByNomor(mejaNum);
        verify(mejaRepository).save(any(Meja.class));
        assertNull(meja.getUser());
    }

    @Test
    void testCreateMeja_NomrAlreadyExists() {
        // Setup
        int nomor = 1;
        when(mejaRepository.checkUnique(nomor)).thenReturn(false);

        // Execute
        Meja result = mejaService.createMeja(nomor);

        // Verify
        assertNull(result);
        verify(mejaRepository).checkUnique(nomor);
        verify(mejaRepository, never()).save(any(Meja.class));
    }

    @Test
    void testUpdateMeja_MejaNotFound() {
        // Setup
        int oldNomor = 1;
        int newNomor = 2;

        when(mejaRepository.getMejaByNomor(oldNomor)).thenReturn(null);

        // Execute
        Meja result = mejaService.updateMeja(oldNomor, newNomor);

        // Verify
        assertNull(result);
        verify(mejaRepository).getMejaByNomor(oldNomor);
        verify(mejaRepository, never()).save(any(Meja.class));
    }

    @Test
    void testUpdateMeja_NewNomorExists() {
        // Setup
        int oldNomor = 1;
        int newNomor = 2;

        Meja existingMeja = new Meja();
        existingMeja.setId(1L);
        existingMeja.setNomor(oldNomor);

        when(mejaRepository.getMejaByNomor(oldNomor)).thenReturn(existingMeja);
        when(mejaRepository.checkUnique(newNomor)).thenReturn(false);

        // Execute
        Meja result = mejaService.updateMeja(oldNomor, newNomor);

        // Verify
        assertNull(result);
        verify(mejaRepository).getMejaByNomor(oldNomor);
        verify(mejaRepository).checkUnique(newNomor);
        verify(mejaRepository, never()).save(any(Meja.class));
    }


    @Test
    void testGetMejaByNomor_MejaNotFound() {
        // Setup
        int nomor = 999;
        when(mejaRepository.getMejaByNomor(nomor)).thenReturn(null);

        // Execute
        MejaDTO result = mejaService.getMejaByNomor(nomor);

        // Verify
        assertNull(result);
        verify(mejaRepository).getMejaByNomor(nomor);
    }

    @Test
    void testDeleteMeja_MejaNotFound() {
        // Setup
        int nomor = 999;
        when(mejaRepository.getMejaByNomor(nomor)).thenReturn(null);

        // Execute
        boolean result = mejaService.deleteMeja(nomor);

        // Verify
        assertFalse(result);
        verify(mejaRepository).getMejaByNomor(nomor);
        verify(mejaRepository, never()).delete(any(Meja.class));
    }

    @Test
    void testSetUserToMeja_MejaNotFound() {
        // Setup
        int mejaNum = 999;
        String username = "testuser";
        when(mejaRepository.getMejaByNomor(mejaNum)).thenReturn(null);

        // Execute
        Meja result = mejaService.setUserToMeja(mejaNum, username);

        // Verify
        assertNull(result);
        verify(mejaRepository).getMejaByNomor(mejaNum);
        verify(mediator, never()).findUserByUsername(username);
    }

    @Test
    void testSetUserToMeja_MejaAlreadyOccupied() {
        // Setup
        int mejaNum = 1;
        String username = "testuser";

        Meja meja = new Meja();
        meja.setId(1L);
        meja.setNomor(mejaNum);
        meja.setUser(new User());

        when(mejaRepository.getMejaByNomor(mejaNum)).thenReturn(meja);

        // Execute
        Meja result = mejaService.setUserToMeja(mejaNum, username);

        // Verify
        assertNull(result);
        verify(mejaRepository).getMejaByNomor(mejaNum);
        verify(mediator, never()).findUserByUsername(username);
    }

    @Test
    void testSetUserToMeja_UserNotFound() {
        // Setup
        int mejaNum = 1;
        String username = "nonexistentuser";

        Meja meja = new Meja();
        meja.setId(1L);
        meja.setNomor(mejaNum);

        when(mejaRepository.getMejaByNomor(mejaNum)).thenReturn(meja);
        when(mediator.findUserByUsername(username)).thenReturn(null);

        // Execute
        Meja result = mejaService.setUserToMeja(mejaNum, username);

        // Verify
        assertNull(result);
        verify(mejaRepository).getMejaByNomor(mejaNum);
        verify(mediator).findUserByUsername(username);
        verify(mejaRepository, never()).save(any(Meja.class));
    }

    @Test
    void testSetUserToMeja_WithCartCreation() {
        // Setup
        int mejaNum = 1;
        String username = "testuser";

        Meja meja = new Meja();
        meja.setId(1L);
        meja.setNomor(mejaNum);

        User user = new User();
        user.setId(1L);
        user.setUsername(username);

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUserId(user.getId());

        when(mejaRepository.getMejaByNomor(mejaNum)).thenReturn(meja);
        when(mediator.findUserByUsername(username)).thenReturn(user);
        when(mediator.getOrCreateCartForUser(user)).thenReturn(cart);
        when(mejaRepository.save(any(Meja.class))).thenReturn(meja);

        // Execute
        Meja result = mejaService.setUserToMeja(mejaNum, username);

        // Verify
        assertNotNull(result);
        verify(mejaRepository).getMejaByNomor(mejaNum);
        verify(mediator).findUserByUsername(username);
        verify(mediator).getOrCreateCartForUser(user);
        verify(mejaRepository).save(any(Meja.class));
    }

    @Test
    void testRemoveUserFromMeja_MejaNotFound() {
        // Setup
        int mejaNum = 999;
        when(mejaRepository.getMejaByNomor(mejaNum)).thenReturn(null);

        // Execute
        boolean result = mejaService.removeUserFromMeja(mejaNum);

        // Verify
        assertFalse(result);
        verify(mejaRepository).getMejaByNomor(mejaNum);
        verify(mejaRepository, never()).save(any(Meja.class));
    }

    @Test
    void testConvertToDTO_WithUserAndCart() {
        // Setup
        Meja meja = new Meja();
        meja.setId(1L);
        meja.setNomor(1);

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        meja.setUser(user);

        // Create cart with items
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUserId(user.getId());
        cart.setUser(user);

        // Create menu item
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("Test Menu");

        // Create cart item
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setMenuId(menu.getId());
        cartItem.setMenu(menu);
        cartItem.setQuantity(2);

        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);
        cart.setItems(cartItems);

        meja.setCart(cart);

        when(mejaRepository.getAllMejas()).thenReturn(List.of(meja));

        // Execute
        List<MejaDTO> result = mejaService.getAllMeja();

        // Verify
        assertEquals(1, result.size());
        MejaDTO mejaDTO = result.get(0);
        assertEquals(1L, mejaDTO.getId());
        assertEquals(1, mejaDTO.getNomor());
        assertEquals("testuser", mejaDTO.getUsername());
        assertNotNull(mejaDTO.getCart());
        assertEquals(1, mejaDTO.getCart().getItems().size());
        assertEquals("Test Menu", mejaDTO.getCart().getItems().get(0).getMenuName());
    }

    @Test
    void testConvertToDTO_WithUserNoCart() {
        // Setup
        Meja meja = new Meja();
        meja.setId(1L);
        meja.setNomor(1);

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        meja.setUser(user);
        // No cart set

        when(mejaRepository.getAllMejas()).thenReturn(List.of(meja));

        // Execute
        List<MejaDTO> result = mejaService.getAllMeja();

        // Verify
        assertEquals(1, result.size());
        MejaDTO mejaDTO = result.get(0);
        assertEquals(1L, mejaDTO.getId());
        assertEquals(1, mejaDTO.getNomor());
        assertEquals("testuser", mejaDTO.getUsername());
        assertNull(mejaDTO.getCart());
    }

    @Test
    void testConvertToDTO_NoUser() {
        // Setup
        Meja meja = new Meja();
        meja.setId(1L);
        meja.setNomor(1);
        // No user set

        when(mejaRepository.getAllMejas()).thenReturn(List.of(meja));

        // Execute
        List<MejaDTO> result = mejaService.getAllMeja();

        // Verify
        assertEquals(1, result.size());
        MejaDTO mejaDTO = result.get(0);
        assertEquals(1L, mejaDTO.getId());
        assertEquals(1, mejaDTO.getNomor());
        assertNull(mejaDTO.getUsername());
        assertNull(mejaDTO.getCart());
    }

    @Test
    void testConvertToDTO_CartWithNullMenuName() {
        // Setup
        Meja meja = new Meja();
        meja.setId(1L);
        meja.setNomor(1);

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        meja.setUser(user);

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUserId(user.getId());
        cart.setUser(user);

        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setMenuId(1L);
        cartItem.setQuantity(1);
        // No menu set (null)

        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);
        cart.setItems(cartItems);

        meja.setCart(cart);

        when(mejaRepository.getAllMejas()).thenReturn(List.of(meja));

        // Execute
        List<MejaDTO> result = mejaService.getAllMeja();

        // Verify
        assertEquals(1, result.size());
        MejaDTO mejaDTO = result.get(0);
        assertNotNull(mejaDTO.getCart());
        assertEquals(1, mejaDTO.getCart().getItems().size());
        assertNull(mejaDTO.getCart().getItems().get(0).getMenuName());
    }
}