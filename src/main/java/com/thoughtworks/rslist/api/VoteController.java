package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class VoteController {
    final
    VoteRepository voteRepository;
    final
    UserRepository userRepository;
    final
    RsEventRepository rsEventRepository;

    public VoteController(VoteRepository voteRepository, UserRepository userRepository, RsEventRepository rsEventRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.rsEventRepository = rsEventRepository;
    }

    @PostMapping("/rs/vote/{rsEventId}")
    public ResponseEntity vote(@PathVariable Integer rsEventId, @RequestBody VoteDto voteDto) {
        final Optional<RsEventEntity> rsEventEntityOptional = rsEventRepository.findById(rsEventId);
        final Optional<UserEntity> userEntityOptional = userRepository.findById(voteDto.getUserId());
        if (!rsEventEntityOptional.isPresent() ||
            !userEntityOptional.isPresent() ||
            userEntityOptional.get().getVoteNumber() < voteDto.getVoteNumber()) {
            return ResponseEntity.badRequest().build();
        }

        final VoteEntity voteEntity = VoteEntity.builder()
            .userEntity(userEntityOptional.get())
            .rsEventEntity(rsEventEntityOptional.get())
            .voteNumber(voteDto.getVoteNumber())
            .voteTime(voteDto.getVoteTime())
            .build();
        voteRepository.save(voteEntity);

        rsEventEntityOptional.get().setVoteNumber(rsEventEntityOptional.get().getVoteNumber() + voteDto.getVoteNumber());
        rsEventRepository.save(rsEventEntityOptional.get());

        userEntityOptional.get().setVoteNumber(userEntityOptional.get().getVoteNumber() - voteDto.getVoteNumber());
        userRepository.save(userEntityOptional.get());

        return ResponseEntity.ok().build();
    }
}
