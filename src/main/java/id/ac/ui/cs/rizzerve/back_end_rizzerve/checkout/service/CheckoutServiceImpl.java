package id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.model.Checkout;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.repository.CheckoutRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final CheckoutRepository checkoutRepository;
    private final CartRepository cartRepository;

    @Autowired
    public CheckoutServiceImpl(CheckoutRepository checkoutRepository, CartRepository cartRepository) {
        this.checkoutRepository = checkoutRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public Checkout createCheckout(Long cartId) {
        // Facade logic: Mengatur checkout dengan menyembunyikan detail implementasi
        Checkout checkout = buildCheckout(cartId);

        return checkoutRepository.save(checkout);
    }

    private int calculateTotalPrice(Cart cart) {
        // Menghitung total harga berdasarkan cart yang ada
        return cart.getItems().stream()
                .mapToInt(CartItem::getQuantity) // Belum hitung total harga karena harus update model cartitem
                .sum();
    }

    private Checkout buildCheckout(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        Checkout checkout = Checkout.builder()
                .user(cart.getUser()) // Hubungkan dengan user yang sama
                .cart(cart) // Hubungkan Cart yang sudah ada
                .totalPrice(calculateTotalPrice(cart)) // Misalnya hitung total price dari Cart
                .isSubmitted(false)
                .createdAt(LocalDateTime.now())
                .build();
        return checkout;
    }
}
