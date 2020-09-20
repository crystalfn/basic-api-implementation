package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.utils.DtoUtils;
import com.thoughtworks.rslist.utils.EntityUtil;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

    @Autowired
    RsEventRepository rsEventRepository;

    @BeforeEach
    public void initRepository() {
        userRepository.deleteAll();
    }

    @Test
    void should_register_user() throws Exception {
        UserDto userDto = DtoUtils.createUser();
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
            .content(userDtoJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().string("index", String.valueOf(1)));

        final List<UserEntity> userEntityList = userRepository.findAll();
        assertEquals(1, userEntityList.size());
        assertEquals("张三", userEntityList.get(0).getUserName());
    }

    @Test
    void name_should_not_empty() throws Exception {
        UserDto userDto = new UserDto("", 25, "female", "crystal@qq.com", "13177777777");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
            .content(userDtoJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorMessage", is("invalid user")));

        final List<UserEntity> userEntityList = userRepository.findAll();
        assertEquals(0, userEntityList.size());
    }

    @Test
    void name_should_not_more_than_8() throws Exception {
        UserDto userDto = new UserDto("crystal_hhh", 25, "female", "crystal@qq.com", "13177777777");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
            .content(userDtoJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorMessage", is("invalid user")));

        final List<UserEntity> userEntityList = userRepository.findAll();
        assertEquals(0, userEntityList.size());
    }

    @Test
    void gender_should_not_empty() throws Exception {
        UserDto userDto = new UserDto("crystal", 25, "", "crystal@qq.com", "13177777777");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
            .content(userDtoJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorMessage", is("invalid user")));

        final List<UserEntity> userEntityList = userRepository.findAll();
        assertEquals(0, userEntityList.size());
    }

    @Test
    void age_should_not_null() throws Exception {
        UserDto userDto = new UserDto("crystal", null, "female", "crystal@qq.com", "13177777777");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
            .content(userDtoJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorMessage", is("invalid user")));

        final List<UserEntity> userEntityList = userRepository.findAll();
        assertEquals(0, userEntityList.size());
    }

    @Test
    void age_should_not_less_than_18() throws Exception {
        UserDto userDto = new UserDto("crystal", 17, "female", "crystal@qq.com", "13177777777");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
            .content(userDtoJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorMessage", is("invalid user")));

        final List<UserEntity> userEntityList = userRepository.findAll();
        assertEquals(0, userEntityList.size());
    }

    @Test
    void age_should_not_more_than_100() throws Exception {
        UserDto userDto = new UserDto("crystal", 101, "female", "crystal@qq.com", "13177777777");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
            .content(userDtoJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorMessage", is("invalid user")));

        final List<UserEntity> userEntityList = userRepository.findAll();
        assertEquals(0, userEntityList.size());
    }

    @Test
    void email_should_not_empty() throws Exception {
        UserDto userDto = new UserDto("crystal", 25, "female", "", "13177777777");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
            .content(userDtoJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorMessage", is("invalid user")));

        final List<UserEntity> userEntityList = userRepository.findAll();
        assertEquals(0, userEntityList.size());
    }

    @Test
    void email_should_valid() throws Exception {
        UserDto userDto = new UserDto("crystal", 25, "female", "@qq.com", "13177777777");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
            .content(userDtoJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorMessage", is("invalid user")));

        final List<UserEntity> userEntityList = userRepository.findAll();
        assertEquals(0, userEntityList.size());
    }

    @Test
    void phone_should_not_empty() throws Exception {
        UserDto userDto = new UserDto("crystal", 25, "female", "crystal@qq.com", "");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
            .content(userDtoJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorMessage", is("invalid user")));

        final List<UserEntity> userEntityList = userRepository.findAll();
        assertEquals(0, userEntityList.size());
    }

    @Test
    void phone_should_start_with_1() throws Exception {
        UserDto userDto = new UserDto("crystal", 25, "female", "crystal@qq.com", "21377777777");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
            .content(userDtoJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorMessage", is("invalid user")));

        final List<UserEntity> userEntityList = userRepository.findAll();
        assertEquals(0, userEntityList.size());
    }

    @Test
    void phone_length_should_be_11() throws Exception {
        UserDto userDto = new UserDto("crystal", 25, "female", "crystal@qq.com", "1137777777");
        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
            .content(userDtoJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorMessage", is("invalid user")));

        final List<UserEntity> userEntityList = userRepository.findAll();
        assertEquals(0, userEntityList.size());
    }

    @Test
    void get_user_by_user_id() throws Exception {
        UserEntity userEntity = EntityUtil.createUserEntity();
        userRepository.save(userEntity);

        mockMvc.perform(get("/user/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.userName", is("张三")))
            .andExpect(jsonPath("$.age", is(22)));
    }

    @Test
    void delete_user_and_user_created_rs_events_by_user_id() throws Exception {
        UserEntity userEntity = EntityUtil.createUserEntity();
        userRepository.save(userEntity);

        RsEventEntity rsEventEntityFirst = RsEventEntity.builder()
            .eventName("事件1")
            .keywords("新闻")
            .userEntity(userEntity)
            .build();
        rsEventRepository.save(rsEventEntityFirst);

        RsEventEntity rsEventEntitySecond = RsEventEntity.builder()
            .eventName("事件2")
            .keywords("新闻")
            .userEntity(userEntity)
            .build();
        rsEventRepository.save(rsEventEntitySecond);

        final List<UserEntity> userEntityList = userRepository.findAll();
        assertEquals(1, userEntityList.size());
        final List<RsEventEntity> rsEventEntityList = rsEventRepository.findAll();
        assertEquals(2, rsEventEntityList.size());

        mockMvc.perform(delete("/user/1"))
            .andExpect(status().isOk());

        final List<UserEntity> userEntityListAfterDelete = userRepository.findAll();
        assertEquals(0, userEntityListAfterDelete.size());
        final List<RsEventEntity> rsEventEntityListAfterDelete =  rsEventRepository.findAll();
        assertEquals(0, rsEventEntityListAfterDelete.size());
    }
}