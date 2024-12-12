package uk.ac.sheffield.bandproject.Service;

import uk.ac.sheffield.bandproject.Model.Band;
import uk.ac.sheffield.bandproject.Model.User;

import java.util.List;
import java.util.Optional;

public interface BandService {
    List<Band> getAllBands();
    Optional<Band> getBandById(Long id);
    Optional<Band> getBandByName(String bandName);
}
