package uk.ac.sheffield.bandproject.Service;

import uk.ac.sheffield.bandproject.Model.Band;
import uk.ac.sheffield.bandproject.Model.Performance;

import java.util.List;
import java.util.Optional;

public interface PerformanceService {
    List<Performance> getAllPerformances();
    Optional<Performance> getPerformanceById(Long id);
    List<Performance> getPerformanceByBand(Long bandId);
    Performance savePerformance(Performance performance);
    Performance updatePerformance(Long id, Performance updatedPerformance);
    void deletePerformance(Long id);
    void addBandToPerformance(Long performanceId, Long bandId);
    void removeBandFromPerformance(Long performanceId, Long bandId);
}
