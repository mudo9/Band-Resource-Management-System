package uk.ac.sheffield.bandproject.Service;

import uk.ac.sheffield.bandproject.Model.PerformanceMember;

import java.util.List;
import java.util.Optional;

public interface PerformanceMemberService {
    Optional<PerformanceMember> findByUserIdAndBandIdAndPerformanceId(Long userId, Long bandId, Long performanceId);
    List<PerformanceMember> findByPerformanceIdAndAvailability(Long performanceId, Boolean availability);
    PerformanceMember savePerformanceMember(PerformanceMember performanceMember);
    PerformanceMember updatePerformanceMember(Long userId, Long bandId, Long performanceId, PerformanceMember performanceMember);
}
