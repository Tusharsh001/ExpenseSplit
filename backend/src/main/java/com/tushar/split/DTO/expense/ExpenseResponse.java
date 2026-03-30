package com.tushar.split.DTO.expense;


import com.tushar.split.DTO.UserSummary;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ExpenseResponse {
    private Integer id;
    private String description;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private int groupId;
    private UserSummary paidBy;
    private List<SplitSummary> splits;
    private SplitType splitType;
    private BigDecimal totalOwed;
    private BigDecimal yourShare;
}
