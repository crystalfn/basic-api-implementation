package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.utils.EntityUtil;
import com.thoughtworks.rslist.utils.VoteUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class VoteControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    VoteRepository voteRepository;

    ObjectMapper objectMapper;
    UserEntity userEntity;
    RsEventEntity rsEventEntity;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        userEntity = EntityUtil.createUserEntity();
        userRepository.save(userEntity);
        rsEventEntity = EntityUtil.createRsEventEntity(userEntity);
        rsEventRepository.save(rsEventEntity);
    }

    @AfterEach
    void resetRepository() {
        voteRepository.deleteAll();
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void should_vote_success_when_user_vote_number_more_than_vote_number() throws Exception {
        int voteNumber = 7;
        final VoteDto voteDto = VoteDto.builder()
            .rsEventId(rsEventEntity.getId())
            .userId(userEntity.getId())
            .voteNumber(voteNumber)
            .voteTime(null)
            .build();
        ObjectMapper objectMapper = new ObjectMapper();
        final String voteJson = objectMapper.writeValueAsString(voteDto);

        mockMvc.perform(post("/rs/vote/{rsEventId}", rsEventEntity.getId())
            .content(voteJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        final List<VoteEntity> voteEntityList = voteRepository.findAll();
        assertEquals(voteNumber, voteEntityList.get(0).getVoteNumber());

        final int currentVoteNumber = rsEventRepository.findById(rsEventEntity.getId()).get().getVoteNumber();
        assertEquals(rsEventEntity.getVoteNumber() + voteNumber, currentVoteNumber);

        final int remainVoteNumber = userRepository.findById(userEntity.getId()).get().getVoteNumber();
        assertEquals(userEntity.getVoteNumber() - voteNumber, remainVoteNumber);
    }

    @Test
    void should_vote_fail_when_user_vote_number_less_than_vote_number() throws Exception {
        int voteNumber = 11;
        final VoteDto voteDto = VoteDto.builder()
            .rsEventId(rsEventEntity.getId())
            .userId(userEntity.getId())
            .voteNumber(voteNumber)
            .voteTime(null)
            .build();
        ObjectMapper objectMapper = new ObjectMapper();
        final String voteJson = objectMapper.writeValueAsString(voteDto);

        mockMvc.perform(post("/rs/vote/{rsEventId}", rsEventEntity.getId())
            .content(voteJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void should_vote_fail_when_rs_event_id_not_exits() throws Exception {
        int voteNumber = 7;
        final VoteDto voteDto = VoteDto.builder()
            .rsEventId(111)
            .userId(userEntity.getId())
            .voteNumber(voteNumber)
            .voteTime(null)
            .build();
        ObjectMapper objectMapper = new ObjectMapper();
        final String voteJson = objectMapper.writeValueAsString(voteDto);

        mockMvc.perform(post("/rs/vote/{rsEventId}", 111)
            .content(voteJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void should_vote_fail_when_user_id_not_exits() throws Exception {
        int voteNumber = 7;
        final VoteDto voteDto = VoteDto.builder()
            .rsEventId(rsEventEntity.getId())
            .userId(111)
            .voteNumber(voteNumber)
            .voteTime(null)
            .build();
        ObjectMapper objectMapper = new ObjectMapper();
        final String voteJson = objectMapper.writeValueAsString(voteDto);

        mockMvc.perform(post("/rs/vote/{rsEventId}", rsEventEntity.getId())
            .content(voteJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void should_get_votes_by_user_id_and_rs_event_id () throws Exception {
        for (int i = 1; i < 5; i++) {
            final VoteEntity voteEntity = VoteUtils.setVote(userEntity, rsEventEntity, i);
            voteRepository.save(voteEntity);
        }

        mockMvc.perform(get("/votes")
            .param("userEntityId", String.valueOf(userEntity.getId()))
            .param("rsEventEntityId", String.valueOf(rsEventEntity.getId())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(4)))
            .andExpect(jsonPath("$[0].userId", is(userEntity.getId()) ))
            .andExpect(jsonPath("$[0].rsEventId", is(rsEventEntity.getId())))
            .andExpect(jsonPath("$[0].voteNumber", is(1)));
    }

    @Test
    void should_get_votes_in_page_by_user_id_and_rs_event_id () throws Exception {
        for (int i = 1; i < 10; i++) {
            final VoteEntity voteEntity = VoteUtils.setVote(userEntity, rsEventEntity, 1);
            voteRepository.save(voteEntity);
        }

        mockMvc.perform(get("/votes/")
            .param("userEntityId", String.valueOf(userEntity.getId()))
            .param("rsEventEntityId", String.valueOf(rsEventEntity.getId())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(5)))
            .andExpect(jsonPath("$[0].userId", is(userEntity.getId())))
            .andExpect(jsonPath("$[0].rsEventId", is(rsEventEntity.getId())))
            .andExpect(jsonPath("$[0].voteNumber", is(1)));

        mockMvc.perform(get("/votes/")
            .param("userEntityId", String.valueOf(userEntity.getId()))
            .param("rsEventEntityId", String.valueOf(rsEventEntity.getId()))
            .param("pageIndex", "2")
            .param("size", "5"))
            .andExpect(jsonPath("$", hasSize(4)))
            .andExpect(jsonPath("$[0].voteNumber", is(1)));
    }

    @Test
    void should_get_votes_between_start_time_and_end_time() throws Exception {
        for (int i = 1; i < 10; i++) {
            final VoteEntity voteEntity = VoteUtils.setVote(userEntity, rsEventEntity, 1);
            voteRepository.save(voteEntity);
        }

        String startTime = "2020-09-20 00:00:00";
        String endTime = "2020-12-20 23:59:59";
        mockMvc.perform(get("/votes/time")
            .param("startTime", startTime)
            .param("endTime", endTime))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(9)))
            .andExpect(jsonPath("$[0].userId", is(userEntity.getId())))
            .andExpect(jsonPath("$[0].rsEventId", is(rsEventEntity.getId())))
            .andExpect(jsonPath("$[0].voteNumber", is(1)));
    }

    @Test
    void should_return_400_if_start_time_more_than_end_time() throws Exception {
        for (int i = 1; i < 10; i++) {
            final VoteEntity voteEntity = VoteUtils.setVote(userEntity, rsEventEntity, 1);
            voteRepository.save(voteEntity);
        }

        String startTime = "2020-12-20 23:59:59";
        String endTime = "2020-09-20 00:00:00";
        mockMvc.perform(get("/votes/time")
            .param("startTime", startTime)
            .param("endTime", endTime))
            .andExpect(status().isBadRequest());
    }
}