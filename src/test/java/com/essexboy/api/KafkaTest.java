package com.essexboy.api;

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.avro.Schema;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.json.JSONException;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class KafkaTest {

    @Value(value = "${kafka.bootstrapServers}")
    private String bootstrapServers;

    @Value(value = "${kafka.schemaRegistryUrl}")
    private String schemaRegistryUrl;

    @Autowired
    private KafkaClient kafkaClient;

    @BeforeClass
    public static void setUp() {
        System.setProperty("AVRO_SCHEMA_FILE", "target/test-classes/payments.avsc");
    }

    @Test
    public void happyPath() throws Exception {

        Random random = new Random();
        final String key = UUID.randomUUID().toString();

        KafkaMessage kafkaMessage = new KafkaMessage();
        kafkaMessage.setTopic("payments");
        kafkaMessage.setSchema("payments");
        kafkaMessage.setKey(key);
        kafkaMessage.setMessage("{\"id\": 123456789, \"accountId\": \"123456\", \"sortCode\": \"10-11-12\", \"amount\": \"10.13\"}");

        kafkaClient.write(kafkaMessage);
    }

    @Test(expected = RuntimeException.class)
    public void wrongSchema() throws Exception {

        Random random = new Random();
        final String key = UUID.randomUUID().toString();

        KafkaMessage kafkaMessage = new KafkaMessage();
        kafkaMessage.setTopic("generic5");
        kafkaMessage.setSchema("xxxx");
        kafkaMessage.setKey(key);
        kafkaMessage.setMessage("{\"id\": \"" + key + "\", \"amount\": \"" + random.nextInt(1000) + "\"}");

        kafkaClient.write(kafkaMessage);
    }

    @Test(expected = RuntimeException.class)
    public void wrongMessage() throws Exception {

        Random random = new Random();
        final String key = UUID.randomUUID().toString();

        KafkaMessage kafkaMessage = new KafkaMessage();
        kafkaMessage.setTopic("generic5");
        kafkaMessage.setSchema("Payment");
        kafkaMessage.setKey(key);
        kafkaMessage.setMessage("{\"id\": \"" + key + "\", \"xxxx\": \"" + random.nextInt(1000) + "\"}");

        kafkaClient.write(kafkaMessage);
    }
}
