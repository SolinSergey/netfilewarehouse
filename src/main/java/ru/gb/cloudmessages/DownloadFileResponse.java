package ru.gb.cloudmessages;

public class DownloadFileResponse extends BasicResponse {

    private final String fileName;
    private final String errorMessage;

    private final byte[] filePartData;

    public DownloadFileResponse(String errorMessage, String token, String fileName, byte[] filePartData) {
        super(token);
        this.fileName = fileName;
        this.filePartData = filePartData;
        this.errorMessage=errorMessage;
        //this.isLast = isLast;
    }


   /* public boolean isLast() {
        return isLast;
    }*/
    public String getFileName() {
        return fileName;
    }

    public byte[] getFilePartData() {
        return filePartData;
    }
}

