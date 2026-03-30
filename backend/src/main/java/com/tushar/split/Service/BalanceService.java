package com.tushar.split.Service;

import com.tushar.split.DTO.BalanceEntry;
import com.tushar.split.DTO.GroupBalanceResponse;
import com.tushar.split.DTO.SettlementResponse;
import com.tushar.split.Model.Expense;
import com.tushar.split.Model.ExpenseSplit;
import com.tushar.split.Model.SplitGroups;
import com.tushar.split.Model.Users;
import com.tushar.split.Repo.ExpenseRepo;
import com.tushar.split.Repo.ExpenseSplitRepo;
import com.tushar.split.Repo.GroupRepo;
import com.tushar.split.Repo.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BalanceService {

    @Autowired
    private ExpenseRepo expenseRepo;
    @Autowired
    private ExpenseSplitRepo expenseSplitRepo;
    @Autowired
    private  GroupRepo groupRepo;
    @Autowired
    private  UserRepo userRepo;

    public Map<Integer, BigDecimal> calculateNetBalances(int groupId) {
        Map<Integer, BigDecimal> balances = new HashMap<>();

        List<Expense> expenses = expenseRepo.findByGroupId(groupId);

        for (Expense expense : expenses) {
            // Payer gets credit (+)
            int payerId = expense.getPaidBy().getId();
            balances.put(payerId,
                    balances.getOrDefault(payerId, BigDecimal.ZERO)
                            .add(expense.getAmount()));

            // Get all splits for this expense
            List<ExpenseSplit> splits = expenseSplitRepo.findByExpenseId(expense.getId());

            // Each participant gets debit (-)
            for (ExpenseSplit split : splits) {
                int userId = split.getUser().getId();
                balances.put(userId,
                        balances.getOrDefault(userId, BigDecimal.ZERO)
                                .subtract(split.getAmount()));
            }
        }

        // Clean up zero balances
        balances.entrySet().removeIf(entry -> entry.getValue().compareTo(BigDecimal.ZERO) == 0);

        log.debug("Calculated balances for group {}: {}", groupId, balances);
        return balances;
    }

    /**
     * Debt Simplification Algorithm
     */
    public List<SettlementResponse> simplifyDebts(Map<Integer, BigDecimal> balances) {
        List<SettlementResponse> settlements = new ArrayList<>();

        // Step 1: Separate into creditors and debtors
        List<BalanceEntry> creditors = new ArrayList<>();
        List<BalanceEntry> debtors = new ArrayList<>();

        for (Map.Entry<Integer, BigDecimal> entry : balances.entrySet()) {
            int userId = entry.getKey();
            BigDecimal amount = entry.getValue();

            if (amount.compareTo(BigDecimal.ZERO) > 0) {
                creditors.add(new BalanceEntry(userId, amount));
            } else if (amount.compareTo(BigDecimal.ZERO) < 0) {
                debtors.add(new BalanceEntry(userId, amount.abs()));
            }
        }

        if (creditors.isEmpty() || debtors.isEmpty()) {
            log.info("Group is already settled up");
            return settlements;
        }

        // Step 2: Sort by amount (largest first)
        creditors.sort((a, b) -> b.getAmount().compareTo(a.getAmount()));
        debtors.sort((a, b) -> b.getAmount().compareTo(a.getAmount()));

        // Step 3: Match and settle
        int i = 0, j = 0;

        while (i < creditors.size() && j < debtors.size()) {
            BalanceEntry creditor = creditors.get(i);
            BalanceEntry debtor = debtors.get(j);

            BigDecimal settleAmount = creditor.getAmount().min(debtor.getAmount());
            settleAmount = settleAmount.setScale(2, RoundingMode.HALF_UP);

            if (settleAmount.compareTo(BigDecimal.ZERO) > 0) {
                String fromUsername = getUserName(debtor.getUserId());
                String toUsername = getUserName(creditor.getUserId());

                SettlementResponse settlement = SettlementResponse.builder()
                        .fromUserId(debtor.getUserId())
                        .fromUsername(fromUsername)
                        .toUserId(creditor.getUserId())
                        .toUsername(toUsername)
                        .amount(settleAmount)
                        .build();

                settlements.add(settlement);

                log.debug("Settlement: {} pays {} ₹{}", fromUsername, toUsername, settleAmount);
            }

            creditor.setAmount(creditor.getAmount().subtract(settleAmount));
            debtor.setAmount(debtor.getAmount().subtract(settleAmount));

            if (creditor.getAmount().compareTo(BigDecimal.ZERO) == 0) i++;
            if (debtor.getAmount().compareTo(BigDecimal.ZERO) == 0) j++;
        }

        log.info("Debt simplification complete. {} transactions needed.", settlements.size());
        return settlements;
    }

    /**
     * Get complete group balance with settlements
     */

    public GroupBalanceResponse getGroupBalanceWithSettlements(int groupId) {
        SplitGroups group = groupRepo.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with ID: " + groupId));

        Map<Integer, BigDecimal> netBalances = calculateNetBalances(groupId);
        List<SettlementResponse> settlements = simplifyDebts(netBalances);

        BigDecimal totalOwed = netBalances.values().stream()
                .filter(amount -> amount.compareTo(BigDecimal.ZERO) > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalOwing = netBalances.values().stream()
                .filter(amount -> amount.compareTo(BigDecimal.ZERO) < 0)
                .map(BigDecimal::abs)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        boolean isAllSettled = netBalances.isEmpty();

        return GroupBalanceResponse.builder()
                .groupId(groupId)
                .groupName(group.getName())
                .netBalances(netBalances)
                .suggestedSettlements(settlements)
                .isAllSettled(isAllSettled)
                .totalOwed(totalOwed)
                .totalOwing(totalOwing)
                .build();
    }
    public Users getUserByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    private String getUserName(int userId) {
        return userRepo.findById(userId)
                .map(Users::getUsername)
                .orElse("Unknown User");
    }
}
