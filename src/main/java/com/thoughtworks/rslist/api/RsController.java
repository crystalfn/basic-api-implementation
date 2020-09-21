package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.exceptions.InvalidIndexException;
import com.thoughtworks.rslist.exceptions.InvalidParamException;
import com.thoughtworks.rslist.exceptions.InvalidRequestParamException;
import com.thoughtworks.rslist.service.RsEventService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class RsController {
    final
    RsEventService rsEventService;

    public RsController(RsEventService rsEventService) {
        this.rsEventService = rsEventService;
    }

    @JsonView(RsEvent.WithoutUser.class)
    @GetMapping("/rs/{id}")
    public ResponseEntity<RsEvent> getOneRsEvent(@PathVariable int id) throws InvalidIndexException {
        try {
            final RsEvent rsEvent = rsEventService.getOneRsEvent(id);
            return ResponseEntity.ok(rsEvent);
        } catch (Exception e) {
            throw new InvalidIndexException("invalid index");
        }
    }

    @JsonView(RsEvent.WithoutUser.class)
    @GetMapping("rs/list")
    public ResponseEntity<List<RsEvent>> getAllEvents() {
        return ResponseEntity.ok(rsEventService.getAllEvents());
    }

    @GetMapping("rs/events")
    public ResponseEntity<List<RsEvent>> getRsEventByRange(@RequestParam(required = false) Integer start,
                                                           @RequestParam(required = false) Integer end) throws InvalidRequestParamException {
        try {
            final List<RsEvent> rsEventByRange = rsEventService.getRsEventByRange(start, end);
            return ResponseEntity.ok(rsEventByRange);
        } catch (Exception e) {
            throw new InvalidRequestParamException("invalid request param");
        }
    }

    @PostMapping("rs/event")
    public ResponseEntity addRsEvent(@Valid @RequestBody RsEvent rsEvent,
                                     BindingResult bindingResult) throws InvalidParamException {
        if (bindingResult.getAllErrors().size() > 0) {
            throw new InvalidParamException("invalid param");
        }

        return rsEventService.addRsEvent(rsEvent);
    }

    @PatchMapping("rs/event/{id}")
    public ResponseEntity updateRsEvent(@PathVariable int id,
                                        @RequestBody RsEvent rsEvent) {
        return rsEventService.updateRsEvent(id, rsEvent);
    }

    @DeleteMapping("rs/{id}")
    public ResponseEntity deleteRsEvent(@PathVariable int id) {
        rsEventService.deleteRsEvent(id);
        return ResponseEntity.ok().build();
    }
}
