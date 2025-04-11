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
    public void createMeja(Meja meja) {
        mejaRepository.createMeja(meja);
    }

    @Override
    public void updateMeja(String oldNomor, String newNomor) {
        mejaRepository.updateMeja(oldNomor, newNomor);
    }

    @Override
    public void deleteMeja(String id) {
        mejaRepository.deleteMeja(id);
    }

    @Override
    public List<Meja> getAllMeja() {
        return mejaRepository.findAll();
    }

    @Override
    public Meja getMejaById(String id) {
        return mejaRepository.findById(id);
    }
}
