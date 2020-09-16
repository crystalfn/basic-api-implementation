package com.thoughtworks.rslist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotEmpty
    @Size(max = 8)
    private String userName;

    @NotNull
    @Min(18)
    @Max(100)
    private Integer age;

    @NotEmpty
    private String gender;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Pattern(regexp = "^1")
    private String phone;
}
