package uk.ac.sheffield.bandproject.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

// Declares this class as a JPA entity mapped to the "performances" table in the database
@Entity
@Table(name = "performances")
public class Performance {

    // Marks 'id' as the primary key for this entity, with auto-generated values
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ensures 'location' field cannot be null or blank, with a validation message if violated
    @NotBlank(message = "Location is required")
    private String location;

    // Ensures 'date' field cannot be null, with a validation message if violated
    @NotNull(message = "Date is required")
    private LocalDate date;

    // Ensures 'time' field cannot be null, with a validation message if violated
    @NotNull(message = "Time is required")
    private LocalTime time;

    // Defines a many-to-many relationship with Band, with cascading merge operations
    @ManyToMany(mappedBy = "performances", cascade = CascadeType.MERGE)
    private Set<Band> bands = new HashSet<>();

    // Defines a many-to-many relationship with MusicSet using a join table "performance_musics"
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "performance_musics", // Specifies the name of the join table
            joinColumns = @JoinColumn(name = "performance_id"), // Join column for Performance
            inverseJoinColumns = @JoinColumn(name = "music_set_id") // Join column for MusicSet
    )
    private Set<MusicSet> musicSets = new HashSet<>();

    // Default constructor required by JPA
    public Performance() {}

    // Constructor with parameters to initialize all fields
    public Performance(Long id, String location, LocalDate date, LocalTime time, Set<Band> bands, Set<MusicSet> musicSets) {
        this.id = id; // Sets the unique ID for the performance
        this.location = location; // Sets the location of the performance
        this.date = date; // Sets the date of the performance
        this.time = time; // Sets the time of the performance
        this.bands = bands; // Links the bands participating in the performance
        this.musicSets = musicSets; // Links the music sets used in the performance
    }

    // Getters and setters for accessing and modifying the entity's fields

    public Long getId() {
        return id; // Returns the ID of the performance
    }
    public void setId(Long id) {
        this.id = id; // Sets the ID of the performance
    }

    public String getLocation() {
        return location; // Returns the location of the performance
    }
    public void setLocation(String location) {
        this.location = location; // Updates the location of the performance
    }

    public LocalDate getDate() {
        return date; // Returns the date of the performance
    }
    public void setDate(LocalDate date) {
        this.date = date; // Updates the date of the performance
    }

    public LocalTime getTime() {
        return time; // Returns the time of the performance
    }
    public void setTime(LocalTime time) {
        this.time = time; // Updates the time of the performance
    }

    public Set<Band> getBands() {
        return bands; // Returns the set of bands participating in the performance
    }
    public void setBands(Set<Band> bands) {
        this.bands = bands; // Updates the set of bands for the performance
    }

    public Set<MusicSet> getMusicSets() {
        return musicSets; // Returns the set of music sets used in the performance
    }
    public void setMusicSets(Set<MusicSet> musicSets) {
        this.musicSets = musicSets; // Updates the set of music sets for the performance
    }
}
