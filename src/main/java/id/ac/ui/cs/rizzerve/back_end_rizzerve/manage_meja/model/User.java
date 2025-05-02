package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "app_user")
public class User {

    @Id
    private int id;

    private String email;

    private String role;

    public User(int id, String email, String role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }
}
