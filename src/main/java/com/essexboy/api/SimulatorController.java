package com.essexboy.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.essexboy.api.SimulatedHttpRequest.getKey;

@RestController
public class SimulatorController {

    Logger logger = LoggerFactory.getLogger(SimulatorController.class);

    @Autowired
    private KafkaClient kafkaClient;

    @Autowired
    private SimulatorService simulatorService;

    @RequestMapping("/**")
    public ResponseEntity<String> simulate(HttpServletRequest request) throws IOException {

        String key = getKey(request);
        final SimulatedHttpRequest simulatedHttpRequest = simulatorService.get(key);

        if (simulatedHttpRequest == null) {
            return new ResponseEntity<>("key " + key + " not found", HttpStatus.BAD_REQUEST);
        }

        if (simulatedHttpRequest.getKafkaMessage() != null) {
            final KafkaMessage kafkaMessage = simulatedHttpRequest.getKafkaMessage().toBuilder().build();
            if (kafkaMessage.getKey() == null) {
                kafkaMessage.setKey(UUID.randomUUID().toString());
            }
            kafkaClient.write(kafkaMessage);
        }

        return new ResponseEntity<>(simulatedHttpRequest.getResponseBody(), simulatedHttpRequest.getHttpStatus());
    }

    @RequestMapping(value = "/simulator", method = RequestMethod.GET)
    public ResponseEntity<List<SimulatedHttpRequest>> get() throws JsonProcessingException {
        return new ResponseEntity<>(simulatorService.get(), HttpStatus.OK);
    }

    @RequestMapping(value = "/simulator", method = RequestMethod.POST)
    public ResponseEntity<List<SimulatedHttpRequest>> post(@RequestBody String request) throws IOException {
        return new ResponseEntity<>(simulatorService.post(request), HttpStatus.OK);
    }

}
