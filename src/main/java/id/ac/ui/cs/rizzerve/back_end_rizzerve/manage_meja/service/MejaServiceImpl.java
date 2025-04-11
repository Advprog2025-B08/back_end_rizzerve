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
    public void createMeja(int meja) {
        mejaRepository.createMeja(meja);
    }

    @Override
    public void updateMeja(int oldNomor, int newNomor) {
        mejaRepository.updateMeja(oldNomor, newNomor);
    }

    @Override
    public void deleteMeja(String id) {
        mejaRepository.deleteMeja(id);
    }

    @Override
    public List<Meja> getAllMeja() {
        return mejaRepository.getAllMejas();
    }

    @Override
    public Meja getMejaById(int id) {
        return mejaRepository.getMejaByNomor(id);
    }
}
