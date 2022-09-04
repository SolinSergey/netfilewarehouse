package ru.gb.handler;

import io.netty.channel.ChannelHandlerContext;
import ru.gb.cloudmessages.CheckUsedSpaceRequest;
import ru.gb.cloudmessages.CheckUsedSpaceResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class CheckFreeSpaceHandler implements RequestHandler<CheckUsedSpaceRequest, CheckUsedSpaceResponse> {
    private static final String SERVER_PATH = System.getProperty("user.dir");

    @Override
    public CheckUsedSpaceResponse handle(CheckUsedSpaceRequest request, ChannelHandlerContext channelHandlerContext) throws IOException {
        String getFilesListRequestPath = SERVER_PATH + "//" + request.getPath() + "//";//request.getPath();

        Path path = Paths.get(getFilesListRequestPath).normalize().toAbsolutePath();
        long size = 0;
        try (Stream<Path> walk = Files.walk(path)) {
            size = walk
                    .filter(Files::isRegularFile)
                    .mapToLong(p -> {
                        try {
                            return Files.size(p);
                        } catch (IOException e) {
                            System.out.printf("Невозможно получить размер файла %s%n%s", p, e);
                            return 0L;
                        }
                    })
                    .sum();
        } catch (IOException e) {
            System.out.printf("Ошибка при подсчёте размера директории %s", e);
        }
        System.out.println(size);

        CheckUsedSpaceResponse checkUsedSpaceResponse = new CheckUsedSpaceResponse("OK", request.getAuthToken(), size);

        return checkUsedSpaceResponse;
    }
}
