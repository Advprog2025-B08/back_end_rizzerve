package id.ac.ui.cs.rizzerve.back_end_rizzerve.checkout.model;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "checkout")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Checkout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Column(name = "total_price")
    private Integer totalPrice;

    @Column(name = "is_submitted")
    private Boolean isSubmitted;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
