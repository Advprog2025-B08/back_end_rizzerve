package id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.dto;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CheckoutRequest {

    @NotNull(message = "Cart ID cannot be null")
    private Long cartId;

    public void setCart(Cart dummyCart) {
    }
}
