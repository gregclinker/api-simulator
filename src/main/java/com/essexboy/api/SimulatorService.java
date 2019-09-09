package com.essexboy.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.getProperty;
import static java.nio.charset.Charset.defaultCharset;

@Service
public class SimulatorService {

    Logger logger = LoggerFactory.getLogger(SimulatorService.class);
    private Map<String, SimulatedHttpRequest> simulatedHttpRequestMap = new HashMap<>();
    @Autowired
    private KafkaClient kafkaClient;

    /**
     * Set up a simple example simulator
     */
    @PostConstruct
    public void init() throws Exception {
        try {
            InputStream inputStream = null;
            if (getProperty(String.valueOf(SystemParameters.DEFINITION_FILE)) != null) {
                inputStream = new FileInputStream(new File(System.getProperty(String.valueOf(SystemParameters.DEFINITION_FILE))));
            } else {
                inputStream = getClass().getResourceAsStream("/simulatorDef.json");
            }
            post(IOUtils.toString(inputStream, defaultCharset()));
            inputStream.close();
        } catch (Exception e) {
            logger.error("error loading definition", e);
            throw e;
        }
    }

    public SimulatedHttpRequest get(String key) {
        logger.debug("looking up API for key: " + key);
        return simulatedHttpRequestMap.get(key);
    }

    public ArrayList<SimulatedHttpRequest> get() {
        ArrayList<SimulatedHttpRequest> simulatedHttpRequests = new ArrayList<>();
        for (String key : simulatedHttpRequestMap.keySet()) {
            simulatedHttpRequests.add(simulatedHttpRequestMap.get(key));
        }
        return simulatedHttpRequests;
    }

    public List<SimulatedHttpRequest> post(String json) throws IOException {
        List<SimulatedHttpRequest> simulatedHttpRequests = new ObjectMapper().readValue(json, new TypeReference<List<SimulatedHttpRequest>>() {
        });

        simulatedHttpRequestMap = new HashMap<>();
        for (SimulatedHttpRequest simulatedHttpRequest : simulatedHttpRequests) {
            simulatedHttpRequestMap.put(simulatedHttpRequest.getKey(), simulatedHttpRequest);
        }
        return simulatedHttpRequests;
    }

    enum SystemParameters {
        DEFINITION_FILE,
        AVRO_SCHEMA_FILE
    }
}
