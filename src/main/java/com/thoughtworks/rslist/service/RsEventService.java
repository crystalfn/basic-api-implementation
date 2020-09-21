package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exceptions.InvalidIndexException;
import com.thoughtworks.rslist.exceptions.InvalidRequestParamException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RsEventService {
    final RsEventRepository rsEventRepository;
    final UserRepository userRepository;

    public RsEventService(RsEventRepository rsEventRepository, UserRepository userRepository) {
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
    }


    public RsEvent getOneRsEvent(int rsEventId) throws InvalidIndexException {
        final Optional<RsEventEntity> rsEventEntityOptional = rsEventRepository.findById(rsEventId);
        if (!rsEventEntityOptional.isPresent()) {
            throw new InvalidIndexException("invalid index");
        }

        return RsEvent.convertRsEventEntityToRsEvent(rsEventEntityOptional.get());
    }

    public List<RsEvent> getAllEvents() {
        List<RsEventEntity> rsEvents = rsEventRepository.findAll();
        return rsEvents
            .stream()
            .map(RsEvent::convertRsEventEntityToRsEvent)
            .collect(Collectors.toList());
    }

    public List<RsEvent> getRsEventByRange(Integer start, Integer end) throws InvalidRequestParamException {
        List<RsEventEntity> rsEvents = rsEventRepository.findAll();
        if (start < 0 || start > rsEvents.size() || end < start || end > rsEvents.size()) {
            throw new InvalidRequestParamException("invalid request param");
        }
        return rsEvents
            .stream()
            .map(RsEvent::convertRsEventEntityToRsEvent)
            .collect(Collectors.toList()).subList(start - 1, end);
    }

    public ResponseEntity addRsEvent(RsEvent rsEvent) {
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

    public ResponseEntity updateRsEvent(int id, RsEvent rsEvent) {
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

    public void deleteRsEvent(int id) {
        rsEventRepository.deleteById(id);
    }
}
