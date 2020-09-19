package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
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
import java.util.stream.Collectors;

@RestController
public class UserController {

    final
    UserRepository userRepository;

    final
    RsEventRepository rsEventRepository;

    public UserController(UserRepository userRepository, RsEventRepository rsEventRepository) {
        this.userRepository = userRepository;
        this.rsEventRepository = rsEventRepository;
    }

    @PostMapping("/user/register")
    public ResponseEntity register(@Valid @RequestBody UserDto userDto) {
        UserEntity userEntity = UserEntity.convertUserToUserEntity(userDto);
        userRepository.save(userEntity);

        return ResponseEntity
            .created(null)
            .header("index", String.valueOf(userRepository.findAll().size()))
            .build();
    }

    @GetMapping("/user/list")
    public ResponseEntity<List<UserDto>> getAllUser() {
        final List<UserEntity> userEntityList = userRepository.findAll();
        return ResponseEntity.ok(
            userEntityList.stream()
                .map(UserDto::convertUserEntityToUserDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/user/{id}")
    public UserEntity getUserById(@PathVariable int id) {
        return userRepository.findById(id).get();
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity deleteUserById(@PathVariable int id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
