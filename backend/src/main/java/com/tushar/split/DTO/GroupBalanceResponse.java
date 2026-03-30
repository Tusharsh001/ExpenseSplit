package com.tushar.split.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupBalanceResponse {
    private int groupId;
    private String groupName;
    private Map<Integer, BigDecimal> netBalances;  // Raw balances per user
    private List<SettlementResponse> suggestedSettlements;  // Simplified transactions
    private Boolean isAllSettled;  // True if all balances are zero
    private BigDecimal totalOwed;   // Sum of all positive balances
    private BigDecimal totalOwing;
}
