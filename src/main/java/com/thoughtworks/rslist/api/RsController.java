package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
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
    private List<RsEvent> rsList = initRsList();

    private List<RsEvent> initRsList() {
        List<RsEvent> tempList = new ArrayList<>();
        tempList.add(new RsEvent("第一条事件", "无分类"));
        tempList.add(new RsEvent("第二条事件", "无分类"));
        tempList.add(new RsEvent("第三条事件", "无分类"));
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
    public void addRsEvent(@RequestBody String rsEventString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        RsEvent rsEvent = objectMapper.readValue(rsEventString, RsEvent.class);
        rsList.add(rsEvent);
    }

    @PutMapping("rs/modify/{index}")
    public void modifyRsEvent(@PathVariable int index,
                              @RequestBody RsEvent rsEvent) {
        if (rsEvent.getEventName().equals("")) {
            rsEvent.setEventName(rsList.get(index - 1).getKeywords());
        }
        if (rsEvent.getKeywords().equals("")) {
            rsEvent.setKeywords(rsList.get(index - 1).getKeywords());
        }
        rsList.set(index - 1, rsEvent);
    }

    @DeleteMapping("rs/delete/{index}")
    public void deleteRsEvent(@PathVariable int index) {
        rsList.remove(index - 1);
    }
}
