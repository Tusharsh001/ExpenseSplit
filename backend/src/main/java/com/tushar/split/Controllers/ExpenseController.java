package com.tushar.split.Controllers;


import com.tushar.split.DTO.expense.ExpenseRequest;
import com.tushar.split.DTO.expense.ExpenseResponse;
import com.tushar.split.Service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@Slf4j
public class ExpenseController {

    @Autowired
    private final ExpenseService expenseService;

    /**
     * Create a new expense
     * POST /api/expenses
     * Request: {
     *     "description": "Dinner",
     *     "amount": 120.00,
     *     "groupId": 1,
     *     "paidById": 1,
     *     "splitType": "EQUAL",
     *     "splitDetails": {}
     * }
     */
    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(@Valid @RequestBody ExpenseRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        log.info("Creating expense: {} in group: {}", request.getDescription(), request.getGroupId());
        ExpenseResponse response = expenseService.createExpense(request, username);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get expense by ID
     * GET /api/expenses/{expenseId}
     */
    @GetMapping("/{expenseId}")
    public ResponseEntity<ExpenseResponse> getExpenseById(@PathVariable int expenseId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        log.info("Fetching expense: {}", expenseId);
        ExpenseResponse response = expenseService.getExpenseById(expenseId, username);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all expenses for a group (Group Detail Page)
     * GET /api/expenses/group/{groupId}
     */
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<ExpenseResponse>> getGroupExpenses(@PathVariable int groupId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        log.info("Fetching expenses for group: {}", groupId);
        List<ExpenseResponse> responses = expenseService.getGroupExpenses(groupId, username);
        return ResponseEntity.ok(responses);
    }

    /**
     * Get all expenses for current user (Dashboard Recent Activity)
     * GET /api/expenses/my-expenses
     */
    @GetMapping("/my-expenses")
    public ResponseEntity<List<ExpenseResponse>> getMyExpenses() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        log.info("Fetching expenses for user: {}", username);
        List<ExpenseResponse> responses = expenseService.getMyExpenses(username);
        return ResponseEntity.ok(responses);
    }

    /**
     * Update expense
     * PUT /api/expenses/{expenseId}
     */
//    @PutMapping("/{expenseId}")
//    public ResponseEntity<ExpenseResponse> updateExpense(@PathVariable int expenseId, @Valid @RequestBody ExpenseRequest request) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//
//        log.info("Updating expense: {}", expenseId);
//        ExpenseResponse response = expenseService.updateExpense(expenseId, request, username);
//        return ResponseEntity.ok(response);
//    }

    /**
     * Delete expense
     * DELETE /api/expenses/{expenseId}
     */
    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(@PathVariable int expenseId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        log.info("Deleting expense: {}", expenseId);
        expenseService.deleteExpense(expenseId, username);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get group balances (Dashboard summary cards)
     * GET /api/expenses/group/{groupId}/balances
     * Response: { "userId": balance }
     */
    @GetMapping("/group/{groupId}/balances")
    public ResponseEntity<Map<Long, BigDecimal>> getGroupBalances(@PathVariable int groupId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        log.info("Calculating balances for group: {}", groupId);
        Map<Long, BigDecimal> balances = expenseService.calculateGroupBalances(groupId, username);
        return ResponseEntity.ok(balances);
    }

    /**
     * Get suggested settlements for a group (Debt simplification)
     * GET /api/expenses/group/{groupId}/settlements
     * Response: [{ "from": 2, "to": 1, "amount": 30 }]
     */
    @GetMapping("/group/{groupId}/settlements")
    public ResponseEntity<List<SettlementResponse>> getSuggestedSettlements(@PathVariable int groupId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        log.info("Calculating suggested settlements for group: {}", groupId);
        List<SettlementResponse> settlements = expenseService.getSuggestedSettlements(groupId, username);
        return ResponseEntity.ok(settlements);
    }

    /**
     * Get total spent by current user
     * GET /api/expenses/total-spent
     */
    @GetMapping("/total-spent")
    public ResponseEntity<BigDecimal> getTotalSpentByCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        log.info("Calculating total spent for user: {}", username);
        BigDecimal totalSpent = expenseService.getTotalSpentByUser(username);
        return ResponseEntity.ok(totalSpent);
    }
}