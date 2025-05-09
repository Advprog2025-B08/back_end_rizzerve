package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.model.Meja;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.repository.MejaRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MejaServiceImpl implements MejaService {

    private final MejaRepository mejaRepository;
    private final UserRepository userRepository;

    public MejaServiceImpl(MejaRepository mejaRepository, UserRepository userRepository) {
        this.mejaRepository = mejaRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Meja createMeja(int meja) {
        return mejaRepository.createMeja(meja);
    }

    @Override
    public Meja updateMeja(int oldNomor, int newNomor) {
        return mejaRepository.updateMeja(oldNomor, newNomor);
    }

    @Override
    public boolean deleteMeja(int id) {
        return mejaRepository.deleteMeja(id);
    }

    @Override
    public List<Meja> getAllMeja() {
        return mejaRepository.getAllMejas();
    }

    @Override
    public Meja getMejaByNomor(int nomor) {
        return mejaRepository.getMejaByNomor(nomor);
    }

    @Override
    public Meja setUserToMeja(int mejaNum, String username) {
        Meja meja = mejaRepository.getMejaByNomor(mejaNum);
        if (meja != null && meja.getUser() == null) {
            User user = userRepository.findByUsername(username)
                    .orElse(null);

            if (user != null) {
                return mejaRepository.setUser(meja, user);
            }
        }
        return null;
    }
    @Override
    public boolean removeUserFromMeja(int mejaNum) {
        return mejaRepository.delUser(mejaNum);
    }

}
