package id.ac.ui.cs.rizzerve.back_end_rizzerve.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    private Long id;
    private User user;
    private List<CartItem> items = new ArrayList<>();
}