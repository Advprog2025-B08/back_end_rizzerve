package id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.model;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.model.Product;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.model.Rating;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RatingTest {

    @Test
    void createRating_Success() {
        User user = User.builder().id(1L).name("Daniel").build();
        Product product = Product.builder().id(1L).name("Nasi Goreng").build();

        Rating rating = Rating.builder()
                .id(1L)
                .user(user)
                .product(product)
                .ratingValue(5)
                .build();

        assertThat(rating.getUser().getName()).isEqualTo("Daniel");
        assertThat(rating.getProduct().getName()).isEqualTo("Nasi Goreng");
        assertThat(rating.getRatingValue()).isEqualTo(5);
    }

    @Test
    void createRating_Failure() {
        User user = User.builder().id(1L).name("Daniel").build();
        Product product = Product.builder().id(1L).name("Daniel").build();

        assertThrows(IllegalArgumentException.class, () -> {
            Rating.builder()
                    .id(1L)
                    .user(user)
                    .product(product)
                    .ratingValue(6)
                    .build();
        });
    }
}
