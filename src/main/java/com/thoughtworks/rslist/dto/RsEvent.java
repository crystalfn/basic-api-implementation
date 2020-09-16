package com.thoughtworks.rslist.dto;

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
}
