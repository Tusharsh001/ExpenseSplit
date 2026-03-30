package com.tushar.split.Repo;

import com.tushar.split.Model.Expense;
import com.tushar.split.Model.ExpenseSplit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseSplitRepo extends JpaRepository<ExpenseSplit,Integer> {

    List<ExpenseSplit> findByExpenseId(int id);

}
