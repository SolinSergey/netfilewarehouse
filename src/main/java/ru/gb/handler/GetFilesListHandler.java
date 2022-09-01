package ru.gb.handler;

import io.netty.channel.ChannelHandlerContext;
import ru.gb.cloudmessages.GetFilesListRequest;
import ru.gb.cloudmessages.GetFilesListResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class GetFilesListHandler implements RequestHandler<GetFilesListRequest, GetFilesListResponse> {
    private static final String SERVER_PATH = System.getProperty("user.dir");
    @Override
    public GetFilesListResponse handle(GetFilesListRequest request, ChannelHandlerContext context) {
        String getFilesListRequestPath = SERVER_PATH+"//"+request.getPath()+"//";//request.getPath();
        //System.out.println(getFilesListRequestPath);
        Path path = Paths.get(getFilesListRequestPath);
        String[] list = path.toFile().list();
        return new GetFilesListResponse("OK", request.getAuthToken(),list != null ? List.of(list) : Collections.emptyList());
    }

}