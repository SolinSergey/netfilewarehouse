package ru.gb.cloudmessages;

import java.io.Serializable;

public class BasicResponse implements Serializable {
    private final String errorMessage;
    private final String authToken;

    public String getErrorMessage() {
        return errorMessage;
    }

    public BasicResponse(String errorMessage, String authToken) {
        this.errorMessage = errorMessage;
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }
}