package ru.gb.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class FileSaw {

    private static final int MB_4 = 10_000_000;

    public void saw(Path path, Consumer<byte[]> filePartConsumer) {
        byte[] filePart = new byte[MB_4];
        File toFile = path.toFile();
        long fileLength = toFile.length();
        long totalBytesRead = 0;
        try (FileInputStream fileInputStream = new FileInputStream(path.toFile())) {
            int readBytes;
            while ((readBytes = fileInputStream.read(filePart)) != -1) {
                totalBytesRead += readBytes;
                if (totalBytesRead == fileLength) {
                    filePart = Arrays.copyOfRange(filePart, 0, readBytes);
                }
                filePartConsumer.accept(filePart);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}