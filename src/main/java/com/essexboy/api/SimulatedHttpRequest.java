package com.essexboy.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.net.UnknownHostException;

@Getter
@Setter
@NoArgsConstructor
@ToString
@ApiModel(value = "SimulatedHttpRequest")
public class SimulatedHttpRequest {

    @Autowired
    private Environment environment;

    private HttpStatus httpStatus;
    private String requestBody;
    private String responseBody;
    private HttpMethod httpMethod;
    private String url;

    @JsonIgnore
    public String getKey() {
        return getKey(httpMethod, url);
    }

    public String getExample() throws UnknownHostException {
        return "http://localhost:8080" + url;
    }

    public static String getKey(HttpMethod httpMethod, String url) {
        return httpMethod + ":" + url;
    }
}
