package uk.ac.sheffield.bandproject.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import uk.ac.sheffield.bandproject.BandProjectApplication;
import uk.ac.sheffield.bandproject.Model.*;
import uk.ac.sheffield.bandproject.Repository.BandRepository;
import uk.ac.sheffield.bandproject.Repository.PerformanceMemberRepository;
import uk.ac.sheffield.bandproject.Repository.PerformanceRepository;
import uk.ac.sheffield.bandproject.Repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = BandProjectApplication.class)
public class PerformanceMemberRepositoryTest {

    @Autowired
    private PerformanceMemberRepository performanceMemberRepository;

    @Autowired
    private BandRepository bandRepository;

    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldReturnPerformanceMemberByUserIdAndBandIdAndPerformanceId() {
        User user = new User(null, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        userRepository.save(user);

        Band band = new Band(null, "TestBand", new HashSet<>(), new HashSet<>(), new HashSet<>());
        bandRepository.save(band);

        Performance performance = new Performance(null, "TestLocation",
                LocalDate.of(2024,1,1),LocalTime.of(10,0),
                new HashSet<>(), new HashSet<>());
        performance.getBands().add(band);
        performanceRepository.save(performance);

        user.getBands().add(band);
        userRepository.save(user);

        band.getUsers().add(user);
        bandRepository.save(band);

        PerformanceMemberId performanceMemberId =
                new PerformanceMemberId(user.getId(), band.getId(), performance.getId());
        PerformanceMember performanceMember = new PerformanceMember(performanceMemberId, user, band, performance,
                Boolean.TRUE);
        performanceMemberRepository.save(performanceMember);

        Optional<PerformanceMember> foundPerformanceMember = performanceMemberRepository
                .findByUserIdAndBandIdAndPerformanceId(user.getId(), band.getId(), performance.getId());
        assertTrue(foundPerformanceMember.isPresent());
        assertEquals(performance, foundPerformanceMember.get().getPerformance());
    }

    @Test
    public void shouldReturnAllPerformanceMembersByPerformanceIdAndAvailability() {
        User user = new User(null, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        userRepository.save(user);

        Band band = new Band(null, "TestBand", new HashSet<>(), new HashSet<>(), new HashSet<>());
        bandRepository.save(band);

        Performance performance = new Performance(null, "TestLocation",
                LocalDate.of(2024,1,1),LocalTime.of(10,0),
                new HashSet<>(), new HashSet<>());
        performance.getBands().add(band);
        performanceRepository.save(performance);

        user.getBands().add(band);
        userRepository.save(user);

        band.getUsers().add(user);
        bandRepository.save(band);

        PerformanceMemberId performanceMemberId =
                new PerformanceMemberId(user.getId(), band.getId(), performance.getId());
        PerformanceMember performanceMember = new PerformanceMember(performanceMemberId, user, band, performance,
                Boolean.TRUE);
        performanceMemberRepository.save(performanceMember);

        List<PerformanceMember> performanceMembers =
                performanceMemberRepository.findByPerformanceIdAndAvailability(performance.getId(), Boolean.TRUE);
        assertEquals(1, performanceMembers.size());
    }

    @Test
    public void shouldDeletePerformanceMemberByPerformanceId() {
        User user = new User(null, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        userRepository.save(user);

        Band band = new Band(null, "TestBand", new HashSet<>(), new HashSet<>(), new HashSet<>());
        bandRepository.save(band);

        Performance performance = new Performance(null, "TestLocation",
                LocalDate.of(2024,1,1),LocalTime.of(10,0),
                new HashSet<>(), new HashSet<>());
        performance.getBands().add(band);
        performanceRepository.save(performance);

        user.getBands().add(band);
        userRepository.save(user);

        band.getUsers().add(user);
        bandRepository.save(band);

        PerformanceMemberId performanceMemberId =
                new PerformanceMemberId(user.getId(), band.getId(), performance.getId());
        PerformanceMember performanceMember = new PerformanceMember(performanceMemberId, user, band, performance,
                Boolean.TRUE);
        performanceMemberRepository.save(performanceMember);

        performanceMemberRepository.deleteByPerformanceId(performance.getId());
        Optional<PerformanceMember> foundPerformanceMember = performanceMemberRepository
                .findByUserIdAndBandIdAndPerformanceId(user.getId(), band.getId(), performance.getId());
        assertFalse(foundPerformanceMember.isPresent());

    }
}
