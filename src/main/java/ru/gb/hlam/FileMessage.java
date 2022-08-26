package ru.gb.hlam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileMessage implements CloudMessage{
    private final long size;
    private final byte[] data;

    private final String name;

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public byte[] getData() {
        return data;
    }

    public FileMessage (Path path) throws IOException{
        size= Files.size(path);
        data = Files.readAllBytes(path);
        name = path.getFileName().toString();
    }
}
