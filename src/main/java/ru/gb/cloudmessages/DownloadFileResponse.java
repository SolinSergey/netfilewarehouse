package ru.gb.cloudmessages;

public class DownloadFileResponse extends BasicResponse {
    private final String fileName;
    private final byte[] filePartData;

    public DownloadFileResponse(String errorMessage, String token, String fileName, byte[] filePartData) {
        super(errorMessage, token);
        this.fileName = fileName;
        this.filePartData = filePartData;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getFilePartData() {
        return filePartData;
    }

}

