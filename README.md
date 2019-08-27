# api-simulator

Spring Boot API simulator

**To Build** - mvn clean installl

**To run** - java -jar target/api-simulator-1.0.jar

**To get the default simulator set-up** - GET:http://localhost:8080/simulator

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
    "url": "/my-api/resource/4"
  },
  {
    "httpStatus": "OK",
    "requestBody": "{\"id\":5,\"name\":\"name\",\"description\":\"description\"}",
    "responseBody": "{\"id\":5,\"name\":\"name\",\"description\":\"description\"}",
    "httpMethod": "POST",
    "url": "/my-api/resource/5"
  }
]
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
    "url": "/my-api/resource/4"
  }
]
```

