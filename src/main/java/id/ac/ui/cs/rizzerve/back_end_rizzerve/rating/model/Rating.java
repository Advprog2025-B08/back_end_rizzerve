package id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.model;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ratings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Column(name = "rating_value", nullable = false)
    private Integer ratingValue;

    public static class RatingBuilder {
        private User user;
        private Menu menu;
        private Integer ratingValue;
        private Long id;

        public RatingBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public RatingBuilder setUser(User user) {
            this.user = user;
            return this;
        }

        public RatingBuilder setMenu(Menu menu) {
            this.menu = menu;
            return this;
        }

        public RatingBuilder setRatingValue(Integer ratingValue) {
            this.ratingValue = ratingValue;
            return this;
        }

        public Rating build() {
            if (this.user == null || this.menu == null) {
                throw new IllegalArgumentException("User and Menu must not be null");
            }
            if (this.ratingValue == null || ratingValue < 1 || ratingValue > 5) {
                throw new IllegalArgumentException("Rating value must be between 1 and 5");
            }
            Rating rating = new Rating();
            rating.setId(id);
            rating.setUser(user);
            rating.setMenu(menu);
            rating.setRatingValue(ratingValue);
            return rating;
        }
    }
}