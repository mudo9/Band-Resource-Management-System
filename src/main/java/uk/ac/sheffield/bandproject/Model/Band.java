package uk.ac.sheffield.bandproject.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bands") // Specifies the table name in the database
public class Band {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically generates the ID value
    private Long id;

    @NotBlank // Ensures the name cannot be null or empty
    private String name;

    @ManyToMany(cascade = CascadeType.MERGE) // Many-to-Many relationship with performances
    @JoinTable(
            name = "performance_bands", // Specifies the join table name
            joinColumns = @JoinColumn(name = "band_id"), // Join column from Band
            inverseJoinColumns = @JoinColumn(name = "performance_id") // Inverse join column from Performance
    )
    private Set<Performance> performances = new HashSet<>(); // Holds the performances associated with the band

    @ManyToMany(mappedBy = "bands") // Many-to-Many relationship with users, mapped by the "bands" field in User
    private Set<User> users = new HashSet<>(); // Holds the users associated with the band

    @ManyToMany(mappedBy = "bands") // Many-to-Many relationship with music sets, mapped by the "bands" field in MusicSet
    private Set<MusicSet> musicSets = new HashSet<>(); // Holds the music sets associated with the band

    // Constructors

    public Band() {} // Default constructor

    public Band(Long id, String name, Set<Performance> performances, Set<User> users, Set<MusicSet> musicSets) {
        this.id = id; // ID of the band
        this.name = name; // Name of the band
        this.performances = performances; // Performances linked to the band
        this.users = users; // Users linked to the band
        this.musicSets = musicSets; // Music sets linked to the band
    }

    // Getters and Setters
    // Provide access to private fields and allow modification where needed

    public Long getId() {
        return id; // Returns the ID of the band
    }
    public void setId(Long id) {
        this.id = id; // Sets the ID of the band
    }
    public String getName() {
        return name; // Returns the name of the band
    }
    public void setName(String name) {
        this.name = name; // Sets the name of the band
    }
    public Set<Performance> getPerformances() {
        return performances; // Returns the performances linked to the band
    }
    public void setPerformances(Set<Performance> performances) {
        this.performances = performances; // Sets the performances linked to the band
    }
    public Set<User> getUsers() {
        return users; // Returns the users linked to the band
    }
    public void setUsers(Set<User> users) {
        this.users = users; // Sets the users linked to the band
    }
    public Set<MusicSet> getMusicSets() {
        return musicSets; // Returns the music sets linked to the band
    }
    public void setMusicSets(Set<MusicSet> musicSets) {
        this.musicSets = musicSets; // Sets the music sets linked to the band
    }
}
