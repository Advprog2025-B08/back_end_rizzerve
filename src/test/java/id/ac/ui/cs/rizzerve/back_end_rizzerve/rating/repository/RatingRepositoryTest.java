package id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.repository;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.model.Rating;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RatingRepositoryTest {

    private RatingRepository ratingRepository;

    @BeforeEach
    void setUp() {
        ratingRepository = new RatingRepository();
    }

    @Test
    void testSaveAndFindById() {
        // Gunakan model User dan Menu yang sebenarnya
        User user = new User();
        user.setId(1L);
        user.setUsername("Daniel");
        user.setPassword("pass");
        user.setRole("CUSTOMER");

        Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("Nasi Goreng");

        Rating rating = Rating.builder()
                .id(1L)
                .user(user)
                .menu(menu)
                .ratingValue(4)
                .build();

        ratingRepository.save(rating);

        Optional<Rating> found = ratingRepository.findById(1L);

        assertThat(found).isPresent();
        assertThat(found.get().getRatingValue()).isEqualTo(4);
    }

    @Test
    void testUpdateRating() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Daniel");
        user.setPassword("pass");
        user.setRole("CUSTOMER");

        Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("Nasi Goreng");

        Rating rating = Rating.builder()
                .id(1L)
                .user(user)
                .menu(menu)
                .ratingValue(3)
                .build();

        ratingRepository.save(rating);

        Rating updatedRating = Rating.builder()
                .id(1L)
                .user(user)
                .menu(menu)
                .ratingValue(5)
                .build();

        ratingRepository.save(updatedRating);

        Optional<Rating> found = ratingRepository.findById(1L);

        assertThat(found).isPresent();
        assertThat(found.get().getRatingValue()).isEqualTo(5);
    }

    @Test
    void testDeleteRating() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Daniel");
        user.setPassword("pass");
        user.setRole("CUSTOMER");

        Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("Nasi Goreng");

        Rating rating = Rating.builder()
                .id(1L)
                .user(user)
                .menu(menu)
                .ratingValue(4)
                .build();

        ratingRepository.save(rating);
        ratingRepository.deleteById(1L);

        Optional<Rating> found = ratingRepository.findById(1L);

        assertThat(found).isEmpty();
    }

    @Test
    void testFindAllByMenuId_ShouldReturnCorrectRatings() {
        Menu menu1 = new Menu();
        menu1.setId(1L);
        menu1.setName("Nasi Goreng");

        Menu menu2 = new Menu();
        menu2.setId(2L);
        menu2.setName("Nasi Padang");

        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("Daniel");
        user1.setPassword("pass");
        user1.setRole("CUSTOMER");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("Angger");
        user2.setPassword("pass");
        user2.setRole("CUSTOMER");

        User user3 = new User();
        user3.setId(3L);
        user3.setUsername("Dewandaru");
        user3.setPassword("pass");
        user3.setRole("CUSTOMER");

        Rating rating1 = Rating.builder()
                .id(1L)
                .user(user1)
                .menu(menu1)
                .ratingValue(4)
                .build();

        Rating rating2 = Rating.builder()
                .id(2L)
                .user(user2)
                .menu(menu1)
                .ratingValue(5)
                .build();

        Rating rating3 = Rating.builder()
                .id(3L)
                .user(user3)
                .menu(menu2)
                .ratingValue(3)
                .build();

        ratingRepository.save(rating1);
        ratingRepository.save(rating2);
        ratingRepository.save(rating3);

        List<Rating> result = ratingRepository.findAllByMenuId(1L);

        assertEquals(2, result.size());
        assertTrue(result.contains(rating1));
        assertTrue(result.contains(rating2));
    }

    @Test
    void testFindAllByMenuId_ShouldReturnEmptyList_WhenNoRatingFound() {
        List<Rating> result = ratingRepository.findAllByMenuId(99L);

        assertTrue(result.isEmpty());
    }
}