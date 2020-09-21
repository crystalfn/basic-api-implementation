package com.thoughtworks.rslist.service;

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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VoteService {
    final RsEventRepository rsEventRepository;
    final UserRepository userRepository;
    final VoteRepository voteRepository;

    public VoteService(RsEventRepository rsEventRepository,
                       UserRepository userRepository,
                       VoteRepository voteRepository) {
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    public Boolean vote(Integer rsEventId, VoteDto voteDto) {
        final Optional<RsEventEntity> rsEventEntityOptional = rsEventRepository.findById(rsEventId);
        final Optional<UserEntity> userEntityOptional = userRepository.findById(voteDto.getUserId());
        if (!rsEventEntityOptional.isPresent() ||
            !userEntityOptional.isPresent() ||
            userEntityOptional.get().getVoteNumber() < voteDto.getVoteNumber()) {
            return false;
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

        return true;
    }

    public List<VoteDto> getVotes(int userEntityId, int rsEventEntityId, int pageIndex, int size) {
        Pageable pageable = PageRequest.of(pageIndex - 1, size);
        final List<VoteEntity> votes = voteRepository.findAllByUserEntityIdAndRsEventEntityId(userEntityId, rsEventEntityId, pageable);
        return votes.stream()
            .map(VoteDto::convertVoteEntityToVoteDto)
            .collect(Collectors.toList());
    }

    public ResponseEntity<List<VoteDto>> getVotesBetweenStartTimeAndEndTime(String startTime,String endTime) {
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
