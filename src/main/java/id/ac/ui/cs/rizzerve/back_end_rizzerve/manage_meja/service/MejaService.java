package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.model.Meja;

import java.util.List;

public interface MejaService {
    void createMeja(Meja meja);
    void updateMeja(String oldNomor, String newNomor);
    void deleteMeja(String id);
    List<Meja> getAllMeja();
    Meja getMejaById(String id);
}
