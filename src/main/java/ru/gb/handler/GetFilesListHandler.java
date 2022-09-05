package ru.gb.handler;

import io.netty.channel.ChannelHandlerContext;
import ru.gb.cloudmessages.GetFilesListRequest;
import ru.gb.cloudmessages.GetFilesListResponse;
import ru.gb.netfilewarehouse.FileData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GetFilesListHandler implements RequestHandler<GetFilesListRequest, GetFilesListResponse> {
    private static final String SERVER_PATH = System.getProperty("user.dir");

    @Override
    public GetFilesListResponse handle(GetFilesListRequest request, ChannelHandlerContext context) {
        String getFilesListRequestPath = SERVER_PATH + "//" + request.getPath() + "//";//request.getPath();
        //System.out.println(getFilesListRequestPath);
        Path path = Paths.get(getFilesListRequestPath).normalize().toAbsolutePath();
        List<FileData> list = null;
        try {
            list = Files.list(path).map(FileData::new).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        GetFilesListResponse response = new GetFilesListResponse("OK", request.getAuthToken(), list);

        return response;
    }

}