package ru.gb.netfilewarehouse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javafx.application.Platform;
import ru.gb.cloudmessages.GetFilesListRequest;
import ru.gb.netfilewarehouse.ObjectRegistry;
import ru.gb.netfilewarehouse.NetFileWarehouseController;
import ru.gb.cloudmessages.BasicResponse;
import ru.gb.cloudmessages.GetFilesListResponse;
import ru.gb.cloudmessages.UploadFileResponse;

import static ru.gb.netfilewarehouse.NetworkNetty.TOKEN;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        BasicResponse response = (BasicResponse) msg;
        NetFileWarehouseController netFileWarehouseController = ObjectRegistry.getInstance(NetFileWarehouseController.class);
        if (response instanceof GetFilesListResponse) {
            GetFilesListResponse getFilesListResponse = (GetFilesListResponse) response;
            netFileWarehouseController.updateServerList(getFilesListResponse.getList());
        } else if (response instanceof UploadFileResponse) {
            String message = response.getErrorMessage();
            System.out.println(message);
           //if (message.equals("OK")) {
               //GetFilesListRequest getFilesListRequest = new GetFilesListRequest(TOKEN, "");
                //ctx.writeAndFlush(getFilesListRequest);
            } else {
                System.out.println(response.getErrorMessage());
            }
        }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}

