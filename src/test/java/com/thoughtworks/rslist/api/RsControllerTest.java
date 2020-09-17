package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void should_get_one_rs_event() throws Exception {
        mockMvc.perform(get("/rs/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.eventName", is("第一条事件")))
            .andExpect(jsonPath("$.keywords", is("无分类")))
            .andExpect(jsonPath("$.user").doesNotExist());
    }

    @Test
    void should_get_rs_event_by_range() throws Exception {
        mockMvc.perform(get("/rs/event?start=1&end=3"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
            .andExpect(jsonPath("$[0].keywords", is("无分类")))
            .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
            .andExpect(jsonPath("$[1].keywords", is("无分类")))
            .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
            .andExpect(jsonPath("$[2].keywords", is("无分类")));
    }

    @Test
    void should_get_all_rs_event() throws Exception {
        mockMvc.perform(get("/rs/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
            .andExpect(jsonPath("$[0].keywords", is("无分类")))
            .andExpect(jsonPath("$[0].user").doesNotExist())
            .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
            .andExpect(jsonPath("$[1].keywords", is("无分类")))
            .andExpect(jsonPath("$[1].user").doesNotExist())
            .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
            .andExpect(jsonPath("$[2].keywords", is("无分类")))
            .andExpect(jsonPath("$[2].user").doesNotExist());
    }

    @Test
    void should_add_one_rs_event_when_user_exit() throws Exception {
        mockMvc.perform(get("/rs/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
            .andExpect(jsonPath("$[0].keywords", is("无分类")))
            .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
            .andExpect(jsonPath("$[1].keywords", is("无分类")))
            .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
            .andExpect(jsonPath("$[2].keywords", is("无分类")));

        mockMvc.perform(get("/user/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].user_name", is("张三")))
            .andExpect(jsonPath("$[0].user_age", is(20)))
            .andExpect(jsonPath("$[0].user_gender", is("male")))
            .andExpect(jsonPath("$[0].user_email", is("zhangSan@qq.com")))
            .andExpect(jsonPath("$[0].user_phone", is("13155555555")));

        UserDto userDto = new UserDto("张三", 20, "male", "zhangSan@qq.com", "13155555555");
        RsEvent addRsEvent = new RsEvent("第四条事件", "无分类", userDto);
        ObjectMapper objectMapper = new ObjectMapper();
        final String addRsEventJson = objectMapper.writeValueAsString(addRsEvent);

        mockMvc.perform(post("/rs/addEvent")
            .content(addRsEventJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().string("index", String.valueOf(4)));

        mockMvc.perform(get("/rs/event"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(4)))
            .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
            .andExpect(jsonPath("$[0].keywords", is("无分类")))
            .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
            .andExpect(jsonPath("$[1].keywords", is("无分类")))
            .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
            .andExpect(jsonPath("$[2].keywords", is("无分类")))
            .andExpect(jsonPath("$[3].eventName", is("第四条事件")))
            .andExpect(jsonPath("$[3].keywords", is("无分类")))
            .andExpect(jsonPath("$[3].user.user_name", is("张三")))
            .andExpect(jsonPath("$[3].user.user_age", is(20)))
            .andExpect(jsonPath("$[3].user.user_gender", is("male")))
            .andExpect(jsonPath("$[3].user.user_email", is("zhangSan@qq.com")))
            .andExpect(jsonPath("$[3].user.user_phone", is("13155555555")));

        mockMvc.perform(get("/user/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].user_name", is("张三")))
            .andExpect(jsonPath("$[0].user_age", is(20)))
            .andExpect(jsonPath("$[0].user_gender", is("male")))
            .andExpect(jsonPath("$[0].user_email", is("zhangSan@qq.com")))
            .andExpect(jsonPath("$[0].user_phone", is("13155555555")));
    }

    @Test
    void should_add_one_rs_event_when_user_not_exit() throws Exception {
        mockMvc.perform(get("/rs/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
            .andExpect(jsonPath("$[0].keywords", is("无分类")))
            .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
            .andExpect(jsonPath("$[1].keywords", is("无分类")))
            .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
            .andExpect(jsonPath("$[2].keywords", is("无分类")));

        mockMvc.perform(get("/user/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].user_name", is("张三")))
            .andExpect(jsonPath("$[0].user_age", is(20)))
            .andExpect(jsonPath("$[0].user_gender", is("male")))
            .andExpect(jsonPath("$[0].user_email", is("zhangSan@qq.com")))
            .andExpect(jsonPath("$[0].user_phone", is("13155555555")));

        UserDto userDto = new UserDto("李四", 20, "male", "zhangSan@qq.com", "13155555555");
        RsEvent addRsEvent = new RsEvent("第四条事件", "无分类", userDto);
        ObjectMapper objectMapper = new ObjectMapper();
        final String addRsEventJson = objectMapper.writeValueAsString(addRsEvent);

        mockMvc.perform(post("/rs/addEvent")
            .content(addRsEventJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().string("index", String.valueOf(4)));

        mockMvc.perform(get("/rs/event"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(4)))
            .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
            .andExpect(jsonPath("$[0].keywords", is("无分类")))
            .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
            .andExpect(jsonPath("$[1].keywords", is("无分类")))
            .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
            .andExpect(jsonPath("$[2].keywords", is("无分类")))
            .andExpect(jsonPath("$[3].eventName", is("第四条事件")))
            .andExpect(jsonPath("$[3].keywords", is("无分类")))
            .andExpect(jsonPath("$[3].user.user_name", is("李四")))
            .andExpect(jsonPath("$[3].user.user_age", is(20)))
            .andExpect(jsonPath("$[3].user.user_gender", is("male")))
            .andExpect(jsonPath("$[3].user.user_email", is("zhangSan@qq.com")))
            .andExpect(jsonPath("$[3].user.user_phone", is("13155555555")));

        mockMvc.perform(get("/user/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].user_name", is("张三")))
            .andExpect(jsonPath("$[0].user_age", is(20)))
            .andExpect(jsonPath("$[0].user_gender", is("male")))
            .andExpect(jsonPath("$[0].user_email", is("zhangSan@qq.com")))
            .andExpect(jsonPath("$[0].user_phone", is("13155555555")))
            .andExpect(jsonPath("$[1].user_name", is("李四")))
            .andExpect(jsonPath("$[1].user_age", is(20)))
            .andExpect(jsonPath("$[1].user_gender", is("male")))
            .andExpect(jsonPath("$[1].user_email", is("zhangSan@qq.com")))
            .andExpect(jsonPath("$[1].user_phone", is("13155555555")));
    }

    @Test
    void should_modify_rs_event_message_when_keywords_is_null() throws Exception {
        mockMvc.perform(get("/rs/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
            .andExpect(jsonPath("$[0].keywords", is("无分类")));

        RsEvent rsEvent = new RsEvent("这是一条被修改的事件", null);
        ObjectMapper objectMapper = new ObjectMapper();
        final String modifyRsEvent = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(put("/rs/modify/1")
            .content(modifyRsEvent)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        mockMvc.perform(get("/rs/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].eventName", is("这是一条被修改的事件")))
            .andExpect(jsonPath("$[0].keywords", is("无分类")));
    }

    @Test
    void should_modify_rs_event_message_when_eventName_is_null() throws Exception {
        mockMvc.perform(get("/rs/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
            .andExpect(jsonPath("$[0].keywords", is("无分类")));

        RsEvent rsEvent = new RsEvent(null, "被修改的分类");
        ObjectMapper objectMapper = new ObjectMapper();
        final String modifyRsEvent = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(put("/rs/modify/1")
            .content(modifyRsEvent)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        mockMvc.perform(get("/rs/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
            .andExpect(jsonPath("$[0].keywords", is("被修改的分类")));
    }

    @Test
    void should_modify_rs_event_message_when_both_not_null() throws Exception {
        mockMvc.perform(get("/rs/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
            .andExpect(jsonPath("$[0].keywords", is("无分类")));

        RsEvent rsEvent = new RsEvent("这是一条被修改的事件", "被修改的分类");
        ObjectMapper objectMapper = new ObjectMapper();
        final String modifyRsEvent = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(put("/rs/modify/1")
            .content(modifyRsEvent)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        mockMvc.perform(get("/rs/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].eventName", is("这是一条被修改的事件")))
            .andExpect(jsonPath("$[0].keywords", is("被修改的分类")));
    }

    @Test
    void should_delete_rs_event() throws Exception {
        mockMvc.perform(get("/rs/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
            .andExpect(jsonPath("$[0].keywords", is("无分类")))
            .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
            .andExpect(jsonPath("$[1].keywords", is("无分类")))
            .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
            .andExpect(jsonPath("$[2].keywords", is("无分类")));

        mockMvc.perform(delete("/rs/delete/2"))
            .andExpect(status().isOk());

        mockMvc.perform(get("/rs/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
            .andExpect(jsonPath("$[0].keywords", is("无分类")))
            .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
            .andExpect(jsonPath("$[1].keywords", is("无分类")));
    }
}