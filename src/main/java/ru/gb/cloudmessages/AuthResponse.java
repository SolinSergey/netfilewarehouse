package ru.gb.cloudmessages;

public class AuthResponse extends BasicResponse{
    private final String userDir;

    private final String userRights;

    public AuthResponse(String errorMessage, String authToken, String userDir, String userRights) {
        super(errorMessage, authToken);
        this.userRights=userRights;
        this.userDir = userDir;
    }

    public String getUserRights() {
        return userRights;
    }

    public String getUserDir() {
        return userDir;
    }
}
