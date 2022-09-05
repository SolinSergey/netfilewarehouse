package ru.gb.handler;

import io.netty.channel.ChannelHandlerContext;
import ru.gb.cloudmessages.DeleteFileRequest;
import ru.gb.cloudmessages.DeleteFileResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DeleteFileHandler implements RequestHandler<DeleteFileRequest, DeleteFileResponse> {
    @Override
    public DeleteFileResponse handle(DeleteFileRequest request, ChannelHandlerContext channelHandlerContext) {
        String fileName = request.getFileName();
        String userDir = request.getUserDir();
        Path path = Path.of(System.getProperty("user.dir") + "//" + userDir + "//" + fileName);
        try {
            Files.delete(path);
        } catch (IOException e) {
            DeleteFileResponse deleteFileResponse = new DeleteFileResponse("Error", request.getAuthToken());
            e.printStackTrace();
        }
        DeleteFileResponse deleteFileResponse = new DeleteFileResponse("OK", request.getAuthToken());
        return deleteFileResponse;
    }
}
