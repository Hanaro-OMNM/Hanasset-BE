package com.omnm.hanasset.loan;

import com.omnm.hanasset.loan.dto.LoanInfoDTO;
import com.omnm.hanasset.loan.entity.Loan;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanMapper {
    LoanInfoDTO loanToDTO(Loan loan);
}
