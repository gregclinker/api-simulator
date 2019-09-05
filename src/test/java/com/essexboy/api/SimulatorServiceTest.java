package com.essexboy.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("noKafka")
public class SimulatorServiceTest {

    @Autowired
    private SimulatorService simulatorService;

    @Test
    public void init() {
        final List<SimulatedHttpRequest> simulatedHttpRequests = simulatorService.get();
        assertEquals(3, simulatedHttpRequests.size());
    }
}