package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MejaDTO {
    private long id;
    private int nomor;
    private String username;
    private CartDTO cart;
}