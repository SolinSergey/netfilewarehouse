package ru.gb.cloudmessages;

public class CheckUsedSpaceRequest extends BasicRequest {
    private final String userPath;

    public String getPath() {
        return userPath;
    }

    public CheckUsedSpaceRequest(String token, String userPath) {
        super(token);
        this.userPath = userPath;
    }
}
