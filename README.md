# api-simulator

Spring Boot API simulator.

A REST API which can be configured, as an API, with any given request/response behaviour.

**To Build** - mvn clean installl

**To run** - java -jar target/api-simulator-1.0.jar

**To get the default simulator set-up** - GET:http://localhost:8080/simulator

```json
[
  {
    "httpStatus": "OK",
    "responseBody": "{\"id\":3,\"name\":\"name\",\"description\":\"description\"}",
    "httpMethod": "POST",
    "url": "/my-api/resource/3"
  },
  {
    "httpStatus": "OK",
    "responseBody": "{\"id\":2,\"name\":\"name\",\"description\":\"description\"}",
    "httpMethod": "GET",
    "url": "/my-api/resource/2",
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
    "responseBody": "{\"id\":4,\"name\":\"name\",\"description\":\"description\"}",
    "httpMethod": "GET",
    "url": "/my-api/resource/5"
  },
  {
    "httpStatus": "OK",
    "responseBody": "{\"id\":6,\"name\":\"name\",\"description\":\"description\"}",
    "httpMethod": "GET",
    "url": "/my-api/resource/6"
  },
  {
    "httpStatus": "OK",
    "requestBody": "{\"id\":4,\"name\":\"name\",\"description\":\"description\"}",
    "responseBody": "{\"id\":4,\"name\":\"name\",\"description\":\"description\"}",
    "httpMethod": "POST",
    "url": "/my-api/resource/4"
  }
]
```

