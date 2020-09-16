package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    UserService userService;

    private List<RsEvent> rsList = initRsList();

    private List<RsEvent> initRsList() {
        List<RsEvent> tempList = new ArrayList<>();
        UserDto userDto = new UserDto("张三", 20, "male", "zhangSan@qq.com", "13155555555");
        tempList.add(new RsEvent("第一条事件", "无分类", userDto));
        tempList.add(new RsEvent("第二条事件", "无分类", userDto));
        tempList.add(new RsEvent("第三条事件", "无分类", userDto));
        return tempList;
    }

    @GetMapping("/rs/{index}")
    public RsEvent getOneRsEvent(@PathVariable int index) {
        return rsList.get(index - 1);
    }

    @GetMapping("rs/list")
    public List<RsEvent> getRsEventByRange(@RequestParam(required = false) Integer start,
                                           @RequestParam(required = false) Integer end) {
        if (start == null || end == null) {
            return rsList;
        }
        return rsList.subList(start - 1, end);
    }

    @PostMapping("rs/event")
    public void addRsEvent(@RequestBody RsEvent rsEvent) {
        rsList.add(rsEvent);

        for (UserDto userDto : userService.getUserDtoList()) {
            if (rsEvent.getUser().getUserName().equals(userDto.getUserName())) {
                return;
            }
        }

        userService.register(rsEvent.getUser());
    }

    @PutMapping("rs/modify/{index}")
    public void modifyRsEvent(@PathVariable int index,
                              @RequestBody RsEvent rsEvent) {
        RsEvent modifyRsEvent = rsList.get(index - 1);
        if (rsEvent.getEventName() != null) {
            modifyRsEvent.setEventName(rsEvent.getEventName());
        }
        if (rsEvent.getKeywords() != null) {
            modifyRsEvent.setKeywords(rsEvent.getKeywords());
        }
    }

    @DeleteMapping("rs/delete/{index}")
    public void deleteRsEvent(@PathVariable int index) {
        rsList.remove(index - 1);
    }
}
