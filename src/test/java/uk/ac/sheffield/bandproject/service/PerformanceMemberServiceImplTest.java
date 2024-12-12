package uk.ac.sheffield.bandproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.ac.sheffield.bandproject.Model.*;
import uk.ac.sheffield.bandproject.Repository.PerformanceMemberRepository;
import uk.ac.sheffield.bandproject.Service.PerformanceMemberServiceImpl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class PerformanceMemberServiceImplTest {

    @Mock
    private PerformanceMemberRepository performanceMemberRepository;

    @InjectMocks
    private PerformanceMemberServiceImpl performanceMemberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnPerformanceMember_whenValidIdsAreGiven() {
        User user = new User(1L, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());

        Band band = new Band(1L, "TestBand", new HashSet<>(), new HashSet<>(), new HashSet<>());

        Performance performance = new Performance(1L, "TestLocation",
                LocalDate.of(2024,1,1), LocalTime.of(10,0),
                new HashSet<>(), new HashSet<>());

        PerformanceMemberId performanceMemberId = new PerformanceMemberId(1L, 1L, 1L);
        PerformanceMember performanceMember = new PerformanceMember(performanceMemberId, user, band, performance,
                Boolean.TRUE);
        when(performanceMemberRepository.findByUserIdAndBandIdAndPerformanceId(1L, 1L, 1L))
                .thenReturn(Optional.of(performanceMember));

        Optional<PerformanceMember> foundPerformanceMember =
                performanceMemberService.findByUserIdAndBandIdAndPerformanceId(1L, 1L, 1L);
        assertTrue(foundPerformanceMember.isPresent());
        assertTrue(foundPerformanceMember.get().getAvailability());
    }

    @Test
    public void shouldReturnAllPerformanceMembers_whenPerformanceIdAndAvailabilityAreGiven() {
        User user1 = new User(1L, "test@test.com", "password", "1234567",
                "TestUser1", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        User user2 = new User(2L, "test2@test.com", "password", "9876543",
                "TestUser2", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());

        Band band = new Band(1L, "TestBand", new HashSet<>(), new HashSet<>(), new HashSet<>());

        Performance performance = new Performance(1L, "TestLocation",
                LocalDate.of(2024,1,1), LocalTime.of(10,0),
                new HashSet<>(), new HashSet<>());

        PerformanceMemberId performanceMemberId1 = new PerformanceMemberId(1L, 1L, 1L);
        PerformanceMember performanceMember1 = new PerformanceMember(performanceMemberId1, user1, band, performance,
                Boolean.TRUE);

        PerformanceMemberId performanceMemberId2 = new PerformanceMemberId(2L, 1L, 1L);
        PerformanceMember performanceMember2 = new PerformanceMember(performanceMemberId2, user2, band, performance,
                Boolean.TRUE);
        when(performanceMemberRepository.findByPerformanceIdAndAvailability(1L, Boolean.TRUE))
                .thenReturn(Arrays.asList(performanceMember1, performanceMember2));

        List<PerformanceMember> performanceMembers =
                performanceMemberService.findByPerformanceIdAndAvailability(1L, Boolean.TRUE);
        assertEquals(2, performanceMembers.size());
        assertEquals("TestUser1", performanceMembers.get(0).getUser().getFullName());
        assertEquals("TestUser2", performanceMembers.get(1).getUser().getFullName());

    }

    @Test
    public void shouldSavePerformanceMember_whenValidPerformanceMemberIsProvided() {
        User user = new User(1L, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());

        Band band = new Band(1L, "TestBand", new HashSet<>(), new HashSet<>(), new HashSet<>());

        Performance performance = new Performance(1L, "TestLocation",
                LocalDate.of(2024,1,1), LocalTime.of(10,0),
                new HashSet<>(), new HashSet<>());

        PerformanceMemberId performanceMemberId = new PerformanceMemberId(1L, 1L, 1L);
        PerformanceMember performanceMember = new PerformanceMember(performanceMemberId, user, band, performance,
                Boolean.TRUE);
        when(performanceMemberRepository.save(performanceMember)).thenReturn(performanceMember);

        PerformanceMember savedPerformanceMember = performanceMemberService.savePerformanceMember(performanceMember);
        assertNotNull(savedPerformanceMember);
        assertTrue(savedPerformanceMember.getAvailability());
    }

    @Test
    public void shouldUpdatePerformanceMember_whenValidIdsAndUpdatedPerformanceMemberAreProvided() {
        User user = new User(1L, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());

        Band band = new Band(1L, "TestBand", new HashSet<>(), new HashSet<>(), new HashSet<>());

        Performance performance = new Performance(1L, "TestLocation",
                LocalDate.of(2024,1,1), LocalTime.of(10,0),
                new HashSet<>(), new HashSet<>());

        PerformanceMemberId performanceMemberId = new PerformanceMemberId(1L, 1L, 1L);
        PerformanceMember existingPerformanceMember = new PerformanceMember(performanceMemberId, user, band, performance,
                Boolean.TRUE);
        PerformanceMember updatedPerformanceMember = new PerformanceMember(performanceMemberId, user, band, performance,
                Boolean.FALSE);
        when(performanceMemberRepository.findByUserIdAndBandIdAndPerformanceId(1L, 1L, 1L))
                .thenReturn(Optional.of(existingPerformanceMember));
        when(performanceMemberRepository.save(existingPerformanceMember)).thenReturn(updatedPerformanceMember);

        PerformanceMember result = performanceMemberService.updatePerformanceMember(1L, 1L, 1L,
                updatedPerformanceMember);
        assertNotNull(result);
        assertFalse(result.getAvailability());
    }
}
