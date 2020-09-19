package com.thoughtworks.rslist.utils;

import com.thoughtworks.rslist.dto.UserDto;

public class DtoUtils {
    public static UserDto createUser() {
        return UserDto.builder()
            .userName("张三")
            .age(22)
            .gender("male")
            .email("three@qq.com")
            .phone("13100000000")
            .build();
    }
}
