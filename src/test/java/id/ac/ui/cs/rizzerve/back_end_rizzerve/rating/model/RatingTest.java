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

        Rating rating = Rating.builder()
                .id(1L)
                .user(user)
                .menu(menu)
                .ratingValue(5)
                .build();

        assertThat(rating.getUser().getUsername()).isEqualTo("Daniel");
        assertThat(rating.getMenu().getName()).isEqualTo("Nasi Goreng");
        assertThat(rating.getRatingValue()).isEqualTo(5);
    }

    @Test
    void createRating_Failure() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Daniel");
        user.setPassword("pass");
        user.setRole("CUSTOMER");

        Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("Nasi Goreng");

        assertThrows(IllegalArgumentException.class, () -> {
            Rating.builder()
                    .id(1L)
                    .user(user)
                    .menu(menu)
                    .ratingValue(6)
                    .build();
        });
    }
}