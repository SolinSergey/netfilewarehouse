package ru.gb.netfilewarehouse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javafx.application.Platform;
import ru.gb.cloudmessages.*;
import ru.gb.service.AuthService;
import ru.gb.service.DownloadFileService;

import java.io.IOException;



public class ClientHandler extends ChannelInboundHandlerAdapter {

    private String userToken;


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        BasicResponse response = (BasicResponse) msg;
        NetFileWarehouseController netFileWarehouseController = ObjectRegistry.getInstance(NetFileWarehouseController.class);

        if (response instanceof AuthResponse){
            System.out.println("AuthResponse получен");

            if (!response.getAuthToken().equals("NotAutorized")){
                System.out.println("authToken="+response.getAuthToken());
                ObjectRegistry.getInstance(AuthService.class).setAuthToken(response.getAuthToken());
            }
            else{
                System.out.println("Авторизация не выполнена");
                ObjectRegistry.getInstance(AuthService.class).setAuthToken(response.getAuthToken());
            }
        }


        if (response instanceof GetFilesListResponse getFilesListResponse) {
            Platform.runLater(()->{
                netFileWarehouseController.updateServerList(getFilesListResponse.getList());
            });

        }
        if (response instanceof UploadFileResponse) {
            String message = response.getErrorMessage();
            System.out.println(message);
            if (message.equals("OK")) {
                GetFilesListRequest getFilesListRequest = new GetFilesListRequest(userToken, "");
                ctx.writeAndFlush(getFilesListRequest);
            }
            else {
                System.out.println(response.getErrorMessage());
            }
        }

        if (response instanceof DownloadFileResponse){
            System.out.println("Пришел DownloadFileResponse");
            System.out.println("Поступил объект с файлом: " + ((DownloadFileResponse) response).getFileName());
            DownloadFileService downloadFileService;
            downloadFileService = ObjectRegistry.getInstance(DownloadFileService.class);
            downloadFileService.saveDownloadFile((DownloadFileResponse) response);
            Platform.runLater(()->{
                try {
                    netFileWarehouseController.updateLocalList(netFileWarehouseController.getList());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}

