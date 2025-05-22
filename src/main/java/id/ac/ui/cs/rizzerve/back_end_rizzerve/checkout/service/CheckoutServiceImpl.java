package id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.model.Checkout;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.repository.CheckoutRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    @Override
    public void deleteCheckout(Long checkoutID) {
        Checkout checkout = checkoutRepository.findById(checkoutID)
                .orElseThrow(() -> new NoSuchElementException("Checkout not found"));

        if (checkout.getIsSubmitted()) {
            throw new IllegalStateException("Cannot cancel a submitted checkout");
        }

        checkoutRepository.delete(checkout);
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

    @Override
    public void updateCartItemQuantity(Long cartId, Long itemId, int deltaQuantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        CartItem item = cart.getItems().stream()
                .filter(ci -> ci.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not found in this cart"));

        int updatedQuantity = item.getQuantity() + deltaQuantity;

        if (updatedQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        } else if (updatedQuantity == 0) {
            cart.getItems().remove(item);
        } else {
            item.setQuantity(updatedQuantity);
        }

        cartRepository.save(cart);
    }

    @Override
    public Optional<Checkout> findById(Long checkoutId) {
        return checkoutRepository.findById(checkoutId);
    }

    @Override
    public Checkout submitCheckout(Long checkoutId) {
        Checkout checkout = checkoutRepository.findById(checkoutId)
                .orElseThrow(() -> new NoSuchElementException("Checkout not found"));

        if (checkout.getIsSubmitted()) {
            throw new IllegalStateException("Checkout already submitted");
        }

        checkout.setIsSubmitted(true);
        return checkoutRepository.save(checkout);
    }

}
