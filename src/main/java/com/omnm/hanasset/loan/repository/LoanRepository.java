package com.omnm.hanasset.loan.repository;

import com.omnm.hanasset.loan.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    @Query(value = """
            SELECT * FROM loan
            WHERE TIMESTAMPDIFF(YEAR, :birthdate, NOW()) BETWEEN 19 AND loan.max_age
            AND loan.income >= :income
            AND NOT (NOT loan.has_house AND :hasHouse)
            AND ((loan.job_type = '') OR (loan.job_type = :jobType))
            AND ((loan.additional_condition = '')
            OR ((loan.additional_condition = 'abnormalHouse' AND :isAbnormalHouse)
            OR (loan.additional_condition = 'housingFraud' AND :isHousingFraud)))
            AND loan.exclusive_area >= :exclusiveArea
            AND loan.rent_type = :rentType
            AND :deposit BETWEEN loan.min_deposit + 1 AND loan.max_deposit
            AND ((loan.rent_type = '전세') OR (loan.rent_type = '월세' AND loan.max_price >= :price))
            ORDER BY loan.rate, loan.limit_amount DESC
            """, nativeQuery = true)
    List<Loan> findAvailableLoans(@Param("birthdate") LocalDate birthdate,
                                  @Param("income") Integer income,
                                  @Param("hasHouse") Boolean hasHouse,
                                  @Param("jobType") String jobType,
                                  @Param("isAbnormalHouse") Boolean isAbnormalHouse,
                                  @Param("isHousingFraud") Boolean isHousingFraud,
                                  @Param("exclusiveArea") Integer exclusiveArea,
                                  @Param("rentType") String rentType,
                                  @Param("deposit") Long deposit,
                                  @Param("price") Long price);
}
