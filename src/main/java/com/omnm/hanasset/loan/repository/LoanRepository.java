package com.omnm.hanasset.loan.repository;

import com.omnm.hanasset.loan.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    @Query(value = """
            SELECT * FROM LOAN
            WHERE TIMESTAMPDIFF(YEAR, :birthdate, NOW()) BETWEEN 19 AND LOAN.max_age
            AND LOAN.income >= :income
            AND NOT (NOT LOAN.has_house AND :hasHouse)
            AND ((LOAN.job_type = '') OR (LOAN.job_type = :jobType))
            AND ((LOAN.additional_condition = '')
            OR ((LOAN.additional_condition = 'abnormalHouse' AND :isAbnormalHouse)
            OR (LOAN.additional_condition = 'housingFraud' AND :isHousingFraud)))
            AND LOAN.exclusive_area >= :exclusiveArea
            AND LOAN.rent_type = :rentType
            AND :deposit BETWEEN LOAN.min_deposit + 1 AND LOAN.max_deposit
            AND ((LOAN.rent_type = '전세') OR (LOAN.rent_type = '월세' AND LOAN.max_price >= :price))
            ORDER BY LOAN.rate, LOAN.limit_amount DESC
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
