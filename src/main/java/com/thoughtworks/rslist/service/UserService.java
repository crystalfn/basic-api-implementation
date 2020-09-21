package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exceptions.InvalidUserException;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public int register(UserDto userDto, BindingResult bindingResult) throws InvalidUserException {
        if (bindingResult.getAllErrors().size() > 0) {
            throw new InvalidUserException("invalid user");
        }

        UserEntity userEntity = UserEntity.convertUserToUserEntity(userDto);
        userRepository.save(userEntity);
        return userRepository.findAll().size();
    }

    public List<UserDto> getAllUsers() {
        final List<UserEntity> userEntityList = userRepository.findAll();
        return userEntityList.stream()
            .map(UserDto::convertUserEntityToUserDto)
            .collect(Collectors.toList());
    }

    public UserEntity getUserById(int id) {
        return userRepository.findById(id).get();
    }

    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }
}
