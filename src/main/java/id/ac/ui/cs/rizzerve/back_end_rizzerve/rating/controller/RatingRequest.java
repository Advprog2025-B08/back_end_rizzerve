package id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.controller;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RatingRequest {
    private Long id;
    private Long menuId;
    private Long userId;
    private Integer ratingValue;
}

