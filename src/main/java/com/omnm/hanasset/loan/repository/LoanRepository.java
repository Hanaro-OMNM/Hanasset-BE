package com.omnm.hanasset.loan.repository;

import com.omnm.hanasset.loan.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}
