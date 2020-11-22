package com.joonho.sprinkle.controller;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joonho.sprinkle.controller.request.CreateSprinkleRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration
public class SprinkleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    // 뿌리기 API
    @Test void 뿌리기_생성에_성공하면_token을_반환한다() throws Exception {
        String toJsonString = objectMapper.writeValueAsString(new CreateSprinkleRequest(1000, 3));

        final ResultActions actions = mockMvc.perform(post("/api/sprinkles")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-ROOM-ID", "room")
                .header("X-USER-ID", 1L)
                .content(toJsonString));

        actions
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isString());
    }

    @Test void 뿌리기_생성에_Room_ID_헤더가_없으면_BadRequest를_발생시킨다() throws Exception {
        String toJsonString = objectMapper.writeValueAsString(new CreateSprinkleRequest(1000, 3));

        final ResultActions actions = mockMvc.perform(post("/api/sprinkles")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", 1L)
                .content(toJsonString));

        actions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test void 뿌리기_생성에_User_ID_헤더가_없으면_BadRequest를_발생시킨다() throws Exception {
        String toJsonString = objectMapper.writeValueAsString(new CreateSprinkleRequest(1000, 3));

        final ResultActions actions = mockMvc.perform(post("/api/sprinkles")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-ROOM-ID", "room")
                .content(toJsonString));

        actions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test void 뿌리기_생성에_amount가_없으면_BadRequest를_발생시킨다() throws Exception {
        final ResultActions actions = mockMvc.perform(post("/api/sprinkles")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-ROOM-ID", "room")
                .header("X-USER-ID", 1L)
                .content("{\"targetNumbers\":3}"));

        actions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test void targetNumbers가_없으면_BadRequest를_발생시킨다() throws Exception {
        final ResultActions actions = mockMvc.perform(post("/api/sprinkles")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-ROOM-ID", "room")
                .header("X-USER-ID", 1L)
                .content("{\"amount\":1000}"));

        actions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test void 받기에_성공하면_amount를_응답받는다() throws Exception {
        String roomId = "room";
        Long userId = 1L;
        Integer amount = 1000;
        Integer targetNumbers = 3;

        String token = call_create_api(roomId, userId, amount, targetNumbers);

        final ResultActions actions = mockMvc.perform(post("/api/sprinkles/" + token + "/receive")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-ROOM-ID", roomId)
                .header("X-USER-ID", 999L));

        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(amount/targetNumbers));
    }

    @Test void 받기에_Room_ID_헤더가_없으면_BadRequest를_발생시킨다() throws Exception {
        String token = "token";

        final ResultActions actions = mockMvc.perform(post("/api/sprinkles/" + token + "/receive")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", 1L));

        actions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test void 받기에_User_ID_헤더가_없으면_BadRequest를_발생시킨다() throws Exception {
        String token = "token";

        final ResultActions actions = mockMvc.perform(post("/api/sprinkles/" + token + "/receive")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-ROOM-ID", "room"));

        actions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test void 뿌리기_내역을_조회한다() throws Exception {
        String roomId = "room";
        Long userId = 1L;
        Long receiver = 2L;
        Integer amount = 1000;
        Integer targetNumbers = 3;

        String token = call_create_api(roomId, userId, amount, targetNumbers);
        call_receive_api(roomId, receiver, token);

        final ResultActions actions = mockMvc.perform(get("/api/sprinkles/" + token)
                .header("X-ROOM-ID", roomId)
                .header("X-USER-ID", userId));

        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAmount").value(amount))
                .andExpect(jsonPath("$.completeReceiveAmount").value(amount/targetNumbers))
                .andExpect(jsonPath("$.sprinkleTargets[0].receiveAmount").value(amount/targetNumbers))
                .andExpect(jsonPath("$.sprinkleTargets[0].receiver").value(receiver));
    }

    private String call_create_api(String roomId, Long userId, Integer amount, Integer targetNumbers) throws Exception {
        String toJsonString = objectMapper.writeValueAsString(new CreateSprinkleRequest(amount, targetNumbers));

        final ResultActions actions = mockMvc.perform(post("/api/sprinkles")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-ROOM-ID", roomId)
                .header("X-USER-ID", userId)
                .content(toJsonString));

        String content = actions.andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(content).get("token").textValue();
    }

    private void call_receive_api(String roomId, Long userId, String token) throws Exception {
        final ResultActions actions = mockMvc.perform(post("/api/sprinkles/" + token + "/receive")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-ROOM-ID", roomId)
                .header("X-USER-ID", userId));
    }
}
