package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.utils.DtoUtils;
import com.thoughtworks.rslist.utils.EntityUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import javax.swing.text.html.parser.Entity;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RsEventRepository rsEventRepository;

    @BeforeEach
    public void resetUserRepository() {
        userRepository.deleteAll();
    }

    @Test
    void should_get_one_rs_event() throws Exception {
        UserEntity userEntity = EntityUtil.createUserEntity();
        userRepository.save(userEntity);
        RsEventEntity rsEventEntity = EntityUtil.createRsEventEntity(userEntity);
        rsEventRepository.save(rsEventEntity);

        mockMvc.perform(get("/rs/{id}", rsEventEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.eventName", is("事件1")))
            .andExpect(jsonPath("$.keywords", is("经济")))
            .andExpect(jsonPath("$.id", is(rsEventEntity.getId())))
            .andExpect(jsonPath("$.voteNumber", is(0)));
    }

    @Test
    void should_return_400_and_error_message_is_invalid_index() throws Exception {
        UserEntity userEntity = EntityUtil.createUserEntity();
        userRepository.save(userEntity);
        RsEventEntity rsEventEntity = EntityUtil.createRsEventEntity(userEntity);
        rsEventRepository.save(rsEventEntity);

        mockMvc.perform(get("/rs/{id}", 111))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorMessage", is("invalid index")));
    }

    @Test
    void should_get_rs_event_by_range() throws Exception {
        UserEntity userEntity = EntityUtil.createUserEntity();
        userRepository.save(userEntity);
        RsEventEntity rsEventEntity = EntityUtil.createRsEventEntity(userEntity);
        rsEventRepository.save(rsEventEntity);

        mockMvc.perform(get("/rs/events?start=1&end=1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].eventName", is("事件1")))
            .andExpect(jsonPath("$[0].keywords", is("经济")))
            .andExpect(jsonPath("$[0].id", is(rsEventEntity.getId())))
            .andExpect(jsonPath("$[0].voteNumber", is(0)));
    }

    @Test
    void should_return_400_when_start_or_end_out_of_range() throws Exception {
        UserEntity userEntity = EntityUtil.createUserEntity();
        userRepository.save(userEntity);
        RsEventEntity rsEventEntity = EntityUtil.createRsEventEntity(userEntity);
        rsEventRepository.save(rsEventEntity);

        mockMvc.perform(get("/rs/events?start=1&end=111"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorMessage", is("invalid request param")));
    }

    @Test
    void should_get_all_rs_event() throws Exception {
        UserEntity userEntity = EntityUtil.createUserEntity();
        userRepository.save(userEntity);
        RsEventEntity rsEventEntity = EntityUtil.createRsEventEntity(userEntity);
        rsEventRepository.save(rsEventEntity);

        mockMvc.perform(get("/rs/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].eventName", is("事件1")))
            .andExpect(jsonPath("$[0].keywords", is("经济")))
            .andExpect(jsonPath("$[0].id", is(rsEventEntity.getId())))
            .andExpect(jsonPath("$[0].voteNumber", is(0)));
    }

    @Test
    void should_add_one_rs_event_when_user_exit() throws Exception {
        UserEntity userEntity = EntityUtil.createUserEntity();
        userRepository.save(userEntity);
        final int userId = userEntity.getId();
        final RsEvent rsEvent = RsEvent.builder()
            .eventName("事件1")
            .keywords("经济")
            .userId(userId)
            .build();

        ObjectMapper objectMapper = new ObjectMapper();
        final String jsonValue = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event")
            .content(jsonValue)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());

        final List<RsEventEntity> rsEvents = rsEventRepository.findAll();
        assertEquals(1, rsEvents.size());
        assertEquals("事件1", rsEvents.get(0).getEventName());
    }

    @Test
    void should_not_add_one_rs_event_when_user_not_exit() throws Exception {
        final RsEvent rsEvent = RsEvent.builder()
            .eventName("事件1")
            .keywords("经济")
            .userId(111)
            .build();

        ObjectMapper objectMapper = new ObjectMapper();
        final String jsonValue = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event")
            .content(jsonValue)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        final List<RsEventEntity> rsEvents = rsEventRepository.findAll();
        assertEquals(0, rsEvents.size());
    }

    @Test
    void should_return_400_when_invalid_params() throws Exception {
        UserEntity userEntity = EntityUtil.createUserEntity();
        userRepository.save(userEntity);
        final RsEvent rsEvent = RsEvent.builder()
            .eventName("事件1")
            .keywords(null)
            .userId(userEntity.getId())
            .build();

        ObjectMapper objectMapper = new ObjectMapper();
        final String jsonValue = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event")
            .content(jsonValue)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorMessage", is("invalid param")));;
    }

    @Test
    void should_update_rs_event_when_user_id_and_rs_event_id_related() throws Exception {
        UserEntity userEntity = EntityUtil.createUserEntity();
        userRepository.save(userEntity);
        RsEventEntity rsEventEntity = EntityUtil.createRsEventEntity(userEntity);
        rsEventRepository.save(rsEventEntity);

        final RsEvent rsEvent = RsEvent.builder()
            .eventName("更新事件")
            .keywords("经济")
            .userId(userEntity.getId())
            .build();
        ObjectMapper objectMapper = new ObjectMapper();
        final String modifyEventJson = objectMapper.writeValueAsString(rsEvent);

        final Integer rsEventEntityId = rsEventEntity.getId();
        mockMvc.perform(patch("/rs/event/{id}", rsEventEntityId)
            .content(modifyEventJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        final RsEventEntity modifyRsEventEntity = rsEventRepository.findById(rsEventEntityId).get();
        assertEquals("更新事件", modifyRsEventEntity.getEventName());
        assertEquals("经济", modifyRsEventEntity.getKeywords());
    }

    @Test
    void should_not_update_rs_event_when_user_id_and_rs_event_id_unrelated() throws Exception {
        UserEntity userEntity = EntityUtil.createUserEntity();
        userRepository.save(userEntity);
        RsEventEntity rsEventEntity = EntityUtil.createRsEventEntity(userEntity);
        rsEventRepository.save(rsEventEntity);

        final RsEvent rsEvent = RsEvent.builder()
            .eventName("更新事件")
            .keywords("经济")
            .userId(111)
            .build();
        ObjectMapper objectMapper = new ObjectMapper();
        final String modifyEventJson = objectMapper.writeValueAsString(rsEvent);

        final Integer rsEventEntityId = rsEventEntity.getId();
        mockMvc.perform(patch("/rs/event/{id}", rsEventEntityId)
            .content(modifyEventJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void should_only_update_rs_event_name_when_keywords_is_null() throws Exception {
        UserEntity userEntity = EntityUtil.createUserEntity();
        userRepository.save(userEntity);
        RsEventEntity rsEventEntity = EntityUtil.createRsEventEntity(userEntity);
        rsEventRepository.save(rsEventEntity);

        final RsEvent rsEvent = RsEvent.builder()
            .eventName("更新事件")
            .userId(userEntity.getId())
            .build();
        ObjectMapper objectMapper = new ObjectMapper();
        final String modifyEventJson = objectMapper.writeValueAsString(rsEvent);

        final Integer rsEventEntityId = rsEventEntity.getId();
        mockMvc.perform(patch("/rs/event/{id}", rsEventEntityId)
            .content(modifyEventJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        final RsEventEntity modifyRsEventEntity = rsEventRepository.findById(rsEventEntityId).get();
        assertEquals("更新事件", modifyRsEventEntity.getEventName());
        assertEquals("经济", modifyRsEventEntity.getKeywords());
    }

    @Test
    void should_only_update_rs_event_keywords_when_name_is_null() throws Exception {
        UserEntity userEntity = EntityUtil.createUserEntity();
        userRepository.save(userEntity);
        RsEventEntity rsEventEntity = EntityUtil.createRsEventEntity(userEntity);
        rsEventRepository.save(rsEventEntity);

        final RsEvent rsEvent = RsEvent.builder()
            .keywords("非经济")
            .userId(userEntity.getId())
            .build();
        ObjectMapper objectMapper = new ObjectMapper();
        final String modifyEventJson = objectMapper.writeValueAsString(rsEvent);

        final Integer rsEventEntityId = rsEventEntity.getId();
        mockMvc.perform(patch("/rs/event/{id}", rsEventEntityId)
            .content(modifyEventJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        final RsEventEntity modifyRsEventEntity = rsEventRepository.findById(rsEventEntityId).get();
        assertEquals("事件1", modifyRsEventEntity.getEventName());
        assertEquals("非经济", modifyRsEventEntity.getKeywords());
    }

    @Test
    void should_delete_rs_event() throws Exception {
        UserEntity userEntity = EntityUtil.createUserEntity();
        userRepository.save(userEntity);
        RsEventEntity rsEventEntity = EntityUtil.createRsEventEntity(userEntity);
        rsEventRepository.save(rsEventEntity);

        mockMvc.perform(get("/rs/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(delete("/rs/{id}", rsEventEntity.getId()))
            .andExpect(status().isOk());

        mockMvc.perform(get("/rs/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }
}