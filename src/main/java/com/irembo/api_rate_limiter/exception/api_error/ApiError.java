package com.irembo.api_rate_limiter.exception.api_error;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiError {


    @JsonProperty("status")
    private int httpStatusCode;
    private String message;

    public ApiError(int httpStatusCode, String message) {
        super();
        this.httpStatusCode = httpStatusCode;
        this.message = message;
    }

    public ApiError() {

    }


    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
