package uk.ac.sheffield.bandproject.Service;

import org.springframework.stereotype.Service;
import uk.ac.sheffield.bandproject.Model.Instrument;
import uk.ac.sheffield.bandproject.Model.MusicSet;
import uk.ac.sheffield.bandproject.Repository.InstrumentRepository;
import uk.ac.sheffield.bandproject.Repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class InstrumentServiceImpl implements InstrumentService {

    private final InstrumentRepository instrumentRepository;

    //Constructor
    public InstrumentServiceImpl(InstrumentRepository instrumentRepository) {
        this.instrumentRepository = instrumentRepository;
    }

    public List<Instrument> getAllInstruments() {
        return instrumentRepository.findAll();
    }

    public Instrument saveInstrument(Instrument instrument) {
        return instrumentRepository.save(instrument);
    }

    public Optional<Instrument> getInstrumentById(Long id) {
        return instrumentRepository.findById(id);
    }

    //Updates an existing Instrument in the database.
    public Instrument updateInstrument(Long id, Instrument updatedInstrument) {
        Instrument instrument = instrumentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Instrument not found"));

        //Update the Instrument's details with the new data
        instrument.setSerialNumber(updatedInstrument.getSerialNumber());
        instrument.setName(updatedInstrument.getName());
        instrument.setMake(updatedInstrument.getMake());
        return instrumentRepository.save(instrument);
    }

    // Deletes an existing Instrument from the database
    public void deleteInstrument(Long id) {
        Instrument instrument = instrumentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Instrument not found"));
        instrumentRepository.delete(instrument);
    }

}
