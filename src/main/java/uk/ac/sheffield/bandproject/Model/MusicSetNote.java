package uk.ac.sheffield.bandproject.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

// Declares this class as a JPA entity mapped to the "music_set_notes" table in the database
@Entity
@Table(name = "music_set_notes")
public class MusicSetNote {

    // Marks 'id' as the primary key for this entity, with auto-generated values
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ensures 'description' field cannot be null or blank
    @NotBlank
    private String description;

    // Ensures 'date' field cannot be null and stores the date for the note
    @NotNull
    private LocalDate date;

    // Many-to-one relationship with MusicSet, indicating each note is linked to a single music set
    @ManyToOne
    @JoinColumn(name = "music_set_id") // Specifies the foreign key column name in the database
    private MusicSet musicSet;

    // Default constructor required by JPA
    public MusicSetNote() {}

    // Constructor with parameters to initialize all fields
    public MusicSetNote(String description, LocalDate date, MusicSet musicSet) {
        this.description = description; // Sets the description of the note
        this.date = date; // Sets the date for the note
        this.musicSet = musicSet; // Links the note to a specific music set
    }

    // Getters and setters for accessing and modifying the entity's fields

    public Long getId() {
        return id; // Returns the ID of the note
    }
    public void setId(Long id) {
        this.id = id; // Sets the ID of the note
    }

    public String getDescription() {
        return description; // Returns the description of the note
    }
    public void setDescription(String description) {
        this.description = description; // Updates the description of the note
    }

    public LocalDate getDate() {
        return date; // Returns the date of the note
    }
    public void setDate(LocalDate date) {
        this.date = date; // Updates the date of the note
    }

    public MusicSet getMusicSet() {
        return musicSet; // Returns the music set associated with the note
    }
    public void setMusicSet(MusicSet musicSet) {
        this.musicSet = musicSet; // Updates the music set associated with the note
    }
}

