package id.ac.ui.cs.rizzerve.back_end_rizzerve.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "RatingBuilder")
public class Rating {
    private Long id;
    private User user;
    private Product product;
    private Integer ratingValue;

    public static class RatingBuilder {
        public Rating build() {
            if (this.ratingValue == null || this.ratingValue < 1 || this.ratingValue > 5) {
                throw new IllegalArgumentException("Rating value must be between 1 and 5");
            }
            return new Rating(id, user, product, ratingValue);
        }
    }
}