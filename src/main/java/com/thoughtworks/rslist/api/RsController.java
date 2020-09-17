package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {

    final
    UserRepository userRepository;

    final
    RsEventRepository rsEventRepository;

    final
    UserService userService;

    private final List<RsEvent> rsList = initRsList();

    public RsController(UserService userService, UserRepository userRepository, RsEventRepository rsEventRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.rsEventRepository = rsEventRepository;
    }

    private List<RsEvent> initRsList() {
        List<RsEvent> tempList = new ArrayList<>();
//        UserDto userDto = new UserDto("张三", 20, "male", "zhangSan@qq.com", "13155555555");
//        tempList.add(new RsEvent("第一条事件", "无分类", userDto));
//        tempList.add(new RsEvent("第二条事件", "无分类", userDto));
//        tempList.add(new RsEvent("第三条事件", "无分类", userDto));
        return tempList;
    }

    @JsonView(RsEvent.WithoutUser.class)
    @GetMapping("/rs/{index}")
    public ResponseEntity<RsEvent> getOneRsEvent(@PathVariable int index) {
        return ResponseEntity.ok(rsList.get(index - 1));
    }

    @JsonView(RsEvent.WithoutUser.class)
    @GetMapping("rs/list")
    public ResponseEntity<List<RsEvent>> getAllEvents() {
        return ResponseEntity.ok(rsList);
    }

    @GetMapping("rs/event")
    public ResponseEntity<List<RsEvent>> getRsEventByRange(@RequestParam(required = false) Integer start,
                                                           @RequestParam(required = false) Integer end) {
        if (start == null || end == null) {
            return ResponseEntity.ok(rsList);
        }
        return ResponseEntity.ok(rsList.subList(start - 1, end));
    }

    @PostMapping("rs/addEvent")
    public ResponseEntity addRsEvent(@RequestBody RsEvent rsEvent) {
        RsEventEntity rsEventEntity = RsEventEntity.builder()
            .eventName(rsEvent.getEventName())
            .keywords(rsEvent.getKeywords())
            .userId(rsEvent.getUserId())
            .build();
        rsEventRepository.save(rsEventEntity);
        return ResponseEntity.created(null).build();
    }

    @PutMapping("rs/modify/{index}")
    public ResponseEntity modifyRsEvent(@PathVariable int index,
                                        @RequestBody RsEvent rsEvent) {
        RsEvent modifyRsEvent = rsList.get(index - 1);
        if (rsEvent.getEventName() != null) {
            modifyRsEvent.setEventName(rsEvent.getEventName());
        }
        if (rsEvent.getKeywords() != null) {
            modifyRsEvent.setKeywords(rsEvent.getKeywords());
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("rs/delete/{index}")
    public ResponseEntity deleteRsEvent(@PathVariable int index) {
        rsList.remove(index - 1);
        return ResponseEntity.ok().build();
    }
}
