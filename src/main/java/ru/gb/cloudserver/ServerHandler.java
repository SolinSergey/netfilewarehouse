package ru.gb.cloudserver;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.gb.cloudmessages.*;
import ru.gb.handler.DownLoadFileHandler;
import ru.gb.handler.HandlerRegistry;
import ru.gb.handler.RequestHandler;
import ru.gb.cloudmessages.BasicRequest;

import java.io.IOException;
import java.time.LocalTime;


public class ServerHandler extends ChannelInboundHandlerAdapter {

    private String token;

    private static final String SERVER_PATH = System.getProperty("user.dir");

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws IOException {
        System.out.println(ctx.channel().remoteAddress());
        //GetFilesListResponse getFilesListResponse = new GetFilesListResponse("",getList());
        //ctx.writeAndFlush(getFilesListResponse);
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws IOException {
        BasicRequest request = (BasicRequest) msg;
        System.out.println("************************"+request.getClass());
        RequestHandler handler = HandlerRegistry.getHandler(request.getClass());
        if (request instanceof RegisterUserRequest){
            BasicResponse response = handler.handle(request, channelHandlerContext);
            System.out.println("Поступил запрос на регистрацию");
            channelHandlerContext.writeAndFlush(response);
        }

        if (request instanceof AuthRequest) {
            BasicResponse response = handler.handle(request, channelHandlerContext);
            if (!response.getAuthToken().equals("NotAutorized")) token = response.getAuthToken();
            channelHandlerContext.writeAndFlush(response);
        } else {
            if (token.equals(request.getAuthToken()) && request instanceof DownloadFileRequest) {
                DownLoadFileHandler downLoadFileHandler = new DownLoadFileHandler();
                downLoadFileHandler.downloadFile(token, ((DownloadFileRequest) request).getFileName(), ((DownloadFileRequest) request).getUserDir(), channelHandlerContext);
            }
            System.out.println(token);
            System.out.println(request.getAuthToken());
            if (token.equals(request.getAuthToken())) {
                //RequestHandler handler = HandlerRegistry.getHandler(request.getClass());
                BasicResponse response = handler.handle(request, channelHandlerContext);
                channelHandlerContext.writeAndFlush(response);
            }

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }

    //public List<String> getList() throws IOException {
    //   Path path= Paths.get(SERVER_PATH+"//test2//");
    //    List<String> files;
    //    files = Files.list(path)
    //           .map(p -> p.getFileName().toString())
    //           .collect(Collectors.toList());
    //  System.out.println(files);
    //   return files;
    // }

    // public String getToken() {
    //   return token;
    // }

}


