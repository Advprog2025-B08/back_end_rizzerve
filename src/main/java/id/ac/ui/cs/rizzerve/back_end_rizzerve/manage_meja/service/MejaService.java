package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.model.Meja;

import java.util.List;

public interface MejaService {
    Meja createMeja(int nomor);
    Meja updateMeja(int oldNomor, int newNomor);
    boolean deleteMeja(String id);
    List<Meja> getAllMeja();
    Meja getMejaById(int id);
    Meja setUserToMeja(int mejaNum, int userId);
    boolean removeUserFromMeja(int mejaNum);
}
