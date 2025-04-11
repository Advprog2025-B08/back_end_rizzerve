package id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.model;

import lombok.Getter;

// Dummy class CartItem
@Getter
public class CartItem {
    private String name;
    private int quantity;
    private int price; // harga per item

    public CartItem(String name, int quantity, int price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

}
