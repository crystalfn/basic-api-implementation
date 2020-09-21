package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.service.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VoteController {
    final
    VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping("/rs/vote/{rsEventId}")
    public ResponseEntity vote(@PathVariable Integer rsEventId, @RequestBody VoteDto voteDto) {
        if (voteService.vote(rsEventId, voteDto)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/votes")
    public ResponseEntity<List<VoteDto>> getVotes(@RequestParam int userEntityId,
                                                  @RequestParam int rsEventEntityId,
                                                  @RequestParam(defaultValue = "1") int pageIndex,
                                                  @RequestParam(defaultValue = "5") int size) {
        final List<VoteDto> votes = voteService.getVotes(userEntityId, rsEventEntityId, pageIndex, size);
        return ResponseEntity.ok(votes);
    }

    @GetMapping("/votes/time")
    public ResponseEntity<List<VoteDto>> getVotesBetweenStartTimeAndEndTime(@RequestParam String startTime,
                                                                            @RequestParam String endTime) {
        return voteService.getVotesBetweenStartTimeAndEndTime(startTime, endTime);
    }
}
