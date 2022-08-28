package ru.gb.cloudmessages;

public class DownloadFileRequest extends BasicRequest {

    private final String fileName;

    public DownloadFileRequest(String token, String fileName) {
        super(token);
        this.fileName = fileName;

        //this.isLast = isLast;
    }

   /* public boolean isLast() {
        return isLast;
    }*/

    public String getFileName() {
        return fileName;
    }


}