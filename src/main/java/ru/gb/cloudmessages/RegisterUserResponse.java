package ru.gb.cloudmessages;

import java.io.Serializable;

public class RegisterUserResponse extends BasicResponse {

    private final String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public RegisterUserResponse(String errorMessage, String authToken) {
        super(errorMessage,authToken);
        this.errorMessage = errorMessage;
    }

}