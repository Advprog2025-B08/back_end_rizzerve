package id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class Checkout {
    private final List<CartItem> items;
    private final int totalPrice;
    private final LocalDateTime timestamp;

    public Checkout(List<CartItem> items, int totalPrice, LocalDateTime timestamp) {
        this.items = items;
        this.totalPrice = totalPrice;
        this.timestamp = timestamp;
    }

}

