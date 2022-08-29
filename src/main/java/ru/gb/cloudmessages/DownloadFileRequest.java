package ru.gb.cloudmessages;

public class DownloadFileRequest extends BasicRequest {

    private final String fileName;

    public DownloadFileRequest(String token, String fileName) {
        super(token);
        this.fileName = fileName;
    }



    public String getFileName() {
        return fileName;
    }


}