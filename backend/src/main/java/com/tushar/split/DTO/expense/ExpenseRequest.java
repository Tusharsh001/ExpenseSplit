package com.tushar.split.DTO.expense;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
public class ExpenseRequest {


    private String description;
    @NotBlank(message = "amount is Required")
    private BigDecimal amount;
    private int groupId;
    private int paidById;
    private SplitType splitType;
    private Map<Integer, BigDecimal> splitDetails;
}
