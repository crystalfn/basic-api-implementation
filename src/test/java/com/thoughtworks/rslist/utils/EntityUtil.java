package com.thoughtworks.rslist.utils;

import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;

public class EntityUtil {
    public static UserEntity createUserEntity() {
        return UserEntity.builder()
            .userName("张三")
            .age(22)
            .gender("male")
            .email("three@qq.com")
            .phone("13100000000")
            .voteNumber(10)
            .build();
    }

    public static RsEventEntity createRsEventEntity(UserEntity userEntity) {
        return RsEventEntity.builder()
            .eventName("事件1")
            .keywords("经济")
            .userEntity(userEntity)
            .build();
    }
}
