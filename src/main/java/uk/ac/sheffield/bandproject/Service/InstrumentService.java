package uk.ac.sheffield.bandproject.Service;

import uk.ac.sheffield.bandproject.Model.Instrument;

import java.util.List;
import java.util.Optional;

public interface InstrumentService {
    List<Instrument> getAllInstruments();
    Instrument saveInstrument(Instrument instrument);
    Instrument updateInstrument(Long id, Instrument updatedInstrument);
    Optional<Instrument> getInstrumentById(Long id);
    void deleteInstrument(Long id);

}
