package ru.gb.cloudmessages;

public class CreateDirRequest extends BasicRequest {

    private final String dirName;

    private final String currentDir;

    public CreateDirRequest(String token, String dirName, String currentDir) {
        super(token);
        this.dirName = dirName;
        this.currentDir = currentDir;
    }

    public String getCurrentDir() {
        return currentDir;
    }

    public String getDirName() {
        return dirName;
    }


}