package ru.gb.netfilewarehouse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javafx.application.Platform;
import ru.gb.cloudmessages.*;
import ru.gb.service.AuthService;
import ru.gb.service.DownloadFileService;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalTime;


public class ClientHandler extends ChannelInboundHandlerAdapter {

    private String userToken;
    private String userDir;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println(msg.getClass().toString() + " " + LocalTime.now().toString());
        BasicResponse response = (BasicResponse) msg;
        NetFileWarehouseController netFileWarehouseController = ObjectRegistry.getInstance(NetFileWarehouseController.class);

        if (response instanceof AuthResponse){
            //System.out.println("AuthResponse получен");
            if (!response.getAuthToken().equals("NotAutorized")){
                ObjectRegistry.getInstance(AuthService.class).setAuthToken(response.getAuthToken());
                ObjectRegistry.getInstance(AuthService.class).setUserDir(((AuthResponse) response).getUserDir());
                ObjectRegistry.getInstance(AuthService.class).setUserRights(((AuthResponse) response).getUserRights());
            }
            else{
                System.out.println("Авторизация не выполнена");
                ObjectRegistry.getInstance(AuthService.class).setAuthToken(response.getAuthToken());
            }
        }

        userToken=ObjectRegistry.getInstance(AuthService.class).getAuthToken();
        userDir=ObjectRegistry.getInstance(AuthService.class).getUserDir();
        if ((response instanceof GetFilesListResponse) && (response.getAuthToken().equals(userToken))) {
            System.out.println("response пришел "+((GetFilesListResponse) response).getList());
            Platform.runLater(()->{
                netFileWarehouseController.updateServerTable(((GetFilesListResponse) response).getList());

            });

        }

        if (response instanceof UploadFileResponse && response.getAuthToken().equals(userToken)) {
            String message = response.getErrorMessage();
            System.out.println(message);


            if (message.equals("OK")) {
                GetFilesListRequest getFilesListRequest = new GetFilesListRequest(userToken,ObjectRegistry.getInstance(NetFileWarehouseController.class).currentServerPath);
                ctx.writeAndFlush(getFilesListRequest);
            }
            else {
                System.out.println(response.getErrorMessage());
            }
        }

        if (response instanceof DownloadFileResponse && response.getAuthToken().equals(userToken)){
            //System.out.println("Пришел DownloadFileResponse");
            //System.out.println("Поступил объект с файлом: " + ((DownloadFileResponse) response).getFileName());
            DownloadFileService downloadFileService;
            downloadFileService = ObjectRegistry.getInstance(DownloadFileService.class);
            downloadFileService.saveDownloadFile((DownloadFileResponse) response);
            Platform.runLater(()->{
                netFileWarehouseController.updateLocalTable(Paths.get(netFileWarehouseController.localPathField.getText()));

            });
        }

        if (response instanceof DeleteFileResponse && response.getAuthToken().equals((userToken))){
            if (response.getErrorMessage().equals("OK")){
                GetFilesListRequest getFilesListRequest = new GetFilesListRequest(userToken,userDir);
                ctx.writeAndFlush(getFilesListRequest);
            }
            else {
                System.out.println("Ошибка удаления файла на сервере");
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }
}

