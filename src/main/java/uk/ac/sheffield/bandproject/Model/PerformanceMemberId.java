package uk.ac.sheffield.bandproject.Model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

// Marks this class as an embeddable ID that can be used as a composite key in JPA entities
@Embeddable
public class PerformanceMemberId implements Serializable {

    // Fields representing the composite key components
    private Long userId; // ID of the user in the relationship
    private Long bandId; // ID of the band in the relationship
    private Long performanceId; // ID of the performance in the relationship

    // Default constructor required for JPA
    public PerformanceMemberId() {}

    // Constructor to initialize all parts of the composite key
    public PerformanceMemberId(Long userId, Long bandId, Long performanceId) {
        this.userId = userId; // Sets the user ID
        this.bandId = bandId; // Sets the band ID
        this.performanceId = performanceId; // Sets the performance ID
    }

    // Getters and setters for accessing and modifying the composite key fields

    public Long getUserId() {
        return userId; // Returns the user ID
    }
    public void setUserId(Long userId) {
        this.userId = userId; // Updates the user ID
    }

    public Long getBandId() {
        return bandId; // Returns the band ID
    }
    public void setBandId(Long bandId) {
        this.bandId = bandId; // Updates the band ID
    }

    public Long getPerformanceId() {
        return performanceId; // Returns the performance ID
    }
    public void setPerformanceId(Long performanceId) {
        this.performanceId = performanceId; // Updates the performance ID
    }

    // Override equals() to compare instances of this composite key based on their field values
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false; // Return false if the object is null or types differ
        PerformanceMemberId that = (PerformanceMemberId) o;
        // Compare each field for equality
        return Objects.equals(userId, that.userId)
                && Objects.equals(bandId, that.bandId)
                && Objects.equals(performanceId, that.performanceId);
    }

    // Override hashCode() to generate a hash code based on the composite key fields
    @Override
    public int hashCode() {
        return Objects.hash(userId, bandId, performanceId); // Compute hash code using all fields
    }
}
