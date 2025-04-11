package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.model.Meja;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.repository.MejaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class MejaServiceTest {

    private MejaRepository mejaRepository;
    private MejaService mejaService;

    @BeforeEach
    void setUp() {
        mejaRepository = mock(MejaRepository.class);
        mejaService = new MejaServiceImpl(mejaRepository);
    }

    @Test
    void testCreateMeja() {
        Meja meja = new Meja();
        meja.setNomor(1);
        mejaService.createMeja(meja);
        verify(mejaRepository, times(1)).createMeja(meja);
    }

    @Test
    void testUpdateMeja() {
        mejaService.updateMeja(1, 2);
        verify(mejaRepository, times(1)).updateMeja(1, 2);
    }

    @Test
    void testDeleteMeja() {
        mejaService.deleteMeja(1);
        verify(mejaRepository, times(1)).deleteMeja("1");
    }

    @Test
    void testGetAllMeja() {
        Meja meja = new Meja();
        meja.setNomor(1);
        Meja meja2 = new Meja();
        meja.setNomor(2);
        List<Meja> dummyList = List.of(meja, meja2);
        when(mejaRepository.findAll()).thenReturn(dummyList);

        List<Meja> result = mejaService.getAllMeja();
        assertEquals(2, result.size());
    }

    @Test
    void testGetMejaById() {
        Meja meja = new Meja();
        meja.setNomor(1);
        when(mejaRepository.findById(1)).thenReturn(meja);

        Meja result = mejaService.getMejaById(1);
        assertEquals(1, result.getNomor());
    }
}
