package id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.repository;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.model.Checkout;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckoutRepository extends JpaRepository<Checkout, Long> {
}
