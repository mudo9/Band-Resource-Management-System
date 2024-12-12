package uk.ac.sheffield.bandproject.Service;

import org.springframework.stereotype.Service;
import uk.ac.sheffield.bandproject.Model.*;
import uk.ac.sheffield.bandproject.Repository.BandRepository;
import uk.ac.sheffield.bandproject.Repository.MusicSetRepository;
import uk.ac.sheffield.bandproject.Repository.PerformanceMemberRepository;
import uk.ac.sheffield.bandproject.Repository.PerformanceRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PerformanceServiceImpl implements PerformanceService {
    private final PerformanceRepository performanceRepository;
    private final BandRepository bandRepository;
    private final PerformanceMemberRepository performanceMemberRepository;
    private final MusicSetRepository musicSetRepository;

    //Constructor
    public PerformanceServiceImpl(PerformanceRepository performanceRepository, BandRepository bandRepository,
                                  PerformanceMemberRepository performanceMemberRepository,
                                  MusicSetRepository musicSetRepository) {
        this.performanceRepository = performanceRepository;
        this.bandRepository = bandRepository;
        this.performanceMemberRepository = performanceMemberRepository;
        this.musicSetRepository = musicSetRepository;
    }
    @Override
    //Retrieves all performances from the performance repository
    public List<Performance> getAllPerformances() {
        return performanceRepository.findAll();
    }

    @Override
    //Retrieves a specific performance
    public Optional<Performance> getPerformanceById(Long id) {
        return performanceRepository.findById(id);
    }

    @Override
    //Retrieves all performances associated with a particular band
    public List<Performance> getPerformanceByBand(Long bandId) {
        return performanceRepository.findByBand(bandId);
    }

    @Override
    //Saves a new performance to the performance repository
    public Performance savePerformance(Performance performance) {
        return performanceRepository.save(performance);
    }

    @Override
    //Updates an existing performance's details
    public Performance updatePerformance(Long id, Performance updatedPerformance) {
        Optional<Performance> performanceOptional = performanceRepository.findById(id);
        if (performanceOptional.isPresent()) {
            Performance performance = performanceOptional.get();
            performance.setLocation(updatedPerformance.getLocation());
            performance.setDate(updatedPerformance.getDate());
            performance.setTime(updatedPerformance.getTime());
            performance.setMusicSets(updatedPerformance.getMusicSets());
            return performanceRepository.save(performance);
        } else {
            throw new RuntimeException("Performance not found");
        }
    }

    @Override
    //Deletes a performance
    public void deletePerformance(Long id) {
        Optional<Performance> performanceOptional = performanceRepository.findById(id);
        if (performanceOptional.isPresent()) {
            Performance performance = performanceOptional.get();

            //Remove performance reference from associated bands
            Set<Band> bands = performance.getBands();
            for (Band band : bands) {
                band.getPerformances().remove(performance);
            }

            //Remove performance reference from associated music sets
            Set<MusicSet> musicSets = performance.getMusicSets();
            for (MusicSet musicSet : musicSets) {
                musicSet.getPerformances().remove(performance);
            }

            //Save the updated bands and music sets
            bandRepository.saveAll(bands);
            musicSetRepository.saveAll(musicSets);
            //Delete associated performance members
            performanceMemberRepository.deleteByPerformanceId(id);
            performanceRepository.deleteById(id);
        } else {
            throw new RuntimeException("Performance not found");
        }
    }

    @Override
    //Adds a band to a performance by associating them
    public void addBandToPerformance(Long performanceId, Long bandId) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new IllegalArgumentException("Performance not found"));
        Band band = bandRepository.findById(bandId)
                .orElseThrow(() -> new IllegalArgumentException("Band not found"));

        if (!performance.getBands().contains(band)) {
            performance.getBands().add(band);
            band.getPerformances().add(performance);
            performanceRepository.save(performance);
            bandRepository.save(band);
        }
        Set<User> users = band.getUsers();

        for (User user : users) {
            PerformanceMemberId primaryKey = new PerformanceMemberId(user.getId(), bandId, performanceId);

            if (!performanceMemberRepository.existsById(primaryKey)) {
                PerformanceMember performanceMember = new PerformanceMember();
                performanceMember.setPerformanceMemberId(primaryKey);
                performanceMember.setUser(user);
                performanceMember.setBand(band);
                performanceMember.setPerformance(performance);
                performanceMemberRepository.save(performanceMember);
            }
        }
    }

    @Override
    //Removes a band from a performance by disassociating them
    public void removeBandFromPerformance(Long performanceId, Long bandId) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new IllegalArgumentException("Performance not found"));
        Band band = bandRepository.findById(bandId)
                .orElseThrow(() -> new IllegalArgumentException("Band not found"));

        if (performance.getBands().contains(band)) {
            performance.getBands().remove(band);
            band.getPerformances().remove(performance);

            Set<User> users = band.getUsers();
            for (User user : users) {
                PerformanceMemberId primaryKey = new PerformanceMemberId(user.getId(), bandId, performanceId);
                performanceMemberRepository.deleteById(primaryKey);
            }

            performanceRepository.save(performance);
            bandRepository.save(band);
        }
    }
}
