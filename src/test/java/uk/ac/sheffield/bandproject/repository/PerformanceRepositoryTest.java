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
import uk.ac.sheffield.bandproject.Repository.PerformanceRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = BandProjectApplication.class)
public class PerformanceRepositoryTest {

    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private BandRepository bandRepository;

    @Test
    public void shouldSaveAndRetrievePerformanceById_whenNewPerformanceIsCreated() {
        Performance performance = new Performance(null, "TestLocation",
                LocalDate.of(2024,1,1), LocalTime.of(10,0),
                new HashSet<>(), new HashSet<>());
        performanceRepository.save(performance);

        Optional<Performance> foundPerformance = performanceRepository.findById(performance.getId());
        assertTrue(foundPerformance.isPresent());
        assertEquals("TestLocation", foundPerformance.get().getLocation());
    }

    @Test
    public void shouldReturnAllPerformancesByBand() {
        Band band = new Band(null, "TestBand", new HashSet<>(), new HashSet<>(), new HashSet<>());
        bandRepository.save(band);

        Performance performance = new Performance(null, "TestLocation",
                LocalDate.of(2024,1,1),LocalTime.of(10,0),
                new HashSet<>(), new HashSet<>());
        performance.getBands().add(band);
        performanceRepository.save(performance);

        band.getPerformances().add(performance);
        bandRepository.save(band);

        List<Performance> performances = performanceRepository.findByBand(band.getId());
        assertEquals(1, performances.size());
    }
}
