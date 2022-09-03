package ru.gb.cloudmessages;

public class AuthResponse extends BasicResponse{
    private final String userDir;

    private final String userRights;

    private long userQuote;

    public AuthResponse(String errorMessage, String authToken, String userDir, String userRights, long userQuote) {
        super(errorMessage, authToken);
        this.userRights=userRights;
        this.userDir = userDir;
        this.userQuote = userQuote;
    }

    public String getUserRights() {
        return userRights;
    }

    public String getUserDir() {
        return userDir;
    }

    public final long getUserQuote() {
        return userQuote;
    }

    public void setUserQuote(long userQuote) {
        this.userQuote = userQuote;
    }
}
