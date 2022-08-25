package ru.gb.netfilewarehouse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import javafx.application.Platform;
import ru.gb.cloudmessages.ListFiles;

import java.io.IOException;
import java.util.ArrayList;
//import ru.gb.file.warehouse.netty.common.dto.BasicResponse;
//import ru.gb.file.warehouse.netty.common.dto.GetFilesListResponse;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    NetFileWarehouseController instance;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
      if (msg instanceof ListFiles listFiles){
          listFiles = (ListFiles) msg;
          System.out.println(listFiles.getFiles().toString());
          System.out.println(NetFileWarehouseController.getInstance());
          //NetFileWarehouseController.getInstance().initLocalPanel();
          NetFileWarehouseController.getInstance().serverListView.getItems().addAll("1","2","3");

      }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
