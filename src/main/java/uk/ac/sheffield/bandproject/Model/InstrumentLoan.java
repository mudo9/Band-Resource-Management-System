package uk.ac.sheffield.bandproject.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "instrument_loans") // Maps this entity to the "instrument_loans" table in the database
public class InstrumentLoan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically generates unique ID values
    private Long id;

    @ManyToOne // Many-to-One relationship: multiple loans can be linked to a single user
    @JoinColumn(name = "user_id") // Foreign key column in the database linking to the user
    private User user;

    @ManyToOne // Many-to-One relationship: multiple loans can be linked to a single instrument
    @JoinColumn(name = "instrument_id") // Foreign key column in the database linking to the instrument
    private Instrument instrument;

    @NotNull // Ensures the date of the loan cannot be null
    private LocalDate date; // Represents the date when the instrument was loaned

    private Boolean returned; // Tracks whether the instrument has been returned (true or false)

    // Constructors

    public InstrumentLoan() {} // Default constructor

    public InstrumentLoan(User user, Instrument instrument, LocalDate date, Boolean returned) {
        this.user = user; // Assigns the user who borrowed the instrument
        this.instrument = instrument; // Assigns the borrowed instrument
        this.date = date; // Sets the date the instrument was loaned
        this.returned = returned; // Indicates if the instrument has been returned
    }

    // Getters and Setters
    // Provide controlled access and modification for private fields

    public Long getId() {
        return id; // Returns the unique ID of the loan
    }
    public void setId(Long id) {
        this.id = id; // Sets the unique ID of the loan
    }
    public User getUser() {
        return user; // Returns the user associated with the loan
    }
    public void setUser(User user) {
        this.user = user; // Sets the user associated with the loan
    }
    public Instrument getInstrument() {
        return instrument; // Returns the instrument associated with the loan
    }
    public void setInstrument(Instrument instrument) {
        this.instrument = instrument; // Sets the instrument associated with the loan
    }
    public LocalDate getDate() {
        return date; // Returns the date of the loan
    }
    public void setDate(LocalDate date) {
        this.date = date; // Sets the date of the loan
    }
    public Boolean getReturned() {
        return returned; // Returns whether the instrument has been returned
    }
    public void setReturned(Boolean returned) {
        this.returned = returned; // Sets whether the instrument has been returned
    }
}
