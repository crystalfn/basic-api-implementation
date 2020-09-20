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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}