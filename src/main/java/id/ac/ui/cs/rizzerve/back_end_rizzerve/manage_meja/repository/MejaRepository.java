package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.repository;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.model.Meja;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class MejaRepository {

    private final List<Meja> MejaData = new ArrayList<>();

    public List<Meja> getAllMejas() {
        return new ArrayList<>(MejaData);
    }

    public Meja getMejaByNomor(int Nomor) {
        return MejaData.stream()
                .filter(m -> m.getNomor()==Nomor)
                .findFirst()
                .orElse(null);
    }

    public void createMeja(int Nomor) {
        Meja meja = new Meja();
        meja.setId(UUID.randomUUID().toString());
        meja.setNomor(Nomor);
        MejaData.add(meja);
    }

    public void updateMeja(int oldNum, int newNum) {
        Meja meja = getMejaByNomor(oldNum);
        if (meja != null) {
            meja.setNomor(newNum);
        }
    }

    public void deleteMeja(String id) {
        MejaData.removeIf(meja -> meja.getId().equals(id));
    }

    public boolean checkUnique(int Nomor) {
        return getMejaByNomor(Nomor) == null;
    }

    public void setUser(int MejaNum, int userId) {
        Meja meja = getMejaByNomor(MejaNum);
        if (meja != null) {
            User dummyUser = new User();
            dummyUser.setId(userId);
            dummyUser.setEmail("dummy@email.com");
            dummyUser.setRole("USER");
            meja.setUser(dummyUser);
        }
    }

    public void delUser(int MejaNum) {
        Meja meja = getMejaByNomor(MejaNum);
        if (meja != null) {
            meja.setUser(null);
        }
    }
}
