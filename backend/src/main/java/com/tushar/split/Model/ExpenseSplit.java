package com.tushar.split.Model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ExpenseSplit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Expense expense;

    @ManyToOne
    private Users user;

    private double amount;

}
