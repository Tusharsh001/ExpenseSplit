package com.tushar.split.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettlementResponse {
    private int fromUserId;      // Who pays the money
    private String fromUsername;  // For frontend display
    private int toUserId;        // Who receives the money
    private String toUsername;    // For frontend display
    private BigDecimal amount;    // How much money
}
