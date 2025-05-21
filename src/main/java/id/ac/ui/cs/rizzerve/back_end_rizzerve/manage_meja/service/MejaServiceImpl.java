package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.model.Meja;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.repository.MejaRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.repository.UserRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MejaServiceImpl implements MejaService {

    private final MejaRepository mejaRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    @Autowired
    public MejaServiceImpl(MejaRepository mejaRepository, UserRepository userRepository, CartRepository cartRepository) {
        this.mejaRepository = mejaRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
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
    @Transactional
    public Meja createMeja(int nomor) {
        if (mejaRepository.checkUnique(nomor)) {
            Meja meja = new Meja();
            meja.setNomor(nomor);
            return mejaRepository.save(meja);
        }
        return null;
    }

    @Override
    @Transactional
    public Meja updateMeja(int oldNomor, int newNomor) {
        Meja meja = mejaRepository.getMejaByNomor(oldNomor);
        if (meja != null && mejaRepository.checkUnique(newNomor)) {
            meja.setNomor(newNomor);
            return mejaRepository.save(meja);
        }
        return null;
    }

    @Override
    @Transactional
    public boolean deleteMeja(int nomor) {
        Meja meja = mejaRepository.getMejaByNomor(nomor);
        if (meja != null) {
            mejaRepository.delete(meja);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Meja setUserToMeja(int mejaNum, String username) {
        Meja meja = mejaRepository.getMejaByNomor(mejaNum);
        if (meja != null && meja.getUser() == null) {
            User user = userRepository.findByUsername(username).orElse(null);
            if (user != null) {
                meja.setUser(user);
                Cart userCart = cartRepository.findByUserId(user.getId())
                        .orElseGet(() -> {
                            Cart newCart = Cart.builder()
                                    .userId(user.getId())
                                    .user(user)
                                    .items(new ArrayList<>())
                                    .build();
                            return cartRepository.save(newCart);
                        });
                meja.setCart(userCart);
                return mejaRepository.save(meja);
            }
        }
        return null;
    }

    @Override
    @Transactional
    public boolean removeUserFromMeja(int mejaNum) {
        Meja meja = mejaRepository.getMejaByNomor(mejaNum);
        if (meja != null) {
            meja.setUser(null);
            mejaRepository.save(meja);
            return true;
        }
        return false;
    }
}