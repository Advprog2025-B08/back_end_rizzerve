package id.ac.ui.cs.rizzerve.back_end_rizzerve.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating {
    private Long id;
    private User user;
    private Product product;
    private Integer ratingValue;
}
