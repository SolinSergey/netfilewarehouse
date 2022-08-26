package ru.gb.cloudmessages;

public class UploadFileRequest extends BasicRequest {

    private final String fileName;
    private final byte[] filePartData;

    public UploadFileRequest(String token, String fileName, byte[] filePartData) {
        super(token);
        this.fileName = fileName;
        this.filePartData = filePartData;
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