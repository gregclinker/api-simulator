package com.essexboy.api;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("noKafka")
public class SimulatorControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void simulatorGetData() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/simulator").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("/my-api/resource/1")));
    }

    @Test
    public void simulatorPostData() throws Exception {
        String jsonBody = IOUtils.toString(getClass().getResourceAsStream("/simulatorDefNew.json"), StandardCharsets.UTF_8.name());
        mvc.perform(MockMvcRequestBuilders.post("/simulator").accept(MediaType.APPLICATION_JSON).content(jsonBody))
                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders.get("/my-api/resource/3").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"id\":3,\"name\":\"name\",\"description\":\"description\"}")));

        mvc.perform(MockMvcRequestBuilders.post("/my-api/resource/5")
                .content("{\"id\":5 ,\"name\" :\"name\",\"description\":\"description\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"id\":5,\"name\":\"name\",\"description\":\"description\"}")));
    }

    @Test
    public void simulateBadUrl() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/xxxx").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(equalTo("key GET:/xxxx not found")));
    }

    @Test
    public void simulate() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/my-api/resource/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"id\":1,\"name\":\"name\",\"description\":\"description\"}")));

        mvc.perform(MockMvcRequestBuilders.get("/my-api/resource/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"id\":2,\"name\":\"name\",\"description\":\"description\"}")));
    }
}
