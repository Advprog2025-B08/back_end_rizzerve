package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.model.Meja;

import java.util.List;

public interface MejaService {
    void createMeja(int nomor);
    void updateMeja(int oldNomor, int newNomor);
    void deleteMeja(String id);
    List<Meja> getAllMeja();
    Meja getMejaById(int id);
}
