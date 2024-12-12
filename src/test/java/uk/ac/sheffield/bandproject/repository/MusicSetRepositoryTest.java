package uk.ac.sheffield.bandproject.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import uk.ac.sheffield.bandproject.BandProjectApplication;
import uk.ac.sheffield.bandproject.Model.Band;
import uk.ac.sheffield.bandproject.Model.MusicSet;
import uk.ac.sheffield.bandproject.Repository.BandRepository;
import uk.ac.sheffield.bandproject.Repository.MusicSetRepository;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = BandProjectApplication.class)
public class MusicSetRepositoryTest {

    @Autowired
    private BandRepository bandRepository;

    @Autowired
    private MusicSetRepository musicSetRepository;

    @Test
    public void shouldReturnAllMusicSetsByBandName() {
        Band band = new Band(null, "TestBand", new HashSet<>(), new HashSet<>(), new HashSet<>());
        bandRepository.save(band);

        MusicSet musicSet = new MusicSet(null, "TestMusicSet", "TestComposer", "TestArranger",
                Boolean.TRUE, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        musicSet.getBands().add(band);
        musicSetRepository.save(musicSet);

        band.getMusicSets().add(musicSet);
        bandRepository.save(band);

        List<MusicSet> musicSets = musicSetRepository.findByBandName(band.getName());
        assertEquals(1, musicSets.size());
    }
}
