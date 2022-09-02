package ru.gb.cloudmessages;

public class ChangeDirRequest extends BasicRequest {

    private final String path;

    public String getPath() {
        return path;
    }

    public ChangeDirRequest(String token, String path) {
        super(token);
        this.path = path;
    }
}
