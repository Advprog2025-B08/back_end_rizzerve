package id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.model.Checkout;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.repository.CheckoutRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final CheckoutRepository checkoutRepository;

    @Autowired
    public CheckoutServiceImpl(CheckoutRepository checkoutRepository) {
        this.checkoutRepository = checkoutRepository;
    }

    @Override
    public Checkout createCheckout(Cart cart) {
        // Facade logic: Mengatur checkout dengan menyembunyikan detail implementasi
        int totalPrice = calculateTotalPrice(cart);
        Checkout checkout = buildCheckout(cart, totalPrice);

        return checkoutRepository.save(checkout);
    }

    private int calculateTotalPrice(Cart cart) {
        // Menghitung total harga berdasarkan cart yang ada
        return cart.getItems().stream()
                .mapToInt(CartItem::getQuantity) // Belum hitung total harga karena harus update model cartitem
                .sum();
    }

    private Checkout buildCheckout(Cart cart, int totalPrice) {
        // Membangun objek Checkout dari Cart dan total price
        return Checkout.builder()
                .userId(cart.getUserId())
                .cart(cart)
                .totalPrice(totalPrice)
                .isSubmitted(false)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
