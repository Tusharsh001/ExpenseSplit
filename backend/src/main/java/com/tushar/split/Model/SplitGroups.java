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
public class SplitGroups {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    private String description;
    @ManyToOne
    private Users createdBy;

    private LocalDateTime createdAt;
    private LocalDateTime LastUpdate;

    @ManyToMany
    @JsonIgnore
    private List<Users> members;

}
