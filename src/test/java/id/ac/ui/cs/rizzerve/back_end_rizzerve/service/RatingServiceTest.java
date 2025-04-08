package id.ac.ui.cs.rizzerve.back_end_rizzerve.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.Product;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.Rating;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.User;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.repository.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        User user = User.builder().id(1L).name("Daniel").build();
        Product product = Product.builder().id(1L).name("Nasi Goreng").build();

        Rating rating = new Rating(1L, user, product, 5);
        ratingService.createRating(rating);

        assertTrue(ratingRepository.findById(1L).isPresent());
    }

    @Test
    void testUpdateRating() {
        User user = User.builder().id(1L).name("Daniel").build();
        Product product = Product.builder().id(1L).name("Nasi Goreng").build();

        Rating rating = new Rating(1L, user, product, 4);
        ratingService.createRating(rating);

        Rating updated = new Rating(1L, user, product, 2);
        ratingService.updateRating(updated);

        assertEquals(2, ratingRepository.findById(1L).get().getRatingValue());
    }

    @Test
    void testDeleteRating() {
        User user = User.builder().id(1L).name("Daniel").build();
        Product product = Product.builder().id(1L).name("Nasi Goreng").build();

        Rating rating = new Rating(1L, user, product, 5);
        ratingService.createRating(rating);

        ratingService.deleteRating(1L);

        assertTrue(ratingRepository.findById(1L).isEmpty());
    }

    @Test
    void testGetAverageRatingByProductId() {
        User user1 = User.builder().id(1L).name("Daniel").build();
        User user2 = User.builder().id(2L).name("Angger").build();
        User user3 = User.builder().id(3L).name("Dewandaru").build();

        Product product = Product.builder().id(10L).name("Nasi Goreng").build();

        ratingService.createRating(new Rating(1L, user1, product, 4));
        ratingService.createRating(new Rating(2L, user2, product, 2));
        ratingService.createRating(new Rating(3L, user3, product, 5));

        double avg = ratingService.getAverageRatingByProductId(10L);

        assertEquals(3.666, avg, 0.01);
    }
}