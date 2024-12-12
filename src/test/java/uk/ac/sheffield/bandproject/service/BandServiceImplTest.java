package uk.ac.sheffield.bandproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.ac.sheffield.bandproject.Model.Band;
import uk.ac.sheffield.bandproject.Model.MusicSet;
import uk.ac.sheffield.bandproject.Model.Performance;
import uk.ac.sheffield.bandproject.Model.User;
import uk.ac.sheffield.bandproject.Repository.BandRepository;
import uk.ac.sheffield.bandproject.Service.BandServiceImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class BandServiceImplTest {
    @Mock
    private BandRepository bandRepository;

    @InjectMocks
    private BandServiceImpl bandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnAllBands_whenGetAllBandsIsCalled() {
        Band band1 = new Band(1L, "TestBand1", new HashSet<>(), new HashSet<>(), new HashSet<>());
        Band band2 = new Band(2L, "TestBand2", new HashSet<>(), new HashSet<>(), new HashSet<>());
        when(bandRepository.findAll()).thenReturn(Arrays.asList(band1, band2));

        List<Band> bands = bandService.getAllBands();
        assertEquals(2, bands.size());
        assertEquals("TestBand1", bands.get(0).getName());
        assertEquals("TestBand2", bands.get(1).getName());
    }

    @Test
    public void shouldReturnBand_whenValidIdIsGiven() {
        Band band = new Band(1L, "TestBand", new HashSet<>(), new HashSet<>(), new HashSet<>());
        when(bandRepository.findById(1L)).thenReturn(Optional.of(band));

        Optional<Band> foundBand = bandService.getBandById(1L);
        assertTrue(foundBand.isPresent());
        assertEquals("TestBand", foundBand.get().getName());
    }

    @Test
    public void shouldReturnBand_whenValidNameIsGiven() {
        Band band = new Band(1L, "TestBand", new HashSet<>(), new HashSet<>(), new HashSet<>());
        when(bandRepository.findByName("TestBand")).thenReturn(Optional.of(band));

        Optional<Band> foundBand = bandService.getBandByName("TestBand");
        assertTrue(foundBand.isPresent());
        assertEquals(1L, foundBand.get().getId());
    }
}
