package uk.ac.sheffield.bandproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.ac.sheffield.bandproject.Model.MusicOrder;
import uk.ac.sheffield.bandproject.Model.MusicPart;
import uk.ac.sheffield.bandproject.Model.MusicSet;
import uk.ac.sheffield.bandproject.Model.User;
import uk.ac.sheffield.bandproject.Repository.MusicPartRepository;
import uk.ac.sheffield.bandproject.Repository.MusicSetRepository;
import uk.ac.sheffield.bandproject.Service.MusicPartServiceImpl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MusicPartServiceImplTest {

    @Mock
    private MusicPartRepository musicPartRepository;

    @Mock
    private MusicSetRepository musicSetRepository;

    @InjectMocks
    private MusicPartServiceImpl musicPartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnAllMusicParts_whenGetAllMusicPartsIsCalled() {
        MusicSet musicSet = new MusicSet(1L, "TestMusicSet", "TestComposer", "TestArranger",
                Boolean.TRUE, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        MusicPart musicPart1 = new MusicPart(1L, "TestPart1", musicSet, new HashSet<>());
        MusicPart musicPart2 = new MusicPart(2L, "TestPart2", musicSet, new HashSet<>());
        when(musicPartRepository.findAll()).thenReturn(Arrays.asList(musicPart1, musicPart2));

        List<MusicPart> musicParts = musicPartService.getAllMusicParts();
        assertEquals(2, musicParts.size());
        assertEquals("TestPart1", musicParts.get(0).getPartName());
        assertEquals("TestPart2", musicParts.get(1).getPartName());
    }

    @Test
    public void shouldReturnMusicPart_whenValidIdIsGiven() {
        MusicSet musicSet = new MusicSet(1L, "TestMusicSet", "TestComposer", "TestArranger",
                Boolean.TRUE, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        MusicPart musicPart = new MusicPart(1L, "TestPart", musicSet, new HashSet<>());
        when(musicPartRepository.findById(1L)).thenReturn(Optional.of(musicPart));

        Optional<MusicPart> foundMusicParts = musicPartService.getMusicPartById(1L);
        assertTrue(foundMusicParts.isPresent());
        assertEquals("TestPart", foundMusicParts.get().getPartName());
    }

    @Test
    public void shouldCreateMusicPart_whenValidIdAndMusicPartAreProvided() {
        MusicSet musicSet1 = new MusicSet(1L, "TestMusicSet1", "TestComposer1", "TestArranger1",
                Boolean.TRUE, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        MusicSet musicSet2 = new MusicSet(2L, "TestMusicSet2", "TestComposer2", "TestArranger2",
                Boolean.TRUE, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        MusicPart existingMusicPart = new MusicPart(1L, "TestPart", musicSet1, new HashSet<>());
        MusicPart updatedMusicPart = new MusicPart(1L, "TestPart", musicSet2, new HashSet<>());
        when(musicSetRepository.findById(2L)).thenReturn(Optional.of(musicSet2));
        when(musicPartRepository.save(existingMusicPart)).thenReturn(updatedMusicPart);

        MusicPart result = musicPartService.createMusicPart(2L, existingMusicPart);
        assertNotNull(result);
        assertEquals("TestPart", result.getPartName());
        assertEquals(musicSet2, result.getMusicSet());
    }

    @Test
    public void shouldSaveMusicPart_whenValidMusicPartAreProvided() {
        MusicSet musicSet = new MusicSet(1L, "TestMusicSet", "TestComposer", "TestArranger",
                Boolean.TRUE, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        MusicPart musicPart = new MusicPart(1L, "TestPart", musicSet, new HashSet<>());
        when(musicPartRepository.save(musicPart)).thenReturn(musicPart);

        MusicPart savedMusicPart = musicPartService.saveMusicPart(musicPart);
        assertNotNull(savedMusicPart);
        assertEquals("TestPart", savedMusicPart.getPartName());
    }

    @Test
    public void shouldUpdateMusicPart_whenValidIdAndUpdatedMusicPartAreProvided() {
        MusicSet musicSet1 = new MusicSet(1L, "TestMusicSet1", "TestComposer1", "TestArranger1",
                Boolean.TRUE, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        MusicSet musicSet2 = new MusicSet(1L, "TestMusicSet2", "TestComposer2", "TestArranger2",
                Boolean.TRUE, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        MusicPart existingMusicPart = new MusicPart(1L, "TestPart", musicSet1, new HashSet<>());
        MusicPart updatedMusicPart = new MusicPart(1L, "TestPart2", musicSet2, new HashSet<>());
        when(musicPartRepository.findById(1L)).thenReturn(Optional.of(existingMusicPart));
        when(musicPartRepository.save(existingMusicPart)).thenReturn(updatedMusicPart);

        MusicPart result = musicPartService.updateMusicPart(1L, updatedMusicPart);
        assertNotNull(result);
        assertEquals("TestPart2", result.getPartName());
        assertEquals(musicSet2, result.getMusicSet());
    }

    @Test
    public void shouldReturnAllMusicPartsOfMusicSet_whenGetPartsByMusicSetIdIsCalled() {
        MusicSet musicSet = new MusicSet(1L, "TestMusicSet", "TestComposer", "TestArranger",
                Boolean.TRUE, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        MusicPart musicPart1 = new MusicPart(1L, "TestPart1", musicSet, new HashSet<>());
        MusicPart musicPart2 = new MusicPart(2L, "TestPart2", musicSet, new HashSet<>());
        when(musicPartRepository.findByMusicSetId(1L)).thenReturn(Arrays.asList(musicPart1, musicPart2));

        List<MusicPart> musicParts = musicPartService.getPartsByMusicSetId(1L);
        assertEquals(2, musicParts.size());
        assertEquals("TestPart1", musicParts.get(0).getPartName());
        assertEquals("TestPart2", musicParts.get(1).getPartName());
    }

    @Test
    public void shouldReturnSpecificMusicPart_whenGetMusicPartForOrderIsCalled() {
        MusicSet musicSet = new MusicSet(1L, "TestMusicSet", "TestComposer", "TestArranger",
                Boolean.TRUE, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        MusicPart musicPart = new MusicPart(1L, "TestPart", musicSet, new HashSet<>());
        when(musicPartRepository.findSpecificMusicPart("TestPart", "TestMusicSet",
                "TestArranger")).thenReturn(Optional.of(musicPart));

        Optional<MusicPart> foundMusicPart = musicPartService.getMusicPartForOrder("TestPart",
                "TestMusicSet", "TestArranger");
        assertTrue(foundMusicPart.isPresent());
        assertEquals("TestPart", foundMusicPart.get().getPartName());
    }
    
    @Test
    public void shouldReturnAllMusicPartsOfUser_whenGetUserMusicPartIsCalled() {
        User owner = new User(1L, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        MusicSet musicSet = new MusicSet(1L, "TestMusicSet", "TestComposer", "TestArranger",
                Boolean.TRUE, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        MusicPart musicPart1 = new MusicPart(1L, "TestPart1", musicSet, new HashSet<>());
        MusicPart musicPart2 = new MusicPart(2L, "TestPart2", musicSet, new HashSet<>());
        MusicOrder musicOrder = new MusicOrder(1L, owner, null, LocalDate.of(2024,1,1),
                "FULFILLED", new HashSet<>());
        
        musicOrder.getMusicParts().add(musicPart1);
        musicOrder.getMusicParts().add(musicPart2);
        musicPart1.getMusicOrders().add(musicOrder);
        musicPart2.getMusicOrders().add(musicOrder);
        
        when(musicPartRepository.findAllByOwnerIdAndFulfilledOrders(1L))
                .thenReturn(Arrays.asList(musicPart1, musicPart2));
        
        List<MusicPart> musicParts = musicPartService.getUserMusicPart(1L);
        assertEquals(2, musicParts.size());
        assertEquals("TestPart1", musicParts.get(0).getPartName());
        assertEquals("TestPart2", musicParts.get(1).getPartName());
    }

    @Test
    public void shouldReturnAllMusicPartOfChild_whenGetChildMusicPartIsCalled() {
        User parent = new User(1L, "test@test.com", "password", "1234567",
                "TestUser", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        User child = new User(2L, "test@test.com", "password", "1234567",
                "TestChild", null, new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        MusicSet musicSet = new MusicSet(1L, "TestMusicSet", "TestComposer", "TestArranger",
                Boolean.TRUE, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        MusicPart musicPart1 = new MusicPart(1L, "TestPart1", musicSet, new HashSet<>());
        MusicPart musicPart2 = new MusicPart(2L, "TestPart2", musicSet, new HashSet<>());
        MusicOrder musicOrder = new MusicOrder(1L, parent, child, LocalDate.of(2024,1,1),
                "FULFILLED", new HashSet<>());

        musicOrder.getMusicParts().add(musicPart1);
        musicOrder.getMusicParts().add(musicPart2);
        musicPart1.getMusicOrders().add(musicOrder);
        musicPart2.getMusicOrders().add(musicOrder);

        when(musicPartRepository.findAllByChildIdAndFulfilledOrders(2L))
                .thenReturn(Arrays.asList(musicPart1, musicPart2));

        List<MusicPart> musicParts = musicPartService.getChildMusicPart(2L);
        assertEquals(2, musicParts.size());
        assertEquals("TestPart1", musicParts.get(0).getPartName());
        assertEquals("TestPart2", musicParts.get(1).getPartName());
    }
}
