package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.thoughtworks.rslist.entity.RsEventEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
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

    @NotNull
    @JsonView(WithoutUser.class)
    private int userId;

    @Valid
    @JsonView(WithUser.class)
    private UserDto user;

    public RsEvent(String eventName, String keywords) {
        this.eventName = eventName;
        this.keywords = keywords;
    }

    public static RsEvent convertRsEventEntityToRsEvent(RsEventEntity rsEventEntity) {
        return RsEvent.builder()
            .eventName(rsEventEntity.getEventName())
            .keywords(rsEventEntity.getKeywords())
            .build();
    }
}
