package uk.ac.sheffield.bandproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.ac.sheffield.bandproject.Model.*;
import uk.ac.sheffield.bandproject.Repository.BandRepository;
import uk.ac.sheffield.bandproject.Repository.MusicSetRepository;
import uk.ac.sheffield.bandproject.Repository.PerformanceMemberRepository;
import uk.ac.sheffield.bandproject.Repository.PerformanceRepository;
import uk.ac.sheffield.bandproject.Service.PerformanceServiceImpl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PerformanceServiceImplTest {
    @Mock
    private PerformanceRepository performanceRepository;

    @Mock
    private BandRepository bandRepository;

    @Mock
    private MusicSetRepository musicSetRepository;

    @Mock
    private PerformanceMemberRepository performanceMemberRepository;

    @InjectMocks
    private PerformanceServiceImpl performanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnAllPerformances_whenGetAllPerformancesIsCalled() {
        Performance performance1 = new Performance(1L, "TestLocation1",
                LocalDate.of(2024,1,1),LocalTime.of(10,0),
                new HashSet<>(), new HashSet<>());
        Performance performance2 = new Performance(2L, "TestLocation2",
                LocalDate.of(2024,2,2),LocalTime.of(20,0),
                new HashSet<>(), new HashSet<>());
        when(performanceRepository.findAll()).thenReturn(Arrays.asList(performance1, performance2));

        List<Performance> performances = performanceService.getAllPerformances();
        assertEquals(2, performances.size());
        assertEquals("TestLocation1", performances.get(0).getLocation());
        assertEquals("TestLocation2", performances.get(1).getLocation());
    }

    @Test
    public void shouldReturnPerformance_whenValidIdIsGiven() {
        Performance performance = new Performance(1L, "TestLocation",
                LocalDate.of(2024,1,1),LocalTime.of(10,0),
                new HashSet<>(), new HashSet<>());
        when(performanceRepository.findById(1L)).thenReturn(Optional.of(performance));

        Optional<Performance> foundPerformance = performanceService.getPerformanceById(1L);
        assertTrue(foundPerformance.isPresent());
        assertEquals("TestLocation", foundPerformance.get().getLocation());
    }

    @Test
    public void shouldReturnAllPerformanceOfBand_whenGetPerformanceByBandIsCalled() {
        Band band = new Band(1L, "TestBand", new HashSet<>(), new HashSet<>(), new HashSet<>());
        Set<Band> bands = new HashSet<>();
        bands.add(band);

        Performance performance1 = new Performance(null, "TestLocation1",
                LocalDate.of(2024,1,1),LocalTime.of(10,0),
                bands, new HashSet<>());
        Performance performance2 = new Performance(null, "TestLocation2",
                LocalDate.of(2024,2,2),LocalTime.of(20,0),
                bands, new HashSet<>());
        when(performanceRepository.findByBand(1L)).thenReturn(Arrays.asList(performance1, performance2));

        List<Performance> performances = performanceService.getPerformanceByBand(1L);
        assertEquals(2, performances.size());
        assertEquals("TestLocation1", performances.get(0).getLocation());
        assertEquals("TestLocation2", performances.get(1).getLocation());
    }

    @Test
    public void shouldSavePerformance_whenValidIdIsGiven() {
        Performance performance = new Performance(1L, "TestLocation",
                LocalDate.of(2024,1,1),LocalTime.of(10,0),
                new HashSet<>(), new HashSet<>());
        when(performanceRepository.save(performance)).thenReturn(performance);

        Performance savedPerformance = performanceService.savePerformance(performance);
        assertNotNull(savedPerformance);
        assertEquals("TestLocation", savedPerformance.getLocation());
    }

    @Test
    public void shouldUpdatePerformance_whenValidIdAndUpdatedPerformanceIsGiven() {
        Performance existingPerformance = new Performance(1L, "TestLocation1",
                LocalDate.of(2024,1,1),LocalTime.of(10,0),
                new HashSet<>(), new HashSet<>());
        Performance updatedPerformance = new Performance(1L, "TestLocation2",
                LocalDate.of(2024,2,2),LocalTime.of(20,0),
                new HashSet<>(), new HashSet<>());
        when(performanceRepository.findById(1L)).thenReturn(Optional.of(existingPerformance));
        when(performanceRepository.save(existingPerformance)).thenReturn(existingPerformance);

        Performance result = performanceService.updatePerformance(1L, updatedPerformance);
        assertNotNull(result);
        assertEquals("TestLocation2", result.getLocation());
        assertEquals(LocalDate.of(2024,2,2), result.getDate());
        assertEquals(LocalTime.of(20,00), result.getTime());
    }

    @Test
    public void shouldDeletePerformance() {
        Band band = new Band(1L, "TestBand", new HashSet<>(), new HashSet<>(), new HashSet<>());
        Set<Band> bands = new HashSet<>();
        bands.add(band);

        MusicSet musicSet = new MusicSet(1L, "TestMusicSet", "TestComposer",
                "TestArranger", Boolean.TRUE, new HashSet<>(), bands, new HashSet<>(),
                new HashSet<>());
        Set<MusicSet> musicSets = new HashSet<>();
        musicSets.add(musicSet);

        Performance performance = new Performance(1L, "TestLocation",
                LocalDate.of(2024,1,1),LocalTime.of(10,0),
                bands, musicSets);

        band.getPerformances().add(performance);
        musicSet.getPerformances().add(performance);

        when(performanceRepository.findById(1L)).thenReturn(Optional.of(performance));
        when(bandRepository.saveAll(bands)).thenReturn(new ArrayList<>(List.of(band)));
        when(musicSetRepository.saveAll(musicSets)).thenReturn(new ArrayList<>(List.of(musicSet)));
        doNothing().when(performanceMemberRepository).deleteByPerformanceId(1L);
        doNothing().when(performanceRepository).deleteById(1L);

        performanceService.deletePerformance(1L);
        verify(bandRepository, times(1)).saveAll(bands);
        verify(musicSetRepository, times(1)).saveAll(musicSets);
        verify(performanceRepository, times(1)).deleteById(1L);
        verify(performanceMemberRepository, times(1)).deleteByPerformanceId(1L);
    }

    @Test
    public void shouldAddBandToPerformance_whenValidIdsAreGiven() {
        User user = new User(1L, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        Set<User> users = new HashSet<>();
        users.add(user);

        Band existingBand = new Band(1L, "TestBand", new HashSet<>(), users, new HashSet<>());
        Set<Band> bands = new HashSet<>();
        bands.add(existingBand);

        Performance existingPerformance = new Performance(1L, "TestLocation1",
                LocalDate.of(2024,1,1),LocalTime.of(10,0),
                new HashSet<>(), new HashSet<>());
        Performance updatedPerformance = new Performance(1L, "TestLocation1",
                LocalDate.of(2024,1,1),LocalTime.of(10,0),
                bands, new HashSet<>());

        Band updatedBand = new Band(1L, "TestBand", new HashSet<>(), users, new HashSet<>());
        updatedBand.getPerformances().add(existingPerformance);

        user.getBands().add(updatedBand);

        PerformanceMemberId performanceMemberId = new PerformanceMemberId(1L, 1L, 1L);
        PerformanceMember performanceMember = new PerformanceMember();
        performanceMember.setPerformanceMemberId(performanceMemberId);
        performanceMember.setUser(user);
        performanceMember.setBand(existingBand);
        performanceMember.setPerformance(existingPerformance);

        when(performanceRepository.findById(1L)).thenReturn(Optional.of(existingPerformance));
        when(bandRepository.findById(1L)).thenReturn(Optional.of(existingBand));
        when(performanceRepository.save(existingPerformance)).thenReturn(updatedPerformance);
        when(performanceMemberRepository.existsById(performanceMemberId)).thenReturn(false);
        when(performanceMemberRepository.save(performanceMember)).thenReturn(performanceMember);

        performanceService.addBandToPerformance(1L, 1L);
        verify(performanceRepository, times(1)).findById(1L);
        verify(bandRepository, times(1)).findById(1L);
        verify(performanceRepository, times(1)).save(existingPerformance);
        verify(bandRepository, times(1)).save(existingBand);
    }

    @Test
    public void shouldRemoveBandToPerformance_whenValidIdsAreGiven() {
        User user = new User(1L, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        Set<User> users = new HashSet<>();
        users.add(user);

        Band band = new Band(1L, "TestBand", new HashSet<>(), users, new HashSet<>());
        Set<Band> bands = new HashSet<>();
        bands.add(band);

        user.setBands(bands);

        Performance performance = new Performance(1L, "TestLocation1",
                LocalDate.of(2024,1,1),LocalTime.of(10,0),
                bands, new HashSet<>());

        PerformanceMemberId performanceMemberId = new PerformanceMemberId(1L, 1L, 1L);

        when(performanceRepository.findById(1L)).thenReturn(Optional.of(performance));
        when(bandRepository.findById(1L)).thenReturn(Optional.of(band));
        doNothing().when(performanceMemberRepository).deleteById(performanceMemberId);

        performanceService.removeBandFromPerformance(1L, 1L);
        assertTrue(performance.getBands().isEmpty());
        assertTrue(band.getPerformances().isEmpty());
        verify(performanceRepository, times(1)).save(performance);
    }
}
