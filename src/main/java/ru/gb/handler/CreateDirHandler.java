package ru.gb.handler;

import io.netty.channel.ChannelHandlerContext;
import ru.gb.cloudmessages.CreateDirRequest;
import ru.gb.cloudmessages.CreateDirResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CreateDirHandler implements RequestHandler<CreateDirRequest, CreateDirResponse> {
    private static final String SERVER_PATH = System.getProperty("user.dir");

    @Override
    public CreateDirResponse handle(CreateDirRequest request, ChannelHandlerContext channelHandlerContext) throws IOException {
        String result;
        Path path = Path.of(SERVER_PATH + "//" + request.getCurrentDir() + "//" + request.getDirName()).normalize().toAbsolutePath();
        try {
            if (Files.notExists(path)) {
                Files.createDirectory(path);
                result = "OK";
            } else {
                result = "EXIST";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        CreateDirResponse createDirResponse = new CreateDirResponse(result, request.getAuthToken());
        return createDirResponse;
    }
}
