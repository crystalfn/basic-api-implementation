package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RsEvent {
    public interface WithoutUser{};
    public interface WithUser extends WithoutUser{};

    @JsonView(RsEvent.WithoutUser.class)
    @NotEmpty
    private String eventName;

    @JsonView(RsEvent.WithoutUser.class)
    @NotEmpty
    private String keywords;

    @Valid
    @JsonView(WithUser.class)
    private UserDto user;

    public RsEvent(String eventName, String keywords) {
        this.eventName = eventName;
        this.keywords = keywords;
    }

    @Valid
    @JsonView(WithUser.class)
    private int userId;
}
