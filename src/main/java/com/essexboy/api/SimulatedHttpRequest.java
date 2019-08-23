package com.essexboy.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "SimulatedHttpRequest")
public class SimulatedHttpRequest {

    private HttpStatus httpStatus;
    private String responseBody;
    private HttpMethod httpMethod;
    private String url;

    @JsonIgnore
    public String getKey() {
        return getKey(httpMethod, url);
    }

    public static String getKey(HttpMethod httpMethod, String url) {
        return httpMethod + ":" + url;
    }
}
