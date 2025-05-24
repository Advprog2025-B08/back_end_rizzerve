package id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.service;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.model.Checkout;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.dto.CheckoutResponse;

import java.util.List;
import java.util.Optional;

public interface CheckoutService {
    Checkout createCheckout(Long cartID);
    void deleteCheckout(Long checkoutID);
    void updateCartItemQuantity(Long cartId, Long itemId, int deltaQuantity);
    Optional<Checkout> findById(Long checkoutId);
    Checkout submitCheckout(Long checkoutId);
    List<CheckoutResponse> getSubmittedCheckouts();
    void deleteCheckoutAfterProcessing(Long checkoutId);
}
