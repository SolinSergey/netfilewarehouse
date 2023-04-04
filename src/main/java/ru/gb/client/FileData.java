package ru.gb.client;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileData implements Serializable {
    private final String DIR = "DIR";
    private final String FILE = "FILE";
    private String fileName;
    private String fileType;
    private long fileSize;

    public FileData(Path path) {
        try {
            this.fileName = path.getFileName().toString();
            this.fileSize = Files.size(path);
            this.fileType = Files.isDirectory(path) ? DIR : FILE;
            if (fileType.equals(DIR)) {
                fileSize = -1L;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
