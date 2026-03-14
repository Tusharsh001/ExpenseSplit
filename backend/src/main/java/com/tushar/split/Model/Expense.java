package com.tushar.split.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100)
    private String description;
    @Column(precision = 10, scale = 2)
    private BigDecimal amount;
    private LocalDateTime createdAt;
    @ManyToOne
    private SplitGroups groups;
    @ManyToOne
    private Users paidBy;


    @OneToMany(mappedBy = "expense")
    @JsonIgnore
    private List<ExpenseSplit> splits;

}
