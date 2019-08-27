package com.essexboy.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Getter
@Setter
@NoArgsConstructor
@ToString
@ApiModel(value = "SimulatedHttpRequest")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimulatedHttpRequest {

    @Autowired
    @JsonIgnore
    private Environment environment;

    private HttpStatus httpStatus;
    private String requestBody;
    private String example;
    private String responseBody;
    private HttpMethod httpMethod;
    private String url;

    @JsonIgnore
    public String getKey() throws IOException {
        return getKey(httpMethod, url, requestBody);
    }

    public String getExample() {
        if (httpMethod == HttpMethod.GET) {
            return "http://localhost:8080" + url;
        }
        return null;
    }

    public static String getKey(HttpServletRequest request) throws IOException {
        if (request.getReader() != null) {
            return getKey(HttpMethod.valueOf(request.getMethod()), request.getRequestURI(), IOUtils.toString(request.getReader()));
        }
        return getKey(HttpMethod.valueOf(request.getMethod()), request.getRequestURI(), null);
    }

    public static String getKey(HttpMethod httpMethod, String url, String jsonBody) throws IOException {
        if (jsonBody != null && !jsonBody.isEmpty()) {
            return httpMethod + ":" + url + ":" + prettyJson(jsonBody);
        }
        return httpMethod + ":" + url;
    }

    private static String prettyJson(String json) throws IOException {
        if (json.isEmpty()) {
            return json;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        Object o = objectMapper.readValue(json, Object.class);
        return objectMapper.writeValueAsString(o);
    }
}
