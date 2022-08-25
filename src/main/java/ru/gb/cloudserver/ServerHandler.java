package ru.gb.cloudserver;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.gb.transferobjects.BasicRequest;
import ru.gb.transferobjects.BasicResponse;
import ru.gb.transferobjects.GetFilesListResponse;
import ru.gb.transferobjects.GetFilesListRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private ByteBuf totalBuf;


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress());
        totalBuf= ctx.alloc().buffer();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        BasicRequest request = (BasicRequest) msg;

        if (request instanceof  GetFilesListRequest){
            GetFilesListRequest getFilesListRequest = (GetFilesListRequest) request;
            String getFilesListRequestPath = getFilesListRequest.getPath();
            Path path = Paths.get(getFilesListRequestPath);
            String[] list = path.toFile().list();
            GetFilesListResponse getFileListResponse =
                    new GetFilesListResponse("OK", list!=null ? List.of(list): Collections.emptyList());
            ctx.writeAndFlush(getFileListResponse);
        }else{
            BasicResponse errorResponse = new BasicResponse("Unexpected request");
            ctx.writeAndFlush(errorResponse);
        }

    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
