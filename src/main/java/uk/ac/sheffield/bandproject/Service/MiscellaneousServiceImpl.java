package uk.ac.sheffield.bandproject.Service;

import org.springframework.stereotype.Service;
import uk.ac.sheffield.bandproject.Model.Miscellaneous;
import uk.ac.sheffield.bandproject.Model.MiscellaneousLoan;
import uk.ac.sheffield.bandproject.Model.User;
import uk.ac.sheffield.bandproject.Repository.MiscellaneousRepository;
import uk.ac.sheffield.bandproject.Repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MiscellaneousServiceImpl implements MiscellaneousService {

    private final MiscellaneousRepository miscellaneousRepository;

    //Constructor
    public MiscellaneousServiceImpl(MiscellaneousRepository miscellaneousRepository) {
        this.miscellaneousRepository = miscellaneousRepository;}

    public List<Miscellaneous> getAllMiscellaneous() {
        return miscellaneousRepository.findAll();
    }

    public Miscellaneous saveMiscellaneous(Miscellaneous item) {
        return miscellaneousRepository.save(item);
    }

    public Optional<Miscellaneous> getMiscellaneousById(Long id){return miscellaneousRepository.findById(id);}

    //Updates a Miscellaneous item in the database
    public Miscellaneous updateMiscellaneous(Long id, Miscellaneous updatedItem) {
        Miscellaneous item = miscellaneousRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Miscellaneous item not found"));

        // Update the item's attributes with the new values from the updated item
        item.setName(updatedItem.getName());
        item.setMake(updatedItem.getMake());
        item.setQuantity(updatedItem.getQuantity());
        item.setSpecificForInstrument(updatedItem.getSpecificForInstrument());
        return miscellaneousRepository.save(item);
    }

    //Deletes a Miscellaneous item from the database
    public void deleteMiscellaneous(Long id) {
        Miscellaneous item = miscellaneousRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Miscellaneous item not found"));
        miscellaneousRepository.delete(item);
    }

}
