package uk.ac.sheffield.bandproject.Service;

import org.springframework.stereotype.Service;
import uk.ac.sheffield.bandproject.Model.PerformanceMember;
import uk.ac.sheffield.bandproject.Repository.PerformanceMemberRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PerformanceMemberServiceImpl implements PerformanceMemberService {
    private final PerformanceMemberRepository performanceMemberRepository;

    //Constructor
    public PerformanceMemberServiceImpl(PerformanceMemberRepository performanceMemberRepository) {
        this.performanceMemberRepository = performanceMemberRepository;
    }

    @Override
    //Retrieves a PerformanceMember
    public Optional<PerformanceMember> findByUserIdAndBandIdAndPerformanceId(Long userId, Long bandId, Long performanceId) {
        // Query the repository for a PerformanceMember with the given parameters.
        return performanceMemberRepository.findByUserIdAndBandIdAndPerformanceId(userId, bandId, performanceId);
    }

    @Override
    //Retrieves a list of PerformanceMembers for a specific performance
    public List<PerformanceMember> findByPerformanceIdAndAvailability(Long performanceId, Boolean availability) {
        return performanceMemberRepository.findByPerformanceIdAndAvailability(performanceId, availability);
    }

    @Override
    //Saves a new PerformanceMember to the repository
    public PerformanceMember savePerformanceMember(PerformanceMember performanceMember) {
        return performanceMemberRepository.save(performanceMember);
    }

    @Override
    //Updates an existing PerformanceMember
    public PerformanceMember updatePerformanceMember(Long userId, Long bandId, Long performanceId,
                                                     PerformanceMember updatedPerformanceMember) {
        Optional<PerformanceMember> performanceMemberOptional = performanceMemberRepository
                .findByUserIdAndBandIdAndPerformanceId(userId, bandId, performanceId);
        if (performanceMemberOptional.isPresent()) {
            PerformanceMember performanceMember = performanceMemberOptional.get();
            performanceMember.setAvailability(updatedPerformanceMember.getAvailability());
            return performanceMemberRepository.save(performanceMember);
        } else {
            throw new RuntimeException("Performance member not found");
        }
    }
}
