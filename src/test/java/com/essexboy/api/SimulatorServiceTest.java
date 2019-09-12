package com.essexboy.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("noKafka")
public class SimulatorServiceTest {

    @Autowired
    private SimulatorService simulatorService;

    @Test
    public void init() {
        final List<SimulatedHttpRequest> simulatedHttpRequests = simulatorService.get();
        assertEquals(4, simulatedHttpRequests.size());
    }

    @Test
    public void getKSQL() {
        final SimulatedHttpRequest simulatedHttpRequest = simulatorService.get("GET:/my-api/payment");
        assertNotNull(simulatedHttpRequest);
        System.out.println(simulatedHttpRequest);
    }
}