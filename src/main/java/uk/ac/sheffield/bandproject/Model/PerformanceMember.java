package uk.ac.sheffield.bandproject.Model;

import jakarta.persistence.*;

// Declares this class as a JPA entity mapped to the "performance_members" table in the database
@Entity
@Table(name = "performance_members")
public class PerformanceMember {

    // Composite primary key for this entity, defined as an embeddable ID
    @EmbeddedId
    private PerformanceMemberId performanceMemberId;

    // Many-to-one relationship linking to the User entity, mapped to the 'user_id' column in the composite key
    @ManyToOne
    @MapsId("userId") // Maps the 'userId' part of the composite key
    @JoinColumn(name = "user_id", nullable = false) // Specifies the foreign key column for User
    private User user;

    // Many-to-one relationship linking to the Band entity, mapped to the 'band_id' column in the composite key
    @ManyToOne
    @MapsId("bandId") // Maps the 'bandId' part of the composite key
    @JoinColumn(name = "band_id", nullable = false) // Specifies the foreign key column for Band
    private Band band;

    // Many-to-one relationship linking to the Performance entity, mapped to the 'performance_id' column in the composite key
    @ManyToOne
    @MapsId("performanceId") // Maps the 'performanceId' part of the composite key
    @JoinColumn(name = "performance_id", nullable = false) // Specifies the foreign key column for Performance
    private Performance performance;

    // Indicates the availability of the member for the performance (true/false)
    private Boolean availability;

    // Default constructor required by JPA
    public PerformanceMember () {}

    // Constructor to initialize the fields and set up the composite key
    public PerformanceMember (PerformanceMemberId  performanceMemberId,User user, Band band, Performance performance, Boolean availability) {
        this.performanceMemberId = performanceMemberId;
        this.user = user; // Sets the user for the performance member
        this.band = band; // Sets the band for the performance member
        this.performance = performance; // Sets the performance for the member
        this.availability = availability; // Sets the availability of the member
        // Initializes the composite key using the IDs of the related entities
        this.performanceMemberId = new PerformanceMemberId(user.getId(), band.getId(), performance.getId());
    }

    // Getters and setters for accessing and modifying the entity's fields

    public PerformanceMemberId getPerformanceMemberId() {
        return performanceMemberId; // Returns the composite key
    }
    public void setPerformanceMemberId(PerformanceMemberId performanceMemberId) {
        this.performanceMemberId = performanceMemberId; // Updates the composite key
    }

    public User getUser() {
        return user; // Returns the user associated with this performance member
    }
    public void setUser(User user) {
        this.user = user; // Updates the user associated with this performance member
    }

    public Band getBand() {
        return band; // Returns the band associated with this performance member
    }
    public void setBand(Band band) {
        this.band = band; // Updates the band associated with this performance member
    }

    public Performance getPerformance() {
        return performance; // Returns the performance associated with this performance member
    }
    public void setPerformance(Performance performance) {
        this.performance = performance; // Updates the performance associated with this performance member
    }

    public Boolean getAvailability() {
        return availability; // Returns the availability status of the member
    }
    public void setAvailability(Boolean availability) {
        this.availability = availability; // Updates the availability status of the member
    }
}
