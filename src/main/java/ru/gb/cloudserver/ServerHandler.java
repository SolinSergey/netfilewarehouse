package ru.gb.cloudserver;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.gb.cloudmessages.*;
import ru.gb.handler.HandlerRegistry;
import ru.gb.handler.RequestHandler;
import ru.gb.netfilewarehouse.ObjectRegistry;
import ru.gb.service.AuthService;
import ru.gb.cloudmessages.BasicRequest;
import ru.gb.handler.HandlerRegistry;
import ru.gb.handler.RequestHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;


public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static final String SERVER_PATH = System.getProperty("user.dir");
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws IOException {
        System.out.println(ctx.channel().remoteAddress());
        GetFilesListResponse getFilesListResponse = new GetFilesListResponse("",getList());
        ctx.writeAndFlush(getFilesListResponse);
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws IOException {
        BasicRequest request = (BasicRequest) msg;
        AuthService authService = ObjectRegistry.getInstance(AuthService.class);
        String authToken = request.getAuthToken();
        if (!(request instanceof RegisterUserRequest) && !authService.auth(authToken)) {
            BasicResponse authErrorResponse = new BasicResponse("Not authenticated!");
            channelHandlerContext.writeAndFlush(authErrorResponse);
        }

        RequestHandler handler = HandlerRegistry.getHandler(request.getClass());
        BasicResponse response = handler.handle(request, channelHandlerContext);
        channelHandlerContext.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }

    public List<String> getList() throws IOException {
        Path path= Paths.get(SERVER_PATH+"//test2//");
        List<String> files;
        files = Files.list(path)
                .map(p -> p.getFileName().toString())
                .collect(Collectors.toList());
        System.out.println(files);
        return files;
    }

}


