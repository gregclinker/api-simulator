package com.essexboy.api;

import org.json.JSONException;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class KafkaTest {

    @Autowired
    private ConcreteKafkaClient kafkaClient;

    @BeforeClass
    public static void setUp() {
        System.setProperty("AVRO_SCHEMA", "target/test-classes/Payment.avsc");
    }

    @Test
    public void happyPath() {

        assertNotNull(kafkaClient.getSchema());

        Random random = new Random();
        final String key = UUID.randomUUID().toString();

        KafkaMessage kafkaMessage = new KafkaMessage();
        kafkaMessage.setTopic("generic5");
        kafkaMessage.setSchema("Payment");
        kafkaMessage.setKey(key);
        kafkaMessage.setMessage("{\"id\": \"" + key + "\", \"amount\": \"" + random.nextInt(1000) + "\"}");

        kafkaClient.write(kafkaMessage);
    }

    @Test(expected = RuntimeException.class)
    public void wrongSchema() {

        assertNotNull(kafkaClient.getSchema());

        Random random = new Random();
        final String key = UUID.randomUUID().toString();

        KafkaMessage kafkaMessage = new KafkaMessage();
        kafkaMessage.setTopic("generic5");
        kafkaMessage.setSchema("xxxx");
        kafkaMessage.setKey(key);
        kafkaMessage.setMessage("{\"id\": \"" + key + "\", \"amount\": \"" + random.nextInt(1000) + "\"}");

        kafkaClient.write(kafkaMessage);
    }

    @Test(expected = JSONException.class)
    public void wrongMessage() {

        assertNotNull(kafkaClient.getSchema());

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
