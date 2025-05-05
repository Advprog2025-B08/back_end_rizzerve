package id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.model;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import lombok.*;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "RatingBuilder")
public class Rating {
    private Long id;
    private User user;
    private Menu menu;
    private Integer ratingValue;

    public static class RatingBuilder {
        public Rating build() {
            if (this.user == null || this.menu == null) {
                throw new IllegalArgumentException("User and Menu must not be null");
            }
            if (this.ratingValue == null || ratingValue < 1 || ratingValue > 5) {
                throw new IllegalArgumentException("Rating value must be between 1 and 5");
            }
            return new Rating(id, user, menu, ratingValue);
        }
    }
}