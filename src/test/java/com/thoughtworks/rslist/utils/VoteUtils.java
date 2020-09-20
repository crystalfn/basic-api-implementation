package com.thoughtworks.rslist.utils;

import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;

import java.time.LocalDateTime;

public class VoteUtils {
    public static VoteEntity setVote(UserEntity userEntity,
                               RsEventEntity rsEventEntity,
                               int voteNumber) {
        return VoteEntity.builder()
            .userEntity(userEntity)
            .rsEventEntity(rsEventEntity)
            .voteTime(LocalDateTime.now())
            .voteNumber(voteNumber)
            .build();
    }
}
