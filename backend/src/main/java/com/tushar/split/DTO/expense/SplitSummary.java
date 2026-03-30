package com.tushar.split.DTO.expense;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class SplitSummary {
    private int userId;
    private String username;
    private BigDecimal amount;
    private Double percentage;
}
