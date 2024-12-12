package uk.ac.sheffield.bandproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.ac.sheffield.bandproject.Model.Band;
import uk.ac.sheffield.bandproject.Model.Instrument;
import uk.ac.sheffield.bandproject.Model.MusicSet;
import uk.ac.sheffield.bandproject.Repository.BandRepository;
import uk.ac.sheffield.bandproject.Repository.MusicSetRepository;
import uk.ac.sheffield.bandproject.Service.MusicSetServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class MusicSetServiceImplTest {

    @Mock
    private MusicSetRepository musicSetRepository;

    @Mock
    private BandRepository bandRepository;

    @InjectMocks
    private MusicSetServiceImpl musicSetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnAllMusicSets_whenGetAllMusicSetsIsCalled() {
        MusicSet musicSet1 = new MusicSet(1L, "TestMusicSet1", "TestComposer1",
                "TestArranger1", Boolean.TRUE, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>());
        MusicSet musicSet2 = new MusicSet(2L, "TestMusicSet2", "TestComposer2",
                "TestArranger2", Boolean.FALSE, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>());
        when(musicSetRepository.findAll()).thenReturn(Arrays.asList(musicSet1, musicSet2));

        List<MusicSet> musicSets = musicSetService.getAllMusicSets();
        assertEquals(2, musicSets.size());
        assertEquals("TestMusicSet1", musicSets.get(0).getTitle());
        assertEquals("TestMusicSet2", musicSets.get(1).getTitle());
    }

    @Test
    public void shouldReturnMusicSet_whenValidIdIsGiven() {
        MusicSet musicSet = new MusicSet(1L, "TestMusicSet", "TestComposer",
                "TestArranger", Boolean.TRUE, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>());
        when(musicSetRepository.findById(1L)).thenReturn(Optional.of(musicSet));

        Optional<MusicSet> foundMusicSet = musicSetService.getMusicSetById(1L);
        assertTrue(foundMusicSet.isPresent());
        assertEquals("TestMusicSet", foundMusicSet.get().getTitle());
    }

    @Test
    public void shouldSaveMusicSet_whenValidMusicSetIsProvided() {
        MusicSet musicSet = new MusicSet(1L, "TestMusicSet", "TestComposer",
                "TestArranger", Boolean.TRUE, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>());
        when(musicSetRepository.save(musicSet)).thenReturn(musicSet);

        MusicSet savedMusicSet = musicSetService.saveMusicSet(musicSet);
        assertNotNull(savedMusicSet);
        assertEquals("TestMusicSet", savedMusicSet.getTitle());
    }

    @Test
    public void shouldUpdateMusicSet_whenValidIdAndUpdatedMusicSetAreProvided() {
        MusicSet existingMusicSet = new MusicSet(1L, "TestMusicSet1", "TestComposer1",
                "TestArranger1", Boolean.TRUE, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>());
        MusicSet updatedMusicSet = new MusicSet(1L, "TestMusicSet2", "TestComposer2",
                "TestArranger2", Boolean.FALSE, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>());
        when(musicSetRepository.findById(1L)).thenReturn(Optional.of(existingMusicSet));
        when(musicSetRepository.save(existingMusicSet)).thenReturn(updatedMusicSet);

        MusicSet result = musicSetService.updateMusicSet(1L, updatedMusicSet);
        assertNotNull(result);
        assertEquals("TestMusicSet2", result.getTitle());
        assertEquals("TestComposer2", result.getComposer());
        assertEquals("TestArranger2", result.getArranger());
    }

    @Test
    public void shouldReturnMusicSet_whenValidBandNameIsGiven() {
        Band band = new Band(1L, "TestBand", new HashSet<>(), new HashSet<>(), new HashSet<>());
        Set<Band> bands = new HashSet<>();
        bands.add(band);

        MusicSet musicSet1 = new MusicSet(1L, "TestMusicSet1", "TestComposer1",
                "TestArranger1", Boolean.TRUE, new HashSet<>(), bands, new HashSet<>(),
                new HashSet<>());
        MusicSet musicSet2 = new MusicSet(2L, "TestMusicSet2", "TestComposer2",
                "TestArranger2", Boolean.FALSE, new HashSet<>(), bands, new HashSet<>(),
                new HashSet<>());
        when(musicSetRepository.findByBandName("TestBand")).thenReturn(List.of(musicSet1, musicSet2));

        List<MusicSet> musicSets = musicSetService.getMusicSetsByBand("TestBand");
        assertEquals(2, musicSets.size());
        assertEquals("TestMusicSet1", musicSets.get(0).getTitle());
        assertEquals("TestMusicSet2", musicSets.get(1).getTitle());
    }

    @Test
    public void shouldAddBandToMusicSet_whenValidMusicSetIdAndBandIdAreProvided() {
        Band band = new Band(1L, "TestBand", new HashSet<>(), new HashSet<>(), new HashSet<>());
        when(bandRepository.findById(1L)).thenReturn(Optional.of(band));
        Set<Band> bands = new HashSet<>();
        bands.add(band);

        MusicSet existingMusicSet = new MusicSet(1L, "TestMusicSet", "TestComposer",
                "TestArranger", Boolean.TRUE, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>());
        MusicSet updatedMusicSet = new MusicSet(1L, "TestMusicSet", "TestComposer",
                "TestArranger", Boolean.TRUE, new HashSet<>(), bands, new HashSet<>(),
                new HashSet<>());
        when(musicSetRepository.findById(1L)).thenReturn(Optional.of(existingMusicSet));
        when(musicSetRepository.save(existingMusicSet)).thenReturn(updatedMusicSet);

        MusicSet result = musicSetService.addBandToMusicSet(1L, 1L);
        assertNotNull(result);
        assertEquals(1, result.getBands().size());
        assertTrue(result.getBands().contains(band));
    }

    @Test
    public void shouldDeleteBandsFromMusicSet_whenValidMusicSetIdIsGiven() {
        Band band = new Band(1L, "TestBand", new HashSet<>(), new HashSet<>(), new HashSet<>());
        Set<Band> bands = new HashSet<>();
        bands.add(band);

        MusicSet musicSet = new MusicSet(1L, "TestMusicSet", "TestComposer",
                "TestArranger", Boolean.TRUE, new HashSet<>(), bands, new HashSet<>(),
                new HashSet<>());
        when(musicSetRepository.findById(1L)).thenReturn(Optional.of(musicSet));

        musicSetService.deletePractice(1L);
        assertTrue(musicSet.getBands().isEmpty());
        verify(musicSetRepository, times(1)).save(musicSet);
    }
}
