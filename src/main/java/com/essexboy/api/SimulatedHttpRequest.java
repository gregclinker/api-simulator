package com.essexboy.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SimulatedHttpRequest {

    private HttpStatus httpStatus;
    private String responseBody;
    private HttpMethod httpMethod;
    private String url;

    public String getKey() {
        return getKey(httpMethod, url);
    }

    public static String getKey(HttpMethod httpMethod, String url) {
        return httpMethod + ":" + url;
    }
}
