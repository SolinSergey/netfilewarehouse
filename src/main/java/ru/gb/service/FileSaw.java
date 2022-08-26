package ru.gb.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.function.BiConsumer;

public class FileSaw {

    private static final int MB_4 = 4_000_000;

    public void saw(Path path, BiConsumer<byte[], Boolean> filePartConsumer) {
        byte[] filePart = new byte[MB_4];
        try (FileInputStream fileInputStream = new FileInputStream(path.toFile())) {
            int readBytes;
            while ((readBytes = fileInputStream.read(filePart)) != -1) {
                boolean isLast = false;
                if (readBytes < MB_4) {
                    filePart = Arrays.copyOfRange(filePart, 0, readBytes);
                    isLast = true;
                }
                filePartConsumer.accept(filePart, isLast);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}