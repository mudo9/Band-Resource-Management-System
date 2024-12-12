package uk.ac.sheffield.bandproject.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import uk.ac.sheffield.bandproject.BandProjectApplication;
import uk.ac.sheffield.bandproject.Model.Band;
import uk.ac.sheffield.bandproject.Model.MusicSet;
import uk.ac.sheffield.bandproject.Model.Performance;
import uk.ac.sheffield.bandproject.Model.User;
import uk.ac.sheffield.bandproject.Repository.BandRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = BandProjectApplication.class)
public class BandRepositoryTest {

    @Autowired
    private BandRepository bandRepository;

    @Test
    public void shouldSaveAndRetrieveBandById_whenNewBandIsCreated() {
        Band band = new Band(null, "TestBand", new HashSet<>(), new HashSet<>(),
                new HashSet<>());
        bandRepository.save(band);

        Optional<Band> foundBand = bandRepository.findById(band.getId());
        assertTrue(foundBand.isPresent());
        assertEquals("TestBand", foundBand.get().getName());
    }

    @Test
    public void shouldSaveAndRetrieveBandByName_whenNewBandIsCreated() {
        Band band = new Band(null, "TestBand", new HashSet<>(), new HashSet<>(),
                new HashSet<>());
        bandRepository.save(band);

        Optional<Band> foundBand = bandRepository.findByName(band.getName());
        assertTrue(foundBand.isPresent());
    }

    @Test
    public void shouldReturnAllBands_whenMultipleBandsArePresent() {
        Band band1 = new Band(null, "TestBand1", new HashSet<>(), new HashSet<>(), new HashSet<>());
        Band band2 = new Band(null, "TestBand2", new HashSet<>(), new HashSet<>(), new HashSet<>());
        bandRepository.save(band1);
        bandRepository.save(band2);

        List<Band> bands = bandRepository.findAll();
        assertEquals(4, bands.size()); // 2 pre-exist bands are Senior and Training Band
    }

    @Test
    public void shouldDeleteBandById_whenValidIdIsGiven() {
        Band band = new Band(null, "TestBand", new HashSet<>(), new HashSet<>(), new HashSet<>());
        bandRepository.save(band);

        bandRepository.deleteById(band.getId());
        Optional<Band> foundBand = bandRepository.findById(band.getId());
        assertFalse(foundBand.isPresent());
    }
}
