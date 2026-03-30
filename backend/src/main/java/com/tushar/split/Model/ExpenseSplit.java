package com.tushar.split.Model;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class ExpenseSplit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Expense expense;

    @ManyToOne
    private Users user;

    private BigDecimal amount;
    private Double percentage;

}
