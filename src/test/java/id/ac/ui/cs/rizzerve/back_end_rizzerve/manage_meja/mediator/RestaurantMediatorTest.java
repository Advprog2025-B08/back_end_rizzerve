package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.mediator;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.repository.UserRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestaurantMediatorTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private RestaurantMediator restaurantMediator;

    private User testUser;
    private Cart testCart;

    @BeforeEach
    void setUp() {
        // Create test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
        testUser.setRole("USER");

        // Create test cart
        testCart = Cart.builder()
                .id(1L)
                .userId(testUser.getId())
                .user(testUser)
                .items(new ArrayList<>())
                .build();
    }


    @Test
    void testConstructorInitialization() {
        UserRepository mockUserRepo = mock(UserRepository.class);
        CartRepository mockCartRepo = mock(CartRepository.class);

        RestaurantMediator mediator = new RestaurantMediator(mockUserRepo, mockCartRepo);

        assertNotNull(mediator);

        when(mockUserRepo.findByUsername("test")).thenReturn(Optional.empty());

        User result = mediator.findUserByUsername("test");
        assertNull(result);

        verify(mockUserRepo, times(1)).findByUsername("test");
    }


    @Test
    void testFindUserByUsername_UserExists() {
        String username = "testuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(testUser));

        User result = restaurantMediator.findUserByUsername(username);

        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testUser.getRole(), result.getRole());

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testFindUserByUsername_UserNotExists() {
        String username = "nonexistentuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        User result = restaurantMediator.findUserByUsername(username);

        assertNull(result);

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testFindUserByUsername_NullUsername() {
        when(userRepository.findByUsername(null)).thenReturn(Optional.empty());

        User result = restaurantMediator.findUserByUsername(null);

        assertNull(result);

        verify(userRepository, times(1)).findByUsername(null);
    }

    @Test
    void testFindUserByUsername_EmptyUsername() {
        String emptyUsername = "";
        when(userRepository.findByUsername(emptyUsername)).thenReturn(Optional.empty());

        User result = restaurantMediator.findUserByUsername(emptyUsername);

        assertNull(result);

        verify(userRepository, times(1)).findByUsername(emptyUsername);
    }


    @Test
    void testGetOrCreateCartForUser_CartExists() {
        when(cartRepository.findByUserId(testUser.getId())).thenReturn(Optional.of(testCart));

        Cart result = restaurantMediator.getOrCreateCartForUser(testUser);

        assertNotNull(result);
        assertEquals(testCart.getId(), result.getId());
        assertEquals(testCart.getUserId(), result.getUserId());
        assertEquals(testCart.getUser().getUsername(), result.getUser().getUsername());

        verify(cartRepository, times(1)).findByUserId(testUser.getId());
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void testGetOrCreateCartForUser_CartNotExists() {
        when(cartRepository.findByUserId(testUser.getId())).thenReturn(Optional.empty());

        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> {
            Cart cartToSave = invocation.getArgument(0);
            cartToSave.setId(2L);
            return cartToSave;
        });

        Cart result = restaurantMediator.getOrCreateCartForUser(testUser);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals(testUser.getId(), result.getUserId());
        assertEquals(testUser, result.getUser());
        assertNotNull(result.getItems());
        assertTrue(result.getItems().isEmpty());

        verify(cartRepository, times(1)).findByUserId(testUser.getId());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void testGetOrCreateCartForUser_CreateCartProperties() {
        when(cartRepository.findByUserId(testUser.getId())).thenReturn(Optional.empty());

        Cart savedCart = null;
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> {
            Cart cartToSave = invocation.getArgument(0);
            assertEquals(testUser.getId(), cartToSave.getUserId());
            assertEquals(testUser, cartToSave.getUser());
            assertNotNull(cartToSave.getItems());
            assertTrue(cartToSave.getItems() instanceof ArrayList);

            cartToSave.setId(3L);
            return cartToSave;
        });

        Cart result = restaurantMediator.getOrCreateCartForUser(testUser);

        assertNotNull(result);
        assertEquals(testUser.getId(), result.getUserId());
        assertEquals(testUser, result.getUser());
        assertNotNull(result.getItems());
        assertTrue(result.getItems().isEmpty());

        verify(cartRepository, times(1)).findByUserId(testUser.getId());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void testGetOrCreateCartForUser_DifferentUsers() {
        User user1 = new User();
        user1.setId(10L);
        user1.setUsername("user1");

        User user2 = new User();
        user2.setId(20L);
        user2.setUsername("user2");

        when(cartRepository.findByUserId(10L)).thenReturn(Optional.empty());
        when(cartRepository.findByUserId(20L)).thenReturn(Optional.empty());

        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> {
            Cart cart = invocation.getArgument(0);
            cart.setId(100L + cart.getUserId()); // Unique ID based on user ID
            return cart;
        });

        // Execute
        Cart cart1 = restaurantMediator.getOrCreateCartForUser(user1);
        Cart cart2 = restaurantMediator.getOrCreateCartForUser(user2);

        // Verify
        assertNotNull(cart1);
        assertNotNull(cart2);
        assertEquals(110L, cart1.getId()); // 100 + 10
        assertEquals(120L, cart2.getId()); // 100 + 20
        assertEquals(user1.getId(), cart1.getUserId());
        assertEquals(user2.getId(), cart2.getUserId());

        verify(cartRepository, times(1)).findByUserId(10L);
        verify(cartRepository, times(1)).findByUserId(20L);
        verify(cartRepository, times(2)).save(any(Cart.class));
    }


    @Test
    void testFindUserAndCreateCart_Integration() {

        String username = "integrationuser";
        User foundUser = new User();
        foundUser.setId(99L);
        foundUser.setUsername(username);
        foundUser.setRole("USER");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(foundUser));
        when(cartRepository.findByUserId(foundUser.getId())).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> {
            Cart cart = invocation.getArgument(0);
            cart.setId(999L);
            return cart;
        });

        User user = restaurantMediator.findUserByUsername(username);
        assertNotNull(user);

        Cart cart = restaurantMediator.getOrCreateCartForUser(user);

        assertNotNull(cart);
        assertEquals(999L, cart.getId());
        assertEquals(foundUser.getId(), cart.getUserId());
        assertEquals(foundUser, cart.getUser());

        verify(userRepository, times(1)).findByUsername(username);
        verify(cartRepository, times(1)).findByUserId(foundUser.getId());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void testUserNotFoundThenNoCartOperations() {

        String username = "notfounduser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        User user = restaurantMediator.findUserByUsername(username);

        assertNull(user);

        verify(userRepository, times(1)).findByUsername(username);
        verifyNoInteractions(cartRepository);
    }


    @Test
    void testGetOrCreateCartForUser_RepositoryException() {
        when(cartRepository.findByUserId(testUser.getId())).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> {
            restaurantMediator.getOrCreateCartForUser(testUser);
        });

        verify(cartRepository, times(1)).findByUserId(testUser.getId());
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void testFindUserByUsername_RepositoryException() {
        String username = "erroruser";
        when(userRepository.findByUsername(username)).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> {
            restaurantMediator.findUserByUsername(username);
        });

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testGetOrCreateCartForUser_SaveException() {
        when(cartRepository.findByUserId(testUser.getId())).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenThrow(new RuntimeException("Save error"));

        assertThrows(RuntimeException.class, () -> {
            restaurantMediator.getOrCreateCartForUser(testUser);
        });

        verify(cartRepository, times(1)).findByUserId(testUser.getId());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }


    @Test
    void testMethodCallsWithCorrectParameters() {
        String specificUsername = "specificuser";
        Long specificUserId = 42L;

        User specificUser = new User();
        specificUser.setId(specificUserId);
        specificUser.setUsername(specificUsername);

        when(userRepository.findByUsername(specificUsername)).thenReturn(Optional.of(specificUser));
        when(cartRepository.findByUserId(specificUserId)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        User foundUser = restaurantMediator.findUserByUsername(specificUsername);
        Cart createdCart = restaurantMediator.getOrCreateCartForUser(foundUser);

        verify(userRepository, times(1)).findByUsername(eq(specificUsername));
        verify(cartRepository, times(1)).findByUserId(eq(specificUserId));
        verify(cartRepository, times(1)).save(argThat(cart ->
                cart.getUserId().equals(specificUserId) &&
                        cart.getUser().equals(specificUser) &&
                        cart.getItems() != null &&
                        cart.getItems().isEmpty()
        ));
    }
}