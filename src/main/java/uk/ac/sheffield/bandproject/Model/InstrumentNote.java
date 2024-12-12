package uk.ac.sheffield.bandproject.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "instrument_notes") // Maps this entity to the "instrument_notes" table in the database
public class InstrumentNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically generates unique ID values
    private Long id;

    @NotBlank // Ensures the description is not null or empty
    private String description; // A brief description of the note

    @NotNull // Ensures the date cannot be null
    private LocalDate date; // Represents the date the note was created

    @ManyToOne // Many-to-One relationship: multiple notes can be associated with a single instrument
    @JoinColumn(name = "instrument_id") // Foreign key column in the database linking to the instrument
    private Instrument instrument; // The instrument this note belongs to

    // Constructors

    public InstrumentNote() {} // Default constructor

    public InstrumentNote(String description, LocalDate date, Instrument instrument) {
        this.description = description; // Assigns the description of the note
        this.date = date; // Sets the date the note was created
        this.instrument = instrument; // Links the note to the associated instrument
    }

    // Getters and Setters
    // Provide controlled access and modification for private fields

    public Long getId() {
        return id; // Returns the unique ID of the note
    }
    public void setId(Long id) {
        this.id = id; // Sets the unique ID of the note
    }
    public String getDescription() {
        return description; // Returns the description of the note
    }
    public void setDescription(String description) {
        this.description = description; // Sets the description of the note
    }
    public LocalDate getDate() {
        return date; // Returns the date the note was created
    }
    public void setDate(LocalDate date) {
        this.date = date; // Sets the date the note was created
    }
    public Instrument getInstrument() {
        return instrument; // Returns the instrument associated with the note
    }
    public void setInstrument(Instrument instrument) {
        this.instrument = instrument; // Sets the instrument associated with the note
    }
}
