package ru.gb.transferobjects;

import java.io.Serializable;

public class BasicResponse implements Serializable {

    private final String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public BasicResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
