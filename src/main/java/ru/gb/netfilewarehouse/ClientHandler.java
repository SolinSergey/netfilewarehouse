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
    private String userDir;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        BasicResponse response = (BasicResponse) msg;
        NetFileWarehouseController netFileWarehouseController = ObjectRegistry.getInstance(NetFileWarehouseController.class);

        if (response instanceof AuthResponse){
            //System.out.println("AuthResponse получен");
            if (!response.getAuthToken().equals("NotAutorized")){
                //System.out.println("authToken="+response.getAuthToken());
               // System.out.println("userDir="+((AuthResponse) response).getUserDir());
               // System.out.println("userRights="+((AuthResponse) response).getUserRights());
                ObjectRegistry.getInstance(AuthService.class).setAuthToken(response.getAuthToken());
                ObjectRegistry.getInstance(AuthService.class).setUserDir(((AuthResponse) response).getUserDir());
               // System.out.println(ObjectRegistry.getInstance((AuthService.class)).getUserDir()+"****************");
                ObjectRegistry.getInstance(AuthService.class).setUserRights(((AuthResponse) response).getUserRights());
               // System.out.println(ObjectRegistry.getInstance((AuthService.class)).getUserRights()+"****************");
            }
            else{
                //System.out.println("Авторизация не выполнена");
                ObjectRegistry.getInstance(AuthService.class).setAuthToken(response.getAuthToken());
            }
        }

        userToken=ObjectRegistry.getInstance(AuthService.class).getAuthToken();
        userDir=ObjectRegistry.getInstance(AuthService.class).getUserDir();
        //System.out.println("ClientHandler userToken!!! ====== " +userToken);
        //userDir=ObjectRegistry.getInstance(AuthService.class).getUserDir();
        //String responseUserToken=;
       //System.out.println("ClientHandler responseUserToken!!! ====== " +responseUserToken);

        if ((response instanceof GetFilesListResponse) && (response.getAuthToken().equals(userToken))) {
            Platform.runLater(()->{
                netFileWarehouseController.updateServerList(((GetFilesListResponse) response).getList());
            });

        }
        if (response instanceof UploadFileResponse && response.getAuthToken().equals(userToken)) {
            String message = response.getErrorMessage();
            System.out.println(message);


            if (message.equals("OK")) {
                GetFilesListRequest getFilesListRequest = new GetFilesListRequest(userToken,userDir);
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
                try {
                    netFileWarehouseController.updateLocalList(netFileWarehouseController.getList());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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
}

