package ru.gb.cloudmessages;

public class DownloadFileRequest extends BasicRequest {

    private final String fileName;

    private final String userDir;

    public DownloadFileRequest(String token, String fileName, String userDir) {
        super(token);
        this.fileName = fileName;
        this.userDir = userDir;
    }


    public String getUserDir() {
        return userDir;
    }

    public String getFileName() {
        return fileName;
    }


}