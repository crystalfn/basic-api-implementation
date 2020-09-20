package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RsController {

    final
    UserRepository userRepository;

    final
    RsEventRepository rsEventRepository;

    public RsController(UserRepository userRepository, RsEventRepository rsEventRepository) {
        this.userRepository = userRepository;
        this.rsEventRepository = rsEventRepository;
    }

    @JsonView(RsEvent.WithoutUser.class)
    @GetMapping("/rs/{id}")
    public ResponseEntity<RsEvent> getOneRsEvent(@PathVariable int id) {
        final RsEvent rsEvent = RsEvent.convertRsEventEntityToRsEvent(rsEventRepository.findById(id).get());
        return ResponseEntity.ok(rsEvent);
    }

    @JsonView(RsEvent.WithoutUser.class)
    @GetMapping("rs/list")
    public ResponseEntity<List<RsEvent>> getAllEvents() {
        List<RsEventEntity> rsEvents = rsEventRepository.findAll();
        return ResponseEntity.ok(
            rsEvents.stream()
                .map(RsEvent::convertRsEventEntityToRsEvent)
                .collect(Collectors.toList()));
    }

    @GetMapping("rs/event")
    public ResponseEntity<List<RsEvent>> getRsEventByRange(@RequestParam(required = false) Integer start,
                                                           @RequestParam(required = false) Integer end) {
        List<RsEventEntity> rsEvents = rsEventRepository.findAll();
        if (start == null || end == null) {
            return ResponseEntity.ok(
                rsEvents.stream()
                    .map(RsEvent::convertRsEventEntityToRsEvent)
                    .collect(Collectors.toList()));
        }
        return ResponseEntity.ok(
            rsEvents.stream()
                .map(RsEvent::convertRsEventEntityToRsEvent)
                .collect(Collectors.toList()).subList(start - 1, end));
    }

    @PostMapping("rs/addEvent")
    public ResponseEntity addRsEvent(@RequestBody RsEvent rsEvent) {
        if (!userRepository.existsById(rsEvent.getUserId())) {
            return ResponseEntity.badRequest().build();
        }

        RsEventEntity rsEventEntity = RsEventEntity.builder()
            .eventName(rsEvent.getEventName())
            .keywords(rsEvent.getKeywords())
            .userEntity(UserEntity.builder()
                    .id(rsEvent.getUserId())
                    .build())
            .build();
        rsEventRepository.save(rsEventEntity);
        return ResponseEntity.created(null).build();
    }

    @PatchMapping("rs/update/{id}")
    public ResponseEntity modifyRsEvent(@PathVariable int id,
                                        @RequestBody RsEvent rsEvent) {
        final RsEventEntity rsEventEntity = rsEventRepository.findById(id).get();
        if (rsEvent.getUserId() == rsEventEntity.getUserEntity().getId()) {
            if (rsEvent.getEventName() != null) {
                rsEventEntity.setEventName(rsEvent.getEventName());
            }
            if (rsEvent.getKeywords() != null) {
                rsEventEntity.setKeywords(rsEvent.getKeywords());
            }
            rsEventRepository.save(rsEventEntity);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("rs/delete/{id}")
    public ResponseEntity deleteRsEvent(@PathVariable int id) {
        rsEventRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
