# api-simulator

Spring Boot API simulator.

A REST API which can be configured, as an API, with any given request/response behaviour.

**To Build**
```shell script
mvn clean installl
```

**To run without Kafka integration**
```shell script
java -Dspring.profiles.active=noKafkavajar target/api-simulator-1.0.jar
```

**To get the default simulator set-up** - GET:http://localhost:8080/simulator

```json
[
  {
    "httpStatus": "OK",
    "requestBody": "{\"id\":3,\"name\":\"name\",\"description\":\"description\"}",
    "responseBody": "{\"id\":3,\"name\":\"name\",\"description\":\"description\"}",
    "httpMethod": "POST",
    "url": "/my-api/resource/3",
    "kafkaMessage": {
      "schema": "Payment",
      "topic": "payments-topic",
      "message": "{\"id\": \"6c5fb721-1616-47bf-a533-96f5f80d1299\", \"amount\": \"102\"}"
    }
  },
  {
    "httpStatus": "OK",
    "responseBody": "{\"id\":2,\"name\":\"name\",\"description\":\"description\"}",
    "httpMethod": "GET",
    "url": "/my-api/resource/2",
    "kafkaMessage": {
      "schema": "Payment",
      "topic": "payments-topic",
      "message": "{\"id\": \"3210722b-e782-4c81-9e5a-ff8d2e96940e\", \"amount\": \"102\"}"
    },
    "tryIt": "http://localhost:8080/my-api/resource/2"
  },
  {
    "httpStatus": "OK",
    "responseBody": "{\"id\":1,\"name\":\"name\",\"description\":\"description\"}",
    "httpMethod": "GET",
    "url": "/my-api/resource/1",
    "tryIt": "http://localhost:8080/my-api/resource/1"
  }
]
```

**Try your API** - GET:http://localhost:8080/my-api/resource/1

```json
{
  "id": 1,
  "name": "name",
  "description": "description"
}
```

**To update simulator set-up** - POST:http://localhost:8080/simulator

```json
[
  {
    "httpStatus": "OK",
    "responseBody": "{\"id\":3,\"name\":\"name\",\"description\":\"description\"}",
    "httpMethod": "GET",
    "url": "/my-api/resource/3"
  },
  {
    "httpStatus": "OK",
    "responseBody": "{\"id\":4,\"name\":\"name\",\"description\":\"description\"}",
    "httpMethod": "GET",
    "url": "/my-api/resource/4",
    "kafkaMessage": {
      "schema": "Payment",
      "topic": "payments-topic",
      "message": "{\"id\": \"c0fe74ee-a948-49a5-b901-f189f66c659e\", \"amount\": \"102\"}"
    }
  },
  {
    "httpStatus": "OK",
    "requestBody": "{\"id\":5,\"name\":\"name\",\"description\":\"description\"}",
    "responseBody": "{\"id\":5,\"name\":\"name\",\"description\":\"description\"}",
    "httpMethod": "POST",
    "url": "/my-api/resource/5",
    "kafkaMessage": {
      "schema": "Payment",
      "topic": "payments-topic",
      "message": "{\"id\": \"4a7ac8c7-9c30-45a2-8277-b026fc99bf44\", \"amount\": \"102\"}"
    }
  }
]
```

**To run with Kafka and an [AVRO Schema](https://avro.apache.org/docs/1.8.2/gettingstartedjava.html)**
```shell script
java -DAVRO_SCHEMA=target/test-classes/Payment.avsc -jar target/api-simulator-1.0.jar
```

Default config for Kafka is set in the Spring Boot application.properties

```
kafka.bootstrapServers=localhost:9092
kafka.schemaRegistryUrl=http://localhost:8081
```
A quick way to get a complete Kafka & Schema Registry up and running is to [run Confluent locally](https://docs.confluent.io/current/quickstart/ce-quickstart.html)  or [run Confluent with Docker](https://docs.confluent.io/current/quickstart/ce-docker-quickstart.html). This also gives you a nice front end that you can use to see Kafka messages.

