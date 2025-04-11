package id.ac.ui.cs.rizzerve.back_end_rizzerve.model;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "cart_id")
    private Long cartId;

    // Transient fields for storing objects without persistence
    @Transient
    private Product product;

    @Transient
    private Cart cart;
}