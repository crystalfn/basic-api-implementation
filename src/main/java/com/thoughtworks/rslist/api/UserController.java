package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    final
    UserRepository userRepository;

    final
    RsEventRepository rsEventRepository;

    final
    UserService userService;

    public UserController(UserRepository userRepository, UserService userService, RsEventRepository rsEventRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.rsEventRepository = rsEventRepository;
    }

    @PostMapping("/user/register")
    public ResponseEntity register(@Valid @RequestBody UserDto userDto) {
        userService.register(userDto);

        UserEntity userEntity = UserEntity.builder()
            .userName(userDto.getUserName())
            .age(userDto.getAge())
            .gender(userDto.getGender())
            .email(userDto.getEmail())
            .phone(userDto.getPhone())
            .build();
        userRepository.save(userEntity);

        return ResponseEntity
            .created(null)
            .header("index", String.valueOf(userService.getUserDtoList().size()))
            .build();
    }

    @GetMapping("/user/list")
    public ResponseEntity<List<UserDto>> getAllUser() {
        return ResponseEntity.ok(userService.getUserDtoList());
    }

    @GetMapping("/user/{id}")
    public UserEntity getUserById(@PathVariable int id) {
        return userRepository.findById(id).get();
    }

    @DeleteMapping("/user/delete/{id}")
    @Transactional
    public ResponseEntity deleteUserById(@PathVariable int id) {
        userRepository.deleteById(id);
        rsEventRepository.deleteAllByUserId(id);
        return ResponseEntity.ok().build();
    }
}
