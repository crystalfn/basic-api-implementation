package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotEmpty
    @Size(max = 8)
    @JsonProperty(value = "user_name")
    private String userName;

    @NotNull
    @Min(18)
    @Max(100)
    @JsonProperty(value = "user_age")
    private Integer age;

    @NotEmpty
    @JsonProperty(value = "user_gender")
    private String gender;

    @NotEmpty
    @Email
    @JsonProperty(value = "user_email")
    private String email;

    @NotEmpty
    @Pattern(regexp = "^1\\d{10}$")
    @JsonProperty(value = "user_phone")
    private String phone;
}
