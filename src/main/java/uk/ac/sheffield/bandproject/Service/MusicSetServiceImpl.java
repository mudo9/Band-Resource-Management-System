package uk.ac.sheffield.bandproject.Service;

import org.springframework.stereotype.*;

import uk.ac.sheffield.bandproject.Model.Band;
import uk.ac.sheffield.bandproject.Model.MusicPart;
import uk.ac.sheffield.bandproject.Model.MusicSet;
import uk.ac.sheffield.bandproject.Model.User;
import uk.ac.sheffield.bandproject.Repository.BandRepository;
import uk.ac.sheffield.bandproject.Repository.MusicSetRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MusicSetServiceImpl implements MusicSetService{
    private final MusicSetRepository musicSetRepository;
    private final BandRepository bandRepository;

    //Constructor
    public MusicSetServiceImpl(MusicSetRepository musicSetRepository,
                               BandRepository bandRepository) {
        this.musicSetRepository = musicSetRepository;
        this.bandRepository = bandRepository;
    }

    public List<MusicSet> getAllMusicSets() {
        return musicSetRepository.findAll();
    }

    public Optional<MusicSet> getMusicSetById(Long id) {
        return musicSetRepository.findById(id);
    }

    public MusicSet saveMusicSet(MusicSet musicSet) {
        return musicSetRepository.save(musicSet);
    }
    //Updates an existing MusicSet by its ID
    public MusicSet updateMusicSet(Long id, MusicSet updatedMusicSet) {
        Optional<MusicSet> musicSetOptional = musicSetRepository.findById(id);
        if (musicSetOptional.isPresent()) {
            MusicSet musicSet = musicSetOptional.get();
            musicSet.setTitle(updatedMusicSet.getTitle());
            musicSet.setComposer(updatedMusicSet.getComposer());
            musicSet.setArranger(updatedMusicSet.getArranger());
            musicSet.setSuitableForTraining(updatedMusicSet.getSuitableForTraining());
            return musicSetRepository.save(musicSet);
        } else {
            throw new RuntimeException("Music Set not found");
        }
    }

    //Retrieves all MusicSets associated with a band name
    public List<MusicSet> getMusicSetsByBand(String bandName) {
        return musicSetRepository.findByBandName(bandName);
    }

    //Adds a band to an existing MusicSet by its ID and the band's ID
    public MusicSet addBandToMusicSet(Long MusicSetId, Long BandId) {
        Optional<MusicSet> musicSetOpt = musicSetRepository.findById(MusicSetId);
        Optional<Band> bandOpt = bandRepository.findById(BandId);

        if (musicSetOpt.isPresent() && bandOpt.isPresent()) {
            MusicSet musicSet = musicSetOpt.get();
            Band band = bandOpt.get();
            if(!musicSet.getSuitableForTraining() && band.getName().equals("Training")){
                throw new RuntimeException("Music Set not suitable for the training band");
            }
            else{
                musicSet.getBands().add(band);
                return musicSetRepository.save(musicSet);
            }
        }
        else{
            throw new RuntimeException("Band not found");
        }
    }

    //Removes all associated bands from a MusicSet
    public void deletePractice(Long musicSetId) {
        try {
            Optional<MusicSet> musicSetOpt = musicSetRepository.findById(musicSetId);
            if(musicSetOpt.isPresent()){
                MusicSet musicSet = musicSetOpt.get();
                if (musicSet.getBands() != null) {
                    Set<Band> musicSetBands = musicSet.getBands();
                    musicSetBands.clear();
                    musicSet.setBands(musicSetBands);
                    musicSetRepository.save(musicSet);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error putting music set into storage: " + e.getMessage());
        }
    }


}
