package uk.ac.sheffield.bandproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.ac.sheffield.bandproject.Model.Instrument;
import uk.ac.sheffield.bandproject.Repository.InstrumentRepository;
import uk.ac.sheffield.bandproject.Service.InstrumentServiceImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InstrumentServiceImpltest {

    @Mock
    private InstrumentRepository instrumentRepository;

    @InjectMocks
    private InstrumentServiceImpl instrumentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnAllInstruments_whenGetAllInstrumentsIsCalled() {
        Instrument instrument1 = new Instrument(1L, "101010", "TestInstrument1", "TestMake1",
                new HashSet<>(), new HashSet<>());
        Instrument instrument2 = new Instrument(1L, "202020", "TestInstrument2", "TestMake2",
                new HashSet<>(), new HashSet<>());
        when(instrumentRepository.findAll()).thenReturn(Arrays.asList(instrument1, instrument2));

        List<Instrument> instruments = instrumentService.getAllInstruments();
        assertEquals(2, instruments.size());
        assertEquals("TestInstrument1", instruments.get(0).getName());
        assertEquals("TestInstrument2", instruments.get(1).getName());
    }

    @Test
    public void shouldReturnInstrument_whenValidIdIsGiven() {
        Instrument instrument = new Instrument(1L, "101010", "TestInstrument1", "TestMake1",
                new HashSet<>(), new HashSet<>());
        when(instrumentRepository.findById(1L)).thenReturn(Optional.of(instrument));

        Optional<Instrument> foundInstrument = instrumentService.getInstrumentById(1L);
        assertTrue(foundInstrument.isPresent());
        assertEquals("TestInstrument1", foundInstrument.get().getName());
    }

    @Test
    public void shouldSaveInstrument_whenValidInstrumentIsProvided() {
        Instrument instrument = new Instrument(1L, "101010", "TestInstrument1", "TestMake1",
                new HashSet<>(), new HashSet<>());
        when(instrumentRepository.save(instrument)).thenReturn(instrument);

        Instrument savedInstrument = instrumentService.saveInstrument(instrument);
        assertNotNull(savedInstrument);
        assertEquals("TestInstrument1", savedInstrument.getName());
    }

    @Test
    public void shouldUpdateInstrument_whenValidIdAndUpdatedInstrumentAreProvided() {
        Instrument existingInstrument = new Instrument(1L, "101010", "TestInstrument1",
                "TestMake1", new HashSet<>(), new HashSet<>());
        Instrument updatedInstrument = new Instrument(1L, "111111", "TestInstrument2",
                "TestMake2", new HashSet<>(), new HashSet<>());
        when(instrumentRepository.findById(1L)).thenReturn(Optional.of(existingInstrument));
        when(instrumentRepository.save(existingInstrument)).thenReturn(updatedInstrument);

        Instrument result = instrumentService.updateInstrument(1L, updatedInstrument);
        assertNotNull(result);
        assertEquals("111111", result.getSerialNumber());
        assertEquals("TestInstrument2", result.getName());
        assertEquals("TestMake2", result.getMake());
    }

    @Test
    public void shouldDeleteInstrument() {
        Instrument instrument = new Instrument(1L, "101010", "TestInstrument1", "TestMake1",
                new HashSet<>(), new HashSet<>());
        when(instrumentRepository.findById(1L)).thenReturn(Optional.of(instrument));
        doNothing().when(instrumentRepository).delete(instrument);

        instrumentService.deleteInstrument(1L);
        verify(instrumentRepository, times(1)).delete(instrument);
    }
}
