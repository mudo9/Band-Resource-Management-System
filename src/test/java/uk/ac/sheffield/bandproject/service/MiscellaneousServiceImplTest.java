package uk.ac.sheffield.bandproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.ac.sheffield.bandproject.Model.Instrument;
import uk.ac.sheffield.bandproject.Model.Miscellaneous;
import uk.ac.sheffield.bandproject.Repository.MiscellaneousRepository;
import uk.ac.sheffield.bandproject.Service.MiscellaneousServiceImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class MiscellaneousServiceImplTest {

    @Mock
    private MiscellaneousRepository miscellaneousRepository;

    @InjectMocks
    private MiscellaneousServiceImpl miscellaneousService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnAllMiscellaneous_whenGetAllMiscellaneousIsCalled() {
        Miscellaneous miscellaneous1 = new Miscellaneous(1L, "TestMiscellaneous1", "TestMake1",
                1, "TestInstrument1", new HashSet<>());
        Miscellaneous miscellaneous2 = new Miscellaneous(2L, "TestMiscellaneous2", "TestMake2",
                2, "TestInstrument2", new HashSet<>());
        when(miscellaneousRepository.findAll()).thenReturn(Arrays.asList(miscellaneous1, miscellaneous2));

        List<Miscellaneous> miscellaneous = miscellaneousService.getAllMiscellaneous();
        assertEquals(2, miscellaneous.size());
        assertEquals("TestMiscellaneous1", miscellaneous.get(0).getName());
        assertEquals("TestMiscellaneous2", miscellaneous.get(1).getName());
    }

    @Test
    public void shouldReturnMiscellaneous_whenValidIdIsGiven() {
        Miscellaneous miscellaneous = new Miscellaneous(1L, "TestMiscellaneous1", "TestMake1",
                1, "TestInstrument1", new HashSet<>());
        when(miscellaneousRepository.findById(1L)).thenReturn(Optional.of(miscellaneous));

        Optional<Miscellaneous> foundMiscellaneous = miscellaneousService.getMiscellaneousById(1L);
        assertTrue(foundMiscellaneous.isPresent());
        assertEquals("TestMiscellaneous1", foundMiscellaneous.get().getName());
    }

    @Test
    public void shouldSaveMiscellaneous_whenValidMiscellaneousIsProvided() {
        Miscellaneous miscellaneous = new Miscellaneous(1L, "TestMiscellaneous1", "TestMake1",
                1, "TestInstrument1", new HashSet<>());
        when(miscellaneousRepository.save(miscellaneous)).thenReturn(miscellaneous);

        Miscellaneous savedMiscellaneous = miscellaneousService.saveMiscellaneous(miscellaneous);
        assertNotNull(savedMiscellaneous);
        assertEquals("TestMiscellaneous1", savedMiscellaneous.getName());
    }

    @Test
    public void shouldUpdateMiscellaneous_whenValidIdAndUpdatedMiscellaneousAreProvided() {
        Miscellaneous existingMiscellaneous = new Miscellaneous(1L, "TestMiscellaneous1", "TestMake1",
                1, "TestInstrument1", new HashSet<>());
        Miscellaneous updatedMiscellaneous = new Miscellaneous(1L, "TestMiscellaneous2", "TestMake2",
                2, "TestInstrument2", new HashSet<>());
        when(miscellaneousRepository.findById(1L)).thenReturn(Optional.of(existingMiscellaneous));
        when(miscellaneousRepository.save(existingMiscellaneous)).thenReturn(updatedMiscellaneous);

        Miscellaneous result = miscellaneousService.updateMiscellaneous(1L, updatedMiscellaneous);
        assertNotNull(result);
        assertEquals("TestMiscellaneous2", result.getName());
        assertEquals("TestMake2", result.getMake());
        assertEquals(2, result.getQuantity());
    }

    @Test
    public void shouldDeleteMiscellaneous() {
        Miscellaneous miscellaneous = new Miscellaneous(1L, "TestMiscellaneous1", "TestMake1",
                1, "TestInstrument1", new HashSet<>());
        when(miscellaneousRepository.findById(1L)).thenReturn(Optional.of(miscellaneous));
        doNothing().when(miscellaneousRepository).delete(miscellaneous);

        miscellaneousService.deleteMiscellaneous(1L);
        verify(miscellaneousRepository, times(1)).delete(miscellaneous);
    }
}
