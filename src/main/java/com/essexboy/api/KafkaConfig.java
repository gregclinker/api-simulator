package com.essexboy.api;

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.commons.io.IOUtils;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

//@Configuration
public class KafkaConfig {

    @Value(value = "${kafka.bootstrapServers}")
    private String bootstrapServers;

    @Value(value = "${kafka.schemaRegistryUrl}")
    private String schemaRegistryUrl;

    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        System.out.println("\nxxx=" + bootstrapServers + "\n");
        return new KafkaAdmin(configs);
    }

    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    //@Bean
    public Producer<String, GenericRecord> producer() {

        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        producerProps.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        producerProps.put(ProducerConfig.ACKS_CONFIG, "0");
        producerProps.put(ProducerConfig.RETRIES_CONFIG, "0");

        Producer<String, GenericRecord> producer = new KafkaProducer<>(producerProps);

        return producer;
    }

    //@Bean
    public Schema avroSchema() throws IOException, RestClientException {

        if (System.getProperty("AVRO_SCHEMA") == null) {
            return null;
        }

        InputStream inputStream = new FileInputStream(new File(System.getProperty("AVRO_SCHEMA")));
        final String schema = IOUtils.toString(inputStream, Charset.defaultCharset());

        Schema avroSchema = new Schema.Parser().parse(schema);
        CachedSchemaRegistryClient client = new CachedSchemaRegistryClient(schemaRegistryUrl, 20);
        client.register(avroSchema.getName() + "-topic", avroSchema);

        return avroSchema;
    }
}



