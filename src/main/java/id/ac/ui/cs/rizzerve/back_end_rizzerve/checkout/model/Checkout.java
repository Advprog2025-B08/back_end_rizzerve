package id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.model;

import java.util.ArrayList;
import java.util.List;

public class Checkout {

    private final List<Item> items = new ArrayList<>();

    public void addItem(String name, int quantity, int pricePerItem) {
        items.add(new Item(name, quantity, pricePerItem));
    }

    public int getTotalPrice() {
        return items.stream()
                .mapToInt(item -> item.quantity * item.pricePerItem)
                .sum();
    }

    // Dummy inner class sebagai placeholder sebelum model Menu tersedia
    static class Item {
        String name;
        int quantity;
        int pricePerItem;

        Item(String name, int quantity, int pricePerItem) {
            this.name = name;
            this.quantity = quantity;
            this.pricePerItem = pricePerItem;
        }
    }
}
