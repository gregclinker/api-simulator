package com.essexboy.api;

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.commons.io.IOUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Properties;

@Service
@Profile("!noKafka")
public class ConcreteKafkaClient implements KafkaClient {

    Logger logger = LoggerFactory.getLogger(ConcreteKafkaClient.class);

    @Value(value = "${kafka.bootstrapServers}")
    private String bootstrapServers;

    @Value(value = "${kafka.schemaRegistryUrl}")
    private String schemaRegistryUrl;

    private Producer<String, GenericRecord> producer;
    private Schema schema;
    private boolean silent;

    @PostConstruct
    public void init() {

        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        producerProps.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        producerProps.put(ProducerConfig.ACKS_CONFIG, "0");
        producerProps.put(ProducerConfig.RETRIES_CONFIG, "0");

        producer = new KafkaProducer<>(producerProps);

        if (System.getProperty("AVRO_SCHEMA") == null) {
            return;
        }

        try {
            InputStream inputStream = new FileInputStream(new File(System.getProperty("AVRO_SCHEMA")));
            final String schemaJson = IOUtils.toString(inputStream, Charset.defaultCharset());

            schema = new Schema.Parser().parse(schemaJson);
            CachedSchemaRegistryClient client = new CachedSchemaRegistryClient(schemaRegistryUrl, 20);
            client.register(this.schema.getName() + "-topic", this.schema);
        } catch (Exception e) {
            logger.error("error initializing Kafka", e);
        }
    }

    @Override
    public void write(KafkaMessage kafkaMessage) {

        try {
            if (schema == null) {
                throw new RuntimeException("no AVRO schema specified, try starting with -Dspring.profiles.active=noKafka, or specify a schema file with -DAVRO_SCHEMA=Payment.avsc");
            }
            if (!kafkaMessage.getSchema().equals(schema.getName())) {
                throw new RuntimeException("invalid schema, found " + schema.getName() + ", expected " + kafkaMessage.getSchema());
            }
            JSONObject jsonObject = new JSONObject(kafkaMessage.getMessage());
            GenericData.Record record = new GenericData.Record(schema);
            for (Schema.Field field : schema.getFields()) {
                switch (field.schema().getType()) {
                    case STRING:
                        record.put(field.name(), jsonObject.get(field.name()).toString());
                        break;
                    case INT:
                        record.put(field.name(), Integer.parseInt(jsonObject.get(field.name()).toString()));
                        break;
                    case LONG:
                        record.put(field.name(), Long.parseLong(jsonObject.get(field.name()).toString()));
                        break;
                    case FLOAT:
                        record.put(field.name(), Float.parseFloat(jsonObject.get(field.name()).toString()));
                        break;
                    case DOUBLE:
                        record.put(field.name(), Double.parseDouble(jsonObject.get(field.name()).toString()));
                        break;
                    default:
                        throw new RuntimeException("type " + field.schema().getType() + " not supported");
                }
            }
            producer.send(new ProducerRecord<>(kafkaMessage.getTopic(), kafkaMessage.getKey(), record));
            producer.flush();
            logger.debug("Kafka client write " + kafkaMessage + " to bootstrap " + bootstrapServers);
            //producer.close();
        } catch (Exception e) {
            logger.error("error writing to topic " + kafkaMessage, e);
            throw e;
        }
    }

    @Override
    public Schema getSchema() {
        return schema;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }
}