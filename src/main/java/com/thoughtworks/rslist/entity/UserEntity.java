package com.thoughtworks.rslist.entity;

import com.thoughtworks.rslist.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "name")
    private String userName;
    private Integer age;
    private String gender;
    private String email;
    private String phone;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.REMOVE)
    private List<RsEventEntity> rsEvents;

    public static UserEntity convertUserToUserEntity(UserDto user) {
        return UserEntity.builder()
            .userName(user.getUserName())
            .age(user.getAge())
            .email(user.getEmail())
            .gender(user.getGender())
            .phone(user.getPhone())
            .build();
    }
}
