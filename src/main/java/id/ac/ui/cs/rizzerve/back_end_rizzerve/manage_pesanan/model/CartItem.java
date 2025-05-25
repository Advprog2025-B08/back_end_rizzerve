package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;
import lombok.*;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "cart_id")
    private Long cartId;

    @Column(name = "menu_id")
    private Long menuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", insertable = false, updatable = false)
    @JsonIgnore
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", insertable = false, updatable = false)
    private Menu menu;

}