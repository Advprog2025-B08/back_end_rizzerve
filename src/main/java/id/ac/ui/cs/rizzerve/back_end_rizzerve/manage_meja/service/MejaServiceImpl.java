package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.model.Meja;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.repository.MejaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MejaServiceImpl implements MejaService {

    private final MejaRepository mejaRepository;

    public MejaServiceImpl(MejaRepository mejaRepository) {
        this.mejaRepository = mejaRepository;
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
    public Meja setUserToMeja(int mejaNum, int userId) {
        return mejaRepository.setUser(mejaNum, userId);
    }

    @Override
    public boolean removeUserFromMeja(int mejaNum) {
        return mejaRepository.delUser(mejaNum);
    }

}
