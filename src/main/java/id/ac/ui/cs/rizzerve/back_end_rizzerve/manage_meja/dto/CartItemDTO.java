package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.dto;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import lombok.Data;

@Data
public class CartItemDTO {
    private Long id;
    private Long menuId;
    private String menuName;
    private int quantity;

    public CartItemDTO(CartItem item) {
        this.id = item.getId();
        this.menuId = item.getMenuId();
        this.quantity = item.getQuantity();
        this.menuName = item.getMenu() != null ? item.getMenu().getName() : null;
    }
}
