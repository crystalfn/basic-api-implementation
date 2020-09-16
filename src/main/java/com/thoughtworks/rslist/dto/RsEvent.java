package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RsEvent {
    private String eventName;
    private String keywords;
    private UserDto user;

    public RsEvent(String eventName, String keywords) {
        this.eventName = eventName;
        this.keywords = keywords;
    }

    @JsonIgnore
    public UserDto getUser() {
        return user;
    }

    @JsonProperty
    public void setUser() {
        this.user = user;
    }
}
