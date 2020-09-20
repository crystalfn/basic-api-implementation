package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @GetMapping("/votes/")
    public ResponseEntity<List<VoteDto>> getVotes(@RequestParam int userEntityId,
                                  @RequestParam int rsEventEntityId,
                                  @RequestParam(defaultValue = "1") int pageIndex,
                                  @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(pageIndex - 1, size);
        final List<VoteEntity> votes = voteRepository.findAllByUserEntityIdAndRsEventEntityId(userEntityId, rsEventEntityId, pageable);
        return ResponseEntity.ok(votes.stream()
            .map(VoteDto::convertVoteEntityToVoteDto)
            .collect(Collectors.toList()));
    }

    @GetMapping("/votes/byTime")
    public ResponseEntity<List<VoteDto>> getVotesBetweenStartTimeAndEndTime(@RequestParam String startTime,
                                  @RequestParam String endTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTimeFormatter = LocalDateTime.parse(startTime, dateTimeFormatter);
        LocalDateTime entTimeFormatter = LocalDateTime.parse(endTime, dateTimeFormatter);

        if (startTimeFormatter.isAfter(entTimeFormatter)) {
            return ResponseEntity.badRequest().build();
        }

        final List<VoteEntity> votes = voteRepository.findAllByVoteTimeBetween(startTimeFormatter, entTimeFormatter);
        return ResponseEntity.ok(votes.stream()
            .map(VoteDto::convertVoteEntityToVoteDto)
            .collect(Collectors.toList()));
    }
}
