package ru.gb.cloudmessages;

public class AuthResponse extends BasicResponse{
    private String userDir;

    public String getUserRights() {
        return userRights;
    }

    public AuthResponse(String errorMessage, String authToken, String userDir, String userRights) {
        super(errorMessage, authToken);
        this.userDir = userDir;
        this.userRights = userRights;
    }

    private String userRights;

    public String getUserDir() {
        return userDir;
    }
}
