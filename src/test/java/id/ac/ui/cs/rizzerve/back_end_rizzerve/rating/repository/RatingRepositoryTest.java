package id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.repository;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.repository.UserRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.repository.MenuRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.model.Rating;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class RatingRepositoryTest {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MenuRepository menuRepository;

    private User user;
    private Menu menu;

    @BeforeEach
    void setUp() {
        // Setup default user and menu for basic tests
        user = new User();
        user.setUsername("Daniel");
        user.setPassword("pass");
        user.setRole("CUSTOMER");
        user = userRepository.save(user);

        menu = new Menu();
        menu.setName("Nasi Goreng");
        menu.setDescription("Lezat");
        menu.setUrl("/nasi-goreng");
        menu.setIcon("icon-nasi-goreng.png");
        menu.setDisplayOrder(1);
        menu.setIsActive(true);
        menu.setCreatedAt(LocalDateTime.now());
        menu.setUpdatedAt(LocalDateTime.now());
        menu = menuRepository.save(menu);
    }

    @Test
    void testSaveAndFindById() {
        Rating rating = new Rating.RatingBuilder()
                .setUser(user)
                .setMenu(menu)
                .setRatingValue(4)
                .build();

        Rating saved = ratingRepository.save(rating);

        assertThat(saved.getId()).isNotNull();
        Optional<Rating> found = ratingRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getRatingValue()).isEqualTo(4);
    }

    @Test
    void testUpdateRating() {
        Rating rating = new Rating.RatingBuilder()
                .setUser(user)
                .setMenu(menu)
                .setRatingValue(4)
                .build();

        Rating saved = ratingRepository.save(rating);

        saved.setRatingValue(5);
        Rating updated = ratingRepository.save(saved);

        Optional<Rating> found = ratingRepository.findById(updated.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getRatingValue()).isEqualTo(5);
    }

    @Test
    void testDeleteRating() {
        Rating rating = new Rating.RatingBuilder()
                .setUser(user)
                .setMenu(menu)
                .setRatingValue(4)
                .build();

        Rating saved = ratingRepository.save(rating);
        ratingRepository.deleteById(saved.getId());

        Optional<Rating> found = ratingRepository.findById(saved.getId());
        assertThat(found).isEmpty();
    }

    @Test
    void testFindAllByMenuId_ShouldReturnCorrectRatings() {
        Menu menu1 = new Menu();
        menu1.setName("Nasi Goreng");
        menu1.setDescription("Lezat");
        menu1.setUrl("/nasi-goreng");
        menu1.setIcon("icon-nasi-goreng.png");
        menu1.setDisplayOrder(1);
        menu1.setIsActive(true);
        menu1.setCreatedAt(LocalDateTime.now());
        menu1.setUpdatedAt(LocalDateTime.now());
        menu1 = menuRepository.save(menu1);

        Menu menu2 = new Menu();
        menu2.setName("Nasi Padang");
        menu2.setDescription("Pedas dan Nikmat");
        menu2.setUrl("/nasi-padang");
        menu2.setIcon("icon-nasi-padang.png");
        menu2.setDisplayOrder(2);
        menu2.setIsActive(true);
        menu2.setCreatedAt(LocalDateTime.now());
        menu2.setUpdatedAt(LocalDateTime.now());
        menu2 = menuRepository.save(menu2);

        User user1 = new User();
        user1.setUsername("Daniel1");
        user1.setPassword("pass");
        user1.setRole("CUSTOMER");
        user1 = userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("Angger");
        user2.setPassword("pass");
        user2.setRole("CUSTOMER");
        user2 = userRepository.save(user2);

        User user3 = new User();
        user3.setUsername("Dewandaru");
        user3.setPassword("pass");
        user3.setRole("CUSTOMER");
        user3 = userRepository.save(user3);

        Rating r1 = new Rating.RatingBuilder()
                .setUser(user1)
                .setMenu(menu1)
                .setRatingValue(4)
                .build();

        Rating r2 = new Rating.RatingBuilder()
                .setUser(user2)
                .setMenu(menu1)
                .setRatingValue(5)
                .build();

        Rating r3 = new Rating.RatingBuilder()
                .setUser(user3)
                .setMenu(menu2)
                .setRatingValue(3)
                .build();

        ratingRepository.saveAll(List.of(r1, r2, r3));

        List<Rating> result = ratingRepository.findAllByMenuId(menu1.getId());

        assertThat(result).hasSize(2)
                .contains(r1, r2);
    }

    @Test
    void testFindAllByMenuId_ShouldReturnEmptyList_WhenNoRatingFound() {
        List<Rating> result = ratingRepository.findAllByMenuId(999L);
        assertThat(result).isEmpty();
    }
}