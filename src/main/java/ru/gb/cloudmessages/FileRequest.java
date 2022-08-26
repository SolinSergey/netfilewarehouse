package ru.gb.cloudmessages;

public class FileRequest implements CloudMessage{
    private final String name;

    public FileRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
