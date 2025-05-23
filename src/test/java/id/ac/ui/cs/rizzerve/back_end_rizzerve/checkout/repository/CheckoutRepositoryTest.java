package id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.repository;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.model.Checkout;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.repository.UserRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
class CheckoutRepositoryTest {
    private Cart cart;
    private User user;
    private User user2;
    private Cart cart2;

    @Autowired
    private CheckoutRepository checkoutRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("Test User");
        user.setPassword("<PASSWORD>");
        user.setRole("USER");
        user = userRepository.save(user);

        user2 = new User();
        user2.setUsername("Test User 2");
        user2.setPassword("<PASSWORD>");
        user2.setRole("USER");
        user2 = userRepository.save(user2);

        cart = Cart.builder().user(user).build();
        cart2 = Cart.builder().user(user2).build();

        cart = cartRepository.save(cart);
        cart2 = cartRepository.save(cart2);
    }

    @Test
    void testSaveAndFindById() {
        Checkout checkout = Checkout.builder()
                .cart(cart)
                .user(user)
                .isSubmitted(false)
                .totalPrice(100)
                .createdAt(LocalDateTime.now())
                .build();

        Checkout saved = checkoutRepository.save(checkout);

        Optional<Checkout> found = checkoutRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getUser().getId()).isEqualTo(user.getId());
        assertThat(found.get().getTotalPrice()).isEqualTo(100);
    }

    @Test
    void testDeleteById() {
        Checkout checkout = Checkout.builder()
                .user(user2)
                .cart(cart2)
                .totalPrice(50)
                .isSubmitted(false)
                .createdAt(LocalDateTime.now())
                .build();

        Checkout saved = checkoutRepository.save(checkout);
        Long id = saved.getId();

        checkoutRepository.deleteById(id);
        Optional<Checkout> deleted = checkoutRepository.findById(id);
        assertThat(deleted).isEmpty();
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Checkout> found = checkoutRepository.findById(999L);
        assertThat(found).isEmpty();
    }

    @Test
    void testSaveWithNullUserShouldFail() {
        Checkout checkout = Checkout.builder()
                .cart(cart)
                .user(null)
                .isSubmitted(false)
                .totalPrice(100)
                .createdAt(LocalDateTime.now())
                .build();

        assertThatThrownBy(() -> checkoutRepository.save(checkout))
                .isInstanceOf(Exception.class);
    }

}
