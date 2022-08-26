package ru.gb.hlam.transferobjects;

public class GetFilesListRequest extends BasicRequest {
    private final String path;

    public String getPath() {
        return path;
    }

    public GetFilesListRequest(String token, String path) {
        super(token);
        this.path = path;
    }
}
