package uk.ac.sheffield.bandproject.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "miscellaneous_loans") // Maps this entity to the "miscellaneous_loans" table in the database
public class MiscellaneousLoan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically generates unique ID values
    private Long id;

    @ManyToOne // Many-to-One relationship: multiple loans can be linked to a single user
    @JoinColumn(name = "user_id") // Foreign key column linking to the user in the database
    private User user; // The user associated with the loan

    @ManyToOne // Many-to-One relationship: multiple loans can be linked to a single miscellaneous item
    @JoinColumn(name = "miscellaneous_id") // Foreign key column linking to the miscellaneous item in the database
    private Miscellaneous miscellaneous; // The miscellaneous item being loaned

    @NotNull // Ensures the loan date cannot be null
    private LocalDate date; // Represents the date the miscellaneous item was loaned

    @NotNull // Ensures the quantity cannot be null
    private Integer quantity; // Represents the quantity of items loaned

    private Boolean returned; // Tracks whether the miscellaneous item(s) have been returned

    // Constructors

    public MiscellaneousLoan() {} // Default constructor

    public MiscellaneousLoan(User user, Miscellaneous miscellaneous, LocalDate date, Integer quantity, Boolean returned) {
        this.user = user; // Assigns the user associated with the loan
        this.miscellaneous = miscellaneous; // Assigns the miscellaneous item being loaned
        this.date = date; // Sets the loan date
        this.quantity = quantity; // Sets the quantity of items loaned
        this.returned = returned; // Indicates whether the item(s) have been returned
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
    public Miscellaneous getMiscellaneous() {
        return miscellaneous; // Returns the miscellaneous item being loaned
    }
    public void setMiscellaneous(Miscellaneous miscellaneous) {
        this.miscellaneous = miscellaneous; // Sets the miscellaneous item being loaned
    }
    public LocalDate getLoanDate() {
        return date; // Returns the loan date
    }
    public void setLoanDate(LocalDate date) {
        this.date = date; // Sets the loan date
    }
    public Integer getQuantity() {
        return quantity; // Returns the quantity of items loaned
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity; // Sets the quantity of items loaned
    }
    public Boolean getReturned() {
        return returned; // Returns whether the item(s) have been returned
    }
    public void setReturned(Boolean returned) {
        this.returned = returned; // Sets whether the item(s) have been returned
    }
}
