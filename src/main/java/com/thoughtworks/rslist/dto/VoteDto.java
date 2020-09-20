package com.thoughtworks.rslist.dto;

import com.thoughtworks.rslist.entity.VoteEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteDto {
    @Valid
    private int rsEventId;

    @Valid
    private int userId;

    @Valid
    private int voteNumber;
    private LocalDateTime voteTime;

    public static VoteDto convertVoteEntityToVoteDto(VoteEntity voteEntity) {
        return VoteDto.builder()
            .rsEventId(voteEntity.getRsEventEntity().getId())
            .userId(voteEntity.getUserEntity().getId())
            .voteNumber(voteEntity.getVoteNumber())
            .voteTime(voteEntity.getVoteTime())
            .build();
    }
}
