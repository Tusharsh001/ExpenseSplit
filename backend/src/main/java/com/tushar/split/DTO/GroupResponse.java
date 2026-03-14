package com.tushar.split.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class GroupResponse {

    private Long id;
    private String name;
    private String description;
    private UserSummary createdBy;
    private List<UserSummary> members;
    private Integer memberCount;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdate;
    private Boolean isAdmin;
}
