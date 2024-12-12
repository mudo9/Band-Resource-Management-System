package uk.ac.sheffield.bandproject.Service;

import org.springframework.stereotype.*;

import uk.ac.sheffield.bandproject.Model.Band;
import uk.ac.sheffield.bandproject.Model.MusicPart;
import uk.ac.sheffield.bandproject.Model.MusicSet;
import uk.ac.sheffield.bandproject.Model.User;
import uk.ac.sheffield.bandproject.Repository.MusicPartRepository;
import uk.ac.sheffield.bandproject.Repository.MusicSetRepository;
import uk.ac.sheffield.bandproject.Repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MusicPartServiceImpl implements MusicPartService {
    private final MusicPartRepository musicPartRepository;
    private final MusicSetRepository musicSetRepository;
    private final UserRepository userRepository;
    //Constructor
    public MusicPartServiceImpl(MusicPartRepository musicPartRepository, MusicSetRepository musicSetRepository,
                                UserRepository userRepository) {
        this.musicPartRepository = musicPartRepository;
        this.musicSetRepository = musicSetRepository;
        this.userRepository = userRepository;
    }

    //Retrieves all MusicPart records from the database
    public List<MusicPart> getAllMusicParts() {
        return musicPartRepository.findAll();
    }

    //Retrieves a specific MusicPart
    public Optional<MusicPart> getMusicPartById(Long id) {
        return musicPartRepository.findById(id);
    }

    //Creates a new MusicPart and associates it with a MusicSet, then saves it to the repository
    public MusicPart createMusicPart(Long musicSetId, MusicPart musicPart) {
        MusicSet musicSet = musicSetRepository.findById(musicSetId)
                .orElseThrow(() -> new RuntimeException("MusicSet not found with id: " + musicSetId));
        musicPart.setMusicSet(musicSet);
        return musicPartRepository.save(musicPart);
    }

    //Saves a MusicPart to the repository
    public MusicPart saveMusicPart(MusicPart musicPart) {
        return musicPartRepository.save(musicPart);
    }

    //Updates an existing MusicPart by its ID
    public MusicPart updateMusicPart(Long id, MusicPart updatedMusicPart) {
        Optional<MusicPart> musicPartOptional = musicPartRepository.findById(id);
        if (musicPartOptional.isPresent()) {
            MusicPart musicPart = musicPartOptional.get();
            // Update the details of the existing MusicPart.
            musicPart.setPartName(updatedMusicPart.getPartName());
            return musicPartRepository.save(musicPart);
        } else {
            throw new RuntimeException("Music Part not found");
        }
    }


    //Retrieves all MusicParts associated with a specific MusicSet
    public List<MusicPart> getPartsByMusicSetId(Long musicSetId) {
        return musicPartRepository.findByMusicSetId(musicSetId);
    }

    //Retrieves MusicParts needed by a user, based on their associated bands and the MusicSets
    public List<MusicPart> getUserMusicPartNeeded(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<Band> bands = user.getBands();
        if (bands.isEmpty()) {
            return new ArrayList<>();
        }

        //Collect all MusicSets from the user's bands and then collect all MusicParts
        Set<MusicSet> musicSets = bands.stream()
                .flatMap(band -> band.getMusicSets().stream())
                .collect(Collectors.toSet());

        return musicSets.stream()
                .flatMap(musicSet -> musicSet.getMusicParts().stream())
                .collect(Collectors.toList());
    }

    //Retrieves a specific MusicPart
    public Optional<MusicPart> getMusicPartForOrder(String musicPartName, String musicSetTitle,
                                                    String musicSetArranger) {
        return musicPartRepository.findSpecificMusicPart(musicPartName, musicSetTitle, musicSetArranger);
    }

    //Retrieves all MusicParts associated with a user
    public List<MusicPart> getUserMusicPart(Long ownerId) {
        return musicPartRepository.findAllByOwnerIdAndFulfilledOrders(ownerId);
    }

    //Retrieves all MusicParts associated with a child
    public List<MusicPart> getChildMusicPart(Long childId) {
        return musicPartRepository.findAllByChildIdAndFulfilledOrders(childId);
    }


}
