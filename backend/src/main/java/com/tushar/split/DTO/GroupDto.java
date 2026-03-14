package com.tushar.split.DTO;

import com.tushar.split.Model.Users;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class GroupDto {

    @NotBlank
    private String name;
    @NotBlank
    private String description;

    private List<Long> memberIds;
}
