package com.tushar.split.Controllers;

import com.tushar.split.DTO.GroupBalanceResponse;
import com.tushar.split.Service.BalanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/balances")
public class BalanceController {

    @Autowired
    private  BalanceService balanceService;

    /**
     * Get net balances for a group
     * GET /api/balances/group/{groupId}
     */
    @GetMapping("/group/{groupId}")
    public ResponseEntity<Map<Integer, BigDecimal>> getGroupBalances(@PathVariable int groupId) {
        log.info("Fetching net balances for group: {}", groupId);
        Map<Integer, BigDecimal> balances = balanceService.calculateNetBalances(groupId);
        return ResponseEntity.ok(balances);
    }

    /**
     * Get group balance with simplified settlements
     * GET /api/balances/group/{groupId}/settlements
     */
    @GetMapping("/group/{groupId}/settlements")
    public ResponseEntity<GroupBalanceResponse> getGroupSettlements(@PathVariable int groupId) {
        log.info("Fetching group settlements for group: {}", groupId);
        GroupBalanceResponse response = balanceService.getGroupBalanceWithSettlements(groupId);
        return ResponseEntity.ok(response);
    }
}
