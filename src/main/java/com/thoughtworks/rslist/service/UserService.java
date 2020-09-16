package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private List<UserDto> userDtoList = initUserDtiList();

    private static List<UserDto> initUserDtiList() {
        List<UserDto> tempList = new ArrayList<>();
        tempList.add(new UserDto("张三", 20, "male", "zhangSan@qq.com", "13155555555"));
        return tempList;
    }

    public void register(UserDto userDto) {
        userDtoList.add(userDto);
    }

    public List<UserDto> getUserDtoList() {
        return userDtoList;
    }
}
