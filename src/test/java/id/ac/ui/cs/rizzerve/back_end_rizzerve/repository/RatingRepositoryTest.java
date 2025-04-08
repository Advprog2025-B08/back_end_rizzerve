package id.ac.ui.cs.rizzerve.back_end_rizzerve.repository;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.Product;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.Rating;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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

        // Update rating value
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
}
