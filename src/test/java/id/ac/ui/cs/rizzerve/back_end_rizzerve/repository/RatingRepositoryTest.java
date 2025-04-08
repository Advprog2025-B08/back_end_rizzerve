package id.ac.ui.cs.rizzerve.back_end_rizzerve.repository;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.Product;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.Rating;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.User;
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
        Rating rating = Rating.builder()
                .id(1L)
                .user(new User(1L, "Daniel"))
                .product(new Product(1L, "Nasi Goreng"))
                .ratingValue(4)
                .build();

        ratingRepository.save(rating);

        Optional<Rating> found = ratingRepository.findById(1L);

        assertThat(found).isPresent();
        assertThat(found.get().getRatingValue()).isEqualTo(4);
    }

    @Test
    void testUpdateRating() {
        Rating rating = Rating.builder()
                .id(1L)
                .user(new User(1L, "Daniel"))
                .product(new Product(1L, "Nasi Goreng"))
                .ratingValue(3)
                .build();

        ratingRepository.save(rating);

        Rating updatedRating = Rating.builder()
                .id(1L)
                .user(new User(1L, "Daniel"))
                .product(new Product(1L, "Nasi Goreng"))
                .ratingValue(5)
                .build();

        ratingRepository.save(updatedRating);

        Optional<Rating> found = ratingRepository.findById(1L);

        assertThat(found).isPresent();
        assertThat(found.get().getRatingValue()).isEqualTo(5);
    }

    @Test
    void testDeleteRating() {
        Rating rating = Rating.builder()
                .id(1L)
                .user(new User(1L, "Daniel"))
                .product(new Product(1L, "Nasi Goreng"))
                .ratingValue(4)
                .build();

        ratingRepository.save(rating);
        ratingRepository.deleteById(1L);

        Optional<Rating> found = ratingRepository.findById(1L);

        assertThat(found).isNotPresent();
    }

    @Test
    void testFindAllByProductId_ShouldReturnCorrectRatings() {
        Product product1 = new Product(1L, "Nasi Goreng");
        Product product2 = new Product(2L, "Nasi Padang");

        User user1 = new User(1L, "Daniel");
        User user2 = new User(2L, "Angger");
        User user3 = new User(3L, "Dewandaru");

        Rating rating1 = new Rating(1L, user1, product1, 4);
        Rating rating2 = new Rating(2L, user2, product1, 5);
        Rating rating3 = new Rating(3L, user3, product2, 3);

        ratingRepository.save(rating1);
        ratingRepository.save(rating2);
        ratingRepository.save(rating3);

        List<Rating> result = ratingRepository.findAllByProductId(1L);

        assertEquals(2, result.size());
        assertTrue(result.contains(rating1));
        assertTrue(result.contains(rating2));
    }

    @Test
    void testFindAllByProductId_ShouldReturnEmptyList_WhenNoRatingFound() {
        List<Rating> result = ratingRepository.findAllByProductId(99L);

        assertTrue(result.isEmpty());
    }
}
