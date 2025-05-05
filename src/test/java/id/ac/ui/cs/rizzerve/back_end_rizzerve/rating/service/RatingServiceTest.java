package id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.model.Rating;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.repository.RatingRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.service.RatingService;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.service.RatingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RatingServiceTest {

    private RatingRepository ratingRepository;
    private RatingService ratingService;

    @BeforeEach
    void setUp() {
        ratingRepository = new RatingRepository();
        ratingService = new RatingServiceImpl(ratingRepository);
    }

    @Test
    void testCreateRating() {
        // Buat User tanpa builder
        User user = new User();
        user.setId(1L);
        user.setUsername("Daniel");
        user.setPassword("pass");
        user.setRole("CUSTOMER");

        // Buat Menu tanpa builder
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("Nasi Goreng");

        Rating rating = Rating.builder()
                .id(1L)
                .user(user)
                .menu(menu)
                .ratingValue(5)
                .build();

        ratingService.createRating(rating);

        Optional<Rating> savedRating = ratingRepository.findById(1L);
        assertTrue(savedRating.isPresent());
        assertEquals(5, savedRating.get().getRatingValue());
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
                .ratingValue(4)
                .build();

        ratingService.createRating(rating);

        Rating updatedRating = Rating.builder()
                .id(1L)
                .user(user)
                .menu(menu)
                .ratingValue(2)
                .build();

        ratingService.updateRating(updatedRating);

        Optional<Rating> updated = ratingRepository.findById(1L);
        assertTrue(updated.isPresent());
        assertEquals(2, updated.get().getRatingValue());
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
                .ratingValue(5)
                .build();

        ratingService.createRating(rating);

        ratingService.deleteRating(1L);

        Optional<Rating> deleted = ratingRepository.findById(1L);
        assertTrue(deleted.isEmpty());
    }

    @Test
    void testGetAverageRatingByMenuId() {
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

        Menu menu = new Menu();
        menu.setId(10L);
        menu.setName("Nasi Goreng");

        ratingService.createRating(Rating.builder()
                .id(1L)
                .user(user1)
                .menu(menu)
                .ratingValue(4)
                .build());

        ratingService.createRating(Rating.builder()
                .id(2L)
                .user(user2)
                .menu(menu)
                .ratingValue(2)
                .build());

        ratingService.createRating(Rating.builder()
                .id(3L)
                .user(user3)
                .menu(menu)
                .ratingValue(5)
                .build());

        double avg = ratingService.getAverageRatingByMenuId(10L);

        assertEquals(3.666, avg, 0.01);
    }
}