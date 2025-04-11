package id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class Checkout {
    private final List<CartItem> items;
    private final int totalPrice;
    private final LocalDateTime timestamp;

    Checkout(List<CartItem> items, int totalPrice, LocalDateTime timestamp) {
        this.items = items;
        this.totalPrice = totalPrice;
        this.timestamp = timestamp;
    }

    List<CartItem> getItems() {
        return items;
    }

    int getTotalPrice() {
        return totalPrice;
    }

    LocalDateTime getTimestamp() {
        return timestamp;
    }
}

// Dummy class CartItem
@Getter
class CartItem {
    private String name;
    private int quantity;
    private int price; // harga per item

    public CartItem(String name, int quantity, int price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

}

