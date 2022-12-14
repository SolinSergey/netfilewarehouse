package ru.gb.handler;

import io.netty.channel.ChannelHandlerContext;
import ru.gb.cloudmessages.UploadFileRequest;
import ru.gb.cloudmessages.UploadFileResponse;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UploadFileHandler implements RequestHandler<UploadFileRequest, UploadFileResponse> {
    private static final String SERVER_PATH = System.getProperty("user.dir");
    private static final Map<ChannelHandlerContext, RandomAccessFile> FILE_DESCRIPTOR_MAP = new ConcurrentHashMap<>();
    private String userDir;

    @Override
    public UploadFileResponse handle(UploadFileRequest request, ChannelHandlerContext context) throws IOException {
        String fileName = request.getFileName();
        userDir = request.getUserDir();
        byte[] filePartData = request.getFilePartData();
        try {
            Path write = Files.write(Paths.get(SERVER_PATH + "//" + request.getUserDir() + "//" + fileName).normalize().toAbsolutePath(), filePartData, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            ex.printStackTrace();
            return new UploadFileResponse("Не удалось сохранить файл на сервере", request.getAuthToken());
        }
        return new UploadFileResponse("OK", request.getAuthToken());
    }
}

