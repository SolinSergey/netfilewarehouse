package ru.gb.cloudmessages;

public class AuthResponse extends BasicResponse{

    public AuthResponse(String errorMessage, String authToken) {
        super(errorMessage,authToken);
    }

}
