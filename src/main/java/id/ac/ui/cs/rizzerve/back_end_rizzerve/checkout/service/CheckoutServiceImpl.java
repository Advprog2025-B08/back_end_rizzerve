package id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.constants.ErrorMessages;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.model.Checkout;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.repository.CheckoutRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.dto.CheckoutResponse;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository.CartRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Checkout checkout = createCheckoutIfValid(cartId);

        return checkoutRepository.save(checkout);
    }

    @Override
    @Transactional
    public void deleteCheckout(Long checkoutID) {
        Checkout checkout = checkoutRepository.findById(checkoutID)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessages.CHECKOUT_NOT_FOUND));
        checkoutRepository.delete(checkout);
        checkoutRepository.flush();
    }

    private int calculateTotalPrice(Cart cart) {
        return cart.getItems().stream()
                .mapToInt(cartItem -> Math.toIntExact(cartItem.getQuantity() * cartItem.getMenu().getPrice()))
                .sum();
    }

    private Checkout createCheckoutIfValid(Long cartId) {
        Cart cart = getCart(cartId);
        validateExistingCheckout(cartId);
        return buildCheckoutFromCart(cart);
    }

    private Cart getCart(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.CART_NOT_FOUND));

    }

    private void validateExistingCheckout(Long cartId) {
        Optional<Checkout> existingCheckout = checkoutRepository.findByCartId(cartId);
        if (existingCheckout.isPresent()) {
            if (existingCheckout.get().getIsSubmitted()) {
                throw new IllegalStateException(ErrorMessages.CHECKOUT_ALREADY_SUBMITTED);
            }
            throw new IllegalStateException(ErrorMessages.CHECKOUT_ALREADY_CREATED);
        }
    }

    private Checkout buildCheckoutFromCart(Cart cart) {
        return Checkout.builder()
                .user(cart.getUser())
                .cart(cart)
                .totalPrice(calculateTotalPrice(cart))
                .isSubmitted(false)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Override
    @Transactional
    public void updateCartItemQuantity(Long cartId, Long itemId, int deltaQuantity) {
        Cart cart = getCart(cartId);
        Checkout checkout = validateIfCheckoutExist(cartId);
        validateIfCheckoutIsSubmitted(checkout);

        CartItem item = cart.getItems().stream()
                .filter(ci -> ci.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.ITEM_NOT_FOUND_IN_CART));

        int updatedQuantity = item.getQuantity() + deltaQuantity;

        if (updatedQuantity < 0) {
            throw new IllegalArgumentException(ErrorMessages.RESULTING_QUANTITY_CAN_NOT_BE_NEGATIVE);
        } else if (updatedQuantity == 0) {
            cart.getItems().remove(item);
        } else {
            item.setQuantity(updatedQuantity);
        }

        int new_price = calculateTotalPrice(cart);
        checkout.setTotalPrice(new_price);
        cartRepository.save(cart);
    }

    @Override
    public Optional<Checkout> findById(Long checkoutId) {
        return checkoutRepository.findById(checkoutId);
    }

    @Override
    public List<CheckoutResponse> getSubmittedCheckouts() {
        List<Checkout> submittedCheckouts = checkoutRepository.findByIsSubmittedTrue();
        return submittedCheckouts.stream()
                .map(checkout -> CheckoutResponse.builder()
                        .id(checkout.getId())
                        .cartId(checkout.getCart().getId())
                        .userId(checkout.getCart().getUser().getId())
                        .totalPrice(calculateTotalPrice(checkout.getCart()))
                        .isSubmitted(checkout.getIsSubmitted())
                        .createdAt(checkout.getCreatedAt().toString())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCheckoutAfterProcessing(Long checkoutId) {
        Checkout checkout = checkoutRepository.findById(checkoutId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessages.CHECKOUT_NOT_FOUND));
        if (!checkout.getIsSubmitted()) {
            throw new IllegalStateException(ErrorMessages.CHECKOUT_HAS_NOT_BEEN_SUBMITTED);
        }
        checkoutRepository.delete(checkout);
        checkoutRepository.flush();
    }


    @Override
    public Checkout submitCheckout(Long checkoutId) {
        Checkout checkout = checkoutRepository.findById(checkoutId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessages.CHECKOUT_NOT_FOUND));

        validateIfCheckoutIsSubmitted(checkout);

        checkout.setIsSubmitted(true);
        return checkoutRepository.save(checkout);
    }

    public void validateIfCheckoutIsSubmitted(Checkout checkout) {
        if (checkout.getIsSubmitted()) {
            throw new IllegalStateException(ErrorMessages.CHECKOUT_ALREADY_SUBMITTED);
        }
    }

    public Checkout validateIfCheckoutExist(Long cartId) {
        return checkoutRepository.findByCartId(cartId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessages.CHECKOUT_NOT_FOUND));
    }

}
