package uk.ac.sheffield.bandproject.Service;

import org.springframework.stereotype.Service;
import uk.ac.sheffield.bandproject.Model.Band;
import uk.ac.sheffield.bandproject.Repository.BandRepository;

import java.util.*;

@Service
public class BandServiceImpl implements BandService {
    private final BandRepository bandRepository;
    public BandServiceImpl(BandRepository bandRepository) {
        this.bandRepository = bandRepository;
    }

    public List<Band> getAllBands() {
        return bandRepository.findAll();
    }

    public Optional<Band> getBandById(Long id) {
        return bandRepository.findById(id);
    }

    public Optional<Band> getBandByName(String bandName) {
        return bandRepository.findByName(bandName);
    }

}
