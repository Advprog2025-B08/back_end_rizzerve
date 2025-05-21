package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.model;

import jakarta.persistence.*;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import java.util.Objects;

@Entity
@Table(name = "mejas")
public class Meja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer nomor;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public void setUser(User user) {
        this.user = user;
        if (user == null) {
            this.cart = null;
        }
    }

    @PrePersist
    @PreUpdate
    public void validateState() {
        if (user == null) {
            cart = null;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNomor() {
        return nomor;
    }

    public void setNomor(Integer nomor) {
        this.nomor = nomor;
    }

    public User getUser() {
        return user;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meja meja = (Meja) o;
        return Objects.equals(id, meja.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Meja{" +
                "id='" + id + '\'' +
                ", nomor=" + nomor +
                ", hasUser=" + (user != null) +
                ", hasCart=" + (cart != null) +
                '}';
    }
}