package id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.builder;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.model.Checkout;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CheckoutBuilder {
    private List<CartItem> items;
    private LocalDateTime timestamp;

    public static CheckoutBuilder builder() {
        return new CheckoutBuilder();
    }

    public CheckoutBuilder fromCart(Cart cart) {
        if (cart == null) throw new IllegalArgumentException("Cart cannot be null");
        this.items = new ArrayList<>(cart.getItems()); // deep copy if needed
        return this;
    }

    public CheckoutBuilder withTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public Checkout build() {
        int total = 0;
        for (CartItem item : items) {
            total += item.getPrice() * item.getQuantity();
        }
        return new Checkout(items, total, timestamp);
    }
}

// Dummy Classes
@Getter
class Cart {
    private List<CartItem> items;
    public Cart(List<CartItem> items) {
        this.items = items;
    }
}
