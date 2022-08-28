package ru.gb.cloudmessages;

public class AuthResponse extends BasicResponse{

    private final String authToken;

    public AuthResponse(String errorMessage, String authToken) {
        super(errorMessage);
        this.authToken=authToken;
    }

    public String getAuthToken() {
        return authToken;
    }
}
