package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.dto;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CartDTO {
    private Long id;
    private Long userId;
    private List<CartItemDTO> items;

    public CartDTO(Cart cart) {
        this.id = cart.getId();
        this.userId = cart.getUserId();
        this.items = cart.getItems().stream()
                .map(CartItemDTO::new)
                .collect(Collectors.toList());
    }
}
