package uk.ac.sheffield.bandproject.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
// Declares this class as a JPA entity mapped to the "music-sets" table in the database
@Entity
@Table(name="music_sets")
public class MusicSet {

    // Marks 'id' as the primary key for this entity with an auto-incrementing value
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ensures 'title' field cannot be null or blank, with a validation message if violated
    @NotBlank(message = "Title is required")
    private String title;

    // Ensures 'composer' field cannot be null or blank, with a validation message if violated
    @NotBlank(message = "Composer is required")
    private String composer;

    // Optional field to specify the arranger of the music set
    private String arranger;

    // Indicates whether the music set is suitable for training purposes
    private boolean suitableForTraining;

    // Defines a one-to-many relationship with MusicPart, with cascading operations and orphan removal
    @OneToMany(mappedBy = "musicSet", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MusicPart> musicParts = new HashSet<>();

    // Many-to-many relationship with Band, with a join table named "practice"
    @ManyToMany
    @JoinTable(
            name = "practice", // Specifies the name of the join table
            joinColumns = @JoinColumn(name = "music_set_id"), // Join column for MusicSet
            inverseJoinColumns = @JoinColumn(name = "band_id") // Join column for Band
    )
    private Set<Band> bands = new HashSet<>();

    // Defines a many-to-many relationship with Performance, mapped by the 'musicSets' field in Performance
    @ManyToMany(mappedBy = "musicSets")
    private Set<Performance> performances = new HashSet<>();

    // One-to-many relationship with MusicSetNote, with cascading operations and orphan removal
    @OneToMany(mappedBy = "musicSet", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MusicSetNote> musicSetNotes = new HashSet<>();

    // Default constructor required by JPA
    public MusicSet(){

    }
    // Constructor with parameters to initialize all fields
    public MusicSet(Long id, String title, String composer, String arranger, boolean suitableForTraining,
                    Set<MusicPart> musicParts, Set<Band> bands, Set<Performance> performances,
                    Set<MusicSetNote> musicSetNotes) {
        this.id = id;
        this.title = title;
        this.composer = composer;
        this.arranger = arranger;
        this.suitableForTraining = suitableForTraining;
        this.musicParts = musicParts;
        this.bands = bands;
        this.performances = performances;
        this.musicSetNotes = musicSetNotes;
    }

    // Getters and setters for accessing and modifying the entity's fields

    public Long getId() {
        return id; // Returns the ID of the music set
    }
    public void setId(Long id) {
        this.id = id; // Sets the ID of the music set
    }

    public String getTitle() {
        return title; // Returns the title of the music set
    }
    public void setTitle(String title) {
        this.title = title; // Updates the title of the music set
    }

    public String getComposer() {
        return composer; // Returns the composer of the music set
    }
    public void setComposer(String composer) {
        this.composer = composer; // Updates the composer of the music set
    }

    public String getArranger() {
        return arranger; // Returns the arranger of the music set
    }
    public void setArranger(String arranger) {
        this.arranger = arranger; // Updates the arranger of the music set
    }

    public boolean getSuitableForTraining() {
        return suitableForTraining; // Indicates if the music set is suitable for training
    }
    public void setSuitableForTraining(Boolean suitableForTraining) {
        this.suitableForTraining = suitableForTraining; // Updates the training suitability of the music set
    }

    public Set<MusicPart> getMusicParts() {
        return musicParts; // Returns the set of music parts
    }

    public Set<Band> getBands() {
        return bands; // Returns the set of bands associated with this music set
    }
    public void setBands(Set<Band> bands) {
        this.bands = bands; // Updates the bands associated with this music set
    }

    public Set<Performance> getPerformances() {
        return performances; // Returns the performances associated with this music set
    }
    public void setPerformances(Set<Performance> performances) {
        this.performances = performances; // Updates the performances associated with this music set
    }

    public Set<MusicSetNote> getMusicSetNotes() {
        return musicSetNotes; // Returns the set of notes for this music set
    }
    public void setMusicSetNotes(Set<MusicSetNote> musicSetNotes) {
        this.musicSetNotes = musicSetNotes; // Updates the notes for this music set
    }
}

