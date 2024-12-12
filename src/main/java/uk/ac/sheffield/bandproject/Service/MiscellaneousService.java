package uk.ac.sheffield.bandproject.Service;

import uk.ac.sheffield.bandproject.Model.Miscellaneous;

import java.util.List;
import java.util.Optional;

public interface MiscellaneousService {
    List<Miscellaneous> getAllMiscellaneous();
    Miscellaneous saveMiscellaneous(Miscellaneous item);
    Miscellaneous updateMiscellaneous(Long id, Miscellaneous updatedItem);
    Optional<Miscellaneous> getMiscellaneousById(Long id);
    void deleteMiscellaneous(Long id);
}
