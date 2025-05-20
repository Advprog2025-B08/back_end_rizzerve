package id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.model;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RatingTest {

    @Test
    void createRating_Success() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Daniel");
        user.setPassword("pass");
        user.setRole("CUSTOMER");

        Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("Nasi Goreng");

        Rating rating = new Rating.RatingBuilder()
                .setId(1L)
                .setUser(user)
                .setMenu(menu)
                .setRatingValue(5)
                .build();

        assertThat(rating.getUser().getUsername()).isEqualTo("Daniel");
        assertThat(rating.getMenu().getName()).isEqualTo("Nasi Goreng");
        assertThat(rating.getRatingValue()).isEqualTo(5);
    }

    @Test
    void createRating_Failure_InvalidRatingValue() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Daniel");
        user.setPassword("pass");
        user.setRole("CUSTOMER");

        Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("Nasi Goreng");

        assertThrows(IllegalArgumentException.class, () -> {
            new Rating.RatingBuilder()
                    .setId(1L)
                    .setUser(user)
                    .setMenu(menu)
                    .setRatingValue(6)
                    .build();
        });
    }

    @Test
    void createRating_Failure_NullUser() {
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("Nasi Goreng");

        assertThrows(IllegalArgumentException.class, () -> {
            new Rating.RatingBuilder()
                    .setId(1L)
                    .setMenu(menu)
                    .setRatingValue(3)
                    .build();
        });
    }

    @Test
    void createRating_Failure_NullMenu() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Daniel");
        user.setPassword("pass");
        user.setRole("CUSTOMER");

        assertThrows(IllegalArgumentException.class, () -> {
            new Rating.RatingBuilder()
                    .setId(1L)
                    .setUser(user)
                    .setRatingValue(3)
                    .build();
        });
    }
}