package com.tushar.split.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true,nullable = false)
    private String username;
    @Column(unique = true,nullable = false)
    private String email;
    private String password;
    private Boolean verified=false;
    private LocalDateTime registerAt;

    @ManyToMany(mappedBy = "members")
    @JsonIgnore
    private List<SplitGroups> groups;
    @OneToMany(mappedBy = "paidBy")
    @JsonIgnore
    private List<Expense> expenses;


}
