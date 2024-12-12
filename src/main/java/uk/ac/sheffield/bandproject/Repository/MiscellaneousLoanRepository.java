package uk.ac.sheffield.bandproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.sheffield.bandproject.Model.MiscellaneousLoan;

public interface MiscellaneousLoanRepository extends JpaRepository<MiscellaneousLoan, Long> {
}
