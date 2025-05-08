package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Cart {

    @Id
    private String id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    private List<Menu> menus;

    public Cart(String id, List<Menu> menus) {
        this.id = id;
        this.menus = menus;
    }

    public List<Menu> getItems() {
        return this.menus;
    }

    public void setItems(List<Menu> items) {
        this.menus = items;
    }
}
