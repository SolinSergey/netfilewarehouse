package ru.gb.hlam.transferobjects;

import java.io.Serializable;

public class BasicRequest implements Serializable {

    private final String authToken;

    public BasicRequest(String authToken) {

        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

}
