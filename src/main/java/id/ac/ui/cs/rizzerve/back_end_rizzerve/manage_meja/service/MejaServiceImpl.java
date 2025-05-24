package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.model.Meja;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.dto.*;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.repository.MejaRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.mediator.RestaurantMediator;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MejaServiceImpl implements MejaService {

    private final MejaRepository mejaRepository;
    private final RestaurantMediator mediator;

    @Autowired
    public MejaServiceImpl(MejaRepository mejaRepository, RestaurantMediator mediator) {
        this.mejaRepository = mejaRepository;
        this.mediator = mediator;
    }

    @Override
    public List<MejaDTO> getAllMeja() {
        List<Meja> allMejas = mejaRepository.getAllMejas();
        List<MejaDTO> dtoList = new ArrayList<>();
        for (Meja meja : allMejas) {
            dtoList.add(convertToDTO(meja));
        }
        return dtoList;
    }

    @Override
    public MejaDTO getMejaByNomor(int nomor) {
        Meja meja = mejaRepository.getMejaByNomor(nomor);
        if (meja == null) return null;
        return convertToDTO(meja);
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
            User user = mediator.findUserByUsername(username);
            if (user != null) {
                meja.setUser(user);
                Cart userCart = mediator.getOrCreateCartForUser(user);
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
            meja.setCart(null);
            mejaRepository.save(meja);
            return true;
        }
        return false;
    }
    private MejaDTO convertToDTO(Meja meja) {
        String username = (meja.getUser() != null) ? meja.getUser().getUsername() : null;
        CartDTO cartDto = null;
        Cart cart = meja.getCart();
        if (cart != null) {
            cartDto = new CartDTO(cart);
            cartDto.setId(meja.getCart().getId());
            cartDto.setUserId(meja.getCart().getUser().getId());

            List<CartItemDTO> itemDTOs = new ArrayList<>();
            meja.getCart().getItems().forEach(item -> {
                CartItemDTO itemDTO = new CartItemDTO(item);
                itemDTO.setId(item.getId());
                itemDTO.setMenuId(item.getMenu().getId());
                itemDTO.setQuantity(item.getQuantity());
                itemDTOs.add(itemDTO);
            });

            cartDto.setItems(itemDTOs);
        }

        return new MejaDTO(meja.getId(), meja.getNomor(), username, cartDto);
    }
}