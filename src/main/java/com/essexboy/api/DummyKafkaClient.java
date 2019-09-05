package com.essexboy.api;

import org.apache.avro.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("noKafka")
public class DummyKafkaClient implements KafkaClient {

    Logger logger = LoggerFactory.getLogger(ConcreteKafkaClient.class);

    @Override
    public void write(KafkaMessage kafkaMessage) {
        logger.debug("dummy Kafka client write " + kafkaMessage);
    }

    @Override
    public Schema getSchema() {
        return null;
    }
}
