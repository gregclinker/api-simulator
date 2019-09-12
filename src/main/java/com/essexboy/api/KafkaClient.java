package com.essexboy.api;

import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import org.apache.avro.Schema;

import java.io.IOException;

public interface KafkaClient {
    void write(KafkaMessage kafkaMessage) throws Exception;
}
