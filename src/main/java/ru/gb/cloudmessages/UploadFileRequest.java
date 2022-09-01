package ru.gb.cloudmessages;

public class UploadFileRequest extends BasicRequest {

    private final String fileName;

    private final String userDir;

    private final byte[] filePartData;

    public UploadFileRequest(String token, String fileName, String userDir, byte[] filePartData) {
        super(token);
        this.fileName = fileName;
        this.filePartData = filePartData;
        this.userDir=userDir;
    }

    public String getUserDir() {
        return userDir;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getFilePartData() {
        return filePartData;
    }

}