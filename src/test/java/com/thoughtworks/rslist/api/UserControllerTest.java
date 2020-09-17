package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void resetUserRepository() {
        userRepository.deleteAll();
    }

    @Test
    void should_register_user() throws Exception {
        UserDto userDto = new UserDto("crystal", 25, "female", "crystal@qq.com", "13177777777");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
            .content(userDtoJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().string("index", String.valueOf(2)));

        final List<UserEntity> userEntityList = userRepository.findAll();
        assertEquals(1, userEntityList.size());
        assertEquals("crystal", userEntityList.get(0).getUserName());
    }

    @Test
    void name_should_not_empty() throws Exception {
        UserDto userDto = new UserDto("", 25, "female", "crystal@qq.com", "13177777777");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
            .content(userDtoJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void name_should_not_more_than_8() throws Exception {
        UserDto userDto = new UserDto("crystal_hhh", 25, "female", "crystal@qq.com", "13177777777");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
            .content(userDtoJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void gender_should_not_empty() throws Exception {
        UserDto userDto = new UserDto("crystal", 25, "", "crystal@qq.com", "13177777777");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
            .content(userDtoJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void age_should_not_null() throws Exception {
        UserDto userDto = new UserDto("crystal", null, "female", "crystal@qq.com", "13177777777");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
            .content(userDtoJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void age_should_not_less_than_18() throws Exception {
        UserDto userDto = new UserDto("crystal", 17, "female", "crystal@qq.com", "13177777777");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
            .content(userDtoJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void age_should_not_more_than_100() throws Exception {
        UserDto userDto = new UserDto("crystal", 101, "female", "crystal@qq.com", "13177777777");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
            .content(userDtoJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void email_should_not_empty() throws Exception {
        UserDto userDto = new UserDto("crystal", 25, "female", "", "13177777777");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
            .content(userDtoJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void email_should_valid() throws Exception {
        UserDto userDto = new UserDto("crystal", 25, "female", "@qq.com", "13177777777");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
            .content(userDtoJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void phone_should_not_empty() throws Exception {
        UserDto userDto = new UserDto("crystal", 25, "female", "crystal@qq.com", "");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
            .content(userDtoJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void phone_should_start_with_1() throws Exception {
        UserDto userDto = new UserDto("crystal", 25, "female", "crystal@qq.com", "21377777777");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
            .content(userDtoJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void phone_length_should_be_11() throws Exception {
        UserDto userDto = new UserDto("crystal", 25, "female", "crystal@qq.com", "1137777777");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
            .content(userDtoJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void get_user_by_user_id() throws Exception {
        UserEntity userEntity = UserEntity.builder()
            .userName("王五")
            .age(22)
            .gender("male")
            .email("five@qq.com")
            .phone("13011111111")
            .build();
        userRepository.save(userEntity);

        mockMvc.perform(get("/user/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.userName", is("王五")))
            .andExpect(jsonPath("$.age", is(22)));
    }
}