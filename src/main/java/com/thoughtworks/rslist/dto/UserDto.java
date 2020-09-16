package com.thoughtworks.rslist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotEmpty
    @Size(max = 8)
    private String userName;

    private Integer age;

    @NotEmpty
    private String gender;

    private String email;
    private String phone;
}
