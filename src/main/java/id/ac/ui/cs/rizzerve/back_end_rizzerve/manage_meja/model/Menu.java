package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Menu {

    @Id
    private String id;

    private String name;

    private int jumlah;

    public Menu(String id, String name, int jumlah) {
        this.id = id;
        this.name = name;
        this.jumlah = jumlah;
    }
    public String getNama() {
        return this.name;
    }
}
