package com.essexboy.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.essexboy.api.SimulatedHttpRequest.getKey;

@RestController
@Api(value = "/simulator", consumes = "application/json", produces = "application/json")
public class SimulatorController {

    Logger logger = LoggerFactory.getLogger(SimulatorController.class);

    private Map<String, SimulatedHttpRequest> simulatedHttpRequestMap = new HashMap<>();

    /**
     * Set up a simple example simulator
     */
    @PostConstruct
    public void init() throws IOException {
        makeSimulatedHttpRequest(HttpMethod.GET, "/my-api/resource/1", null, HttpStatus.OK, makeExampleResource(1l, "name", "description"));
        makeSimulatedHttpRequest(HttpMethod.GET, "/my-api/resource/2", null, HttpStatus.OK, makeExampleResource(2l, "name", "description"));
        makeSimulatedHttpRequest(HttpMethod.POST, "/my-api/resource/3", "{\"id\":3,\"name\":\"name\",\"description\":\"description\"}", HttpStatus.OK, makeExampleResource(3l, "name", "description"));
    }

    @RequestMapping("/**")
    public ResponseEntity<String> simulate(HttpServletRequest request) throws IOException {

        String key = getKey(request);

        logger.debug("looking up API for key: " + key);

        if (simulatedHttpRequestMap.get(key) == null) {
            return new ResponseEntity<>("key " + key + " not found", HttpStatus.BAD_REQUEST);
        }
        SimulatedHttpRequest simulatedHttpRequest = simulatedHttpRequestMap.get(key);

        return new ResponseEntity<>(simulatedHttpRequest.getResponseBody(), simulatedHttpRequest.getHttpStatus());
    }

    @ApiOperation(value = "Gets the configuration of this simulator",
            notes = "Multiple status values can be provided with comma seperated strings",
            response = List.class,
            responseContainer = "List")
    @RequestMapping(value = "/simulator", method = RequestMethod.GET)
    public ResponseEntity<List<SimulatedHttpRequest>> get() throws JsonProcessingException {
        ArrayList<SimulatedHttpRequest> simulatedHttpRequests = new ArrayList<>();
        for (String key : simulatedHttpRequestMap.keySet()) {
            simulatedHttpRequests.add(simulatedHttpRequestMap.get(key));
        }
        return new ResponseEntity<>(simulatedHttpRequests, HttpStatus.OK);
    }

    @ApiOperation(value = "Updates the configuration of this simulator",
            notes = "Multiple status values can be provided with comma seperated strings",
            response = List.class,
            responseContainer = "List")
    @RequestMapping(value = "/simulator", method = RequestMethod.POST)
    public ResponseEntity<List<SimulatedHttpRequest>> post(@RequestBody String request) throws IOException {

        List<SimulatedHttpRequest> simulatedHttpRequests = new ObjectMapper().readValue(request, new TypeReference<List<SimulatedHttpRequest>>() {
        });

        simulatedHttpRequestMap = new HashMap<>();
        for (SimulatedHttpRequest simulatedHttpRequest : simulatedHttpRequests) {
            simulatedHttpRequestMap.put(simulatedHttpRequest.getKey(), simulatedHttpRequest);
        }
        return new ResponseEntity<>(simulatedHttpRequests, HttpStatus.OK);
    }

    private void makeSimulatedHttpRequest(HttpMethod httpMethod, String url, String requestBody, HttpStatus httpStatus, String responseBody) throws IOException {
        SimulatedHttpRequest simulatedHttpRequest = new SimulatedHttpRequest();
        simulatedHttpRequest.setHttpMethod(httpMethod);
        simulatedHttpRequest.setUrl(url);
        simulatedHttpRequest.setHttpStatus(httpStatus);
        simulatedHttpRequest.setResponseBody(responseBody);

        simulatedHttpRequestMap.put(simulatedHttpRequest.getKey(), simulatedHttpRequest);
    }

    private String makeExampleResource(Long id, String name, String description) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(new ExampleResource(id, name, description));
    }

}
