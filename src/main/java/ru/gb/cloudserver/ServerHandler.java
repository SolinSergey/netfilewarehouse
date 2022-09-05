package ru.gb.cloudserver;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.gb.cloudmessages.*;
import ru.gb.handler.DownLoadFileHandler;
import ru.gb.handler.HandlerRegistry;
import ru.gb.handler.RequestHandler;
import ru.gb.cloudmessages.BasicRequest;

import java.io.IOException;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    private String token;

    private static final String SERVER_PATH = System.getProperty("user.dir");

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws IOException {
        System.out.println(ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws IOException {
        BasicRequest request = (BasicRequest) msg;
        RequestHandler handler = HandlerRegistry.getHandler(request.getClass());
        if (request instanceof RegisterUserRequest) {
            BasicResponse response = handler.handle(request, channelHandlerContext);
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
            if (token.equals(request.getAuthToken())) {
                BasicResponse response = handler.handle(request, channelHandlerContext);
                channelHandlerContext.writeAndFlush(response);
            }

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }

}


