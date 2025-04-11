package id.ac.ui.cs.rizzerve.back_end_rizzerve.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {
    private Long id;
    private Product product;
    private int quantity;
}