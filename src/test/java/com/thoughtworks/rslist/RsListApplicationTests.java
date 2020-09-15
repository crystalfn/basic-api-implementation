package com.thoughtworks.rslist;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RsListApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void should_get_one_rs_event() throws Exception {
        mockMvc.perform(get("/rs/1"))
            .andExpect(status().isOk())
            .andExpect(content().string("第一条事件"));

        mockMvc.perform(get("/rs/2"))
            .andExpect(status().isOk())
            .andExpect(content().string("第二条事件"));

        mockMvc.perform(get("/rs/3"))
            .andExpect(status().isOk())
            .andExpect(content().string("第三条事件"));
    }

    @Test
    void should_get_rs_event_by_range() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=3"))
            .andExpect(status().isOk())
            .andExpect(content().string("[第一条事件, 第二条事件, 第三条事件]"));
    }

    @Test
    void should_get_all_rs_event() throws Exception {
        mockMvc.perform(get("/rs/list"))
            .andExpect(status().isOk())
            .andExpect(content().string("[第一条事件, 第二条事件, 第三条事件]"));
    }

    @Test
    void should_add_one_rs_event() throws Exception {
        mockMvc.perform(get("/rs/list"))
            .andExpect(status().isOk())
            .andExpect(content().string("[第一条事件, 第二条事件, 第三条事件]"));

        mockMvc.perform(post("/rs/event").content("第四条事件").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        mockMvc.perform(get("/rs/list"))
            .andExpect(status().isOk())
            .andExpect(content().string("[第一条事件, 第二条事件, 第三条事件, 第四条事件]"));
    }
}
