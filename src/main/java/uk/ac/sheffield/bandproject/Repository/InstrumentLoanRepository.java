package uk.ac.sheffield.bandproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.sheffield.bandproject.Model.InstrumentLoan;

public interface InstrumentLoanRepository extends JpaRepository<InstrumentLoan, Long> {
}
