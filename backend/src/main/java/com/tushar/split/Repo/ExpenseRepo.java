package com.tushar.split.Repo;

import com.tushar.split.Model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExpenseRepo extends JpaRepository<Expense,Integer> {

    List<Expense> findByGroupId(int groupId);

    @Query("SELECT e FROM Expense e WHERE e.group.id = :groupId AND " +
            "(e.paidBy.id = :userId OR EXISTS (SELECT s FROM e.splits s WHERE s.user.id = :userId))")
    List<Expense> findUserExpensesInGroup(@Param("groupId") int groupId, @Param("userId") int userId);
}
