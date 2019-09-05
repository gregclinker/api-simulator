package com.essexboy.api;

import org.apache.avro.Schema;

public interface KafkaClient {
    void write(KafkaMessage kafkaMessage);

    Schema getSchema();
}
