package ru.gb.netfilewarehouse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import ru.gb.cloudmessages.*;
import ru.gb.service.AuthService;
import ru.gb.service.DownloadFileService;
import ru.gb.service.RegistrationService;

import java.nio.file.Paths;
import java.time.LocalTime;


public class ClientHandler extends ChannelInboundHandlerAdapter {
    private String userToken;
    private String userDir;
    private long userQuote;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        BasicResponse response = (BasicResponse) msg;
        NetFileWarehouseController netFileWarehouseController = ObjectRegistry.getInstance(NetFileWarehouseController.class);
        if (response instanceof RegisterUserResponse) {
            if (response.getErrorMessage().equals("OK")) {
                ObjectRegistry.getInstance(RegistrationService.class).setRegisterSuccess("successfull");
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Регистрация успешно выполнена!", ButtonType.OK);
                    alert.showAndWait();
                });
            } else {
                Platform.runLater(()->{
                    Alert alert = new Alert(Alert.AlertType.ERROR, "При регистрации пользователя произошла ошибка!\nПопробуйте произвести регистрацию повторно!", ButtonType.OK);
                    alert.showAndWait();
                });
                ObjectRegistry.getInstance(RegistrationService.class).setRegisterSuccess("error");
            }
        }
        if (response instanceof AuthResponse) {
            if (!response.getAuthToken().equals("NotAutorized")) {
                ObjectRegistry.getInstance(AuthService.class).setAuthToken(response.getAuthToken());
                ObjectRegistry.getInstance(AuthService.class).setUserDir(((AuthResponse) response).getUserDir());
                ObjectRegistry.getInstance(AuthService.class).setUserRights(((AuthResponse) response).getUserRights());
                ObjectRegistry.getInstance((AuthService.class)).setUserQuote(((AuthResponse) response).getUserQuote());
            } else {
                System.out.println("Авторизация не выполнена");
                ObjectRegistry.getInstance(AuthService.class).setAuthToken(response.getAuthToken());
            }
        }
        userToken = ObjectRegistry.getInstance(AuthService.class).getAuthToken();
        userDir = ObjectRegistry.getInstance(AuthService.class).getUserDir();
        userQuote = ObjectRegistry.getInstance(AuthService.class).getUserQuote();
        if ((response instanceof GetFilesListResponse) && (response.getAuthToken().equals(userToken))) {
            Platform.runLater(() -> {
                netFileWarehouseController.updateServerTable(((GetFilesListResponse) response).getList());
            });
        }
        if (response instanceof UploadFileResponse && response.getAuthToken().equals(userToken)) {
            String message = response.getErrorMessage();
            System.out.println(message);
            if (message.equals("OK")) {
                GetFilesListRequest getFilesListRequest = new GetFilesListRequest(userToken, ObjectRegistry.getInstance(NetFileWarehouseController.class).currentServerPath);
                ctx.writeAndFlush(getFilesListRequest);
            } else {
                System.out.println(response.getErrorMessage());
            }
        }
        if (response instanceof DownloadFileResponse && response.getAuthToken().equals(userToken)) {
            DownloadFileService downloadFileService;
            downloadFileService = ObjectRegistry.getInstance(DownloadFileService.class);
            downloadFileService.saveDownloadFile((DownloadFileResponse) response);
            Platform.runLater(() -> {
                netFileWarehouseController.updateLocalTable(Paths.get(netFileWarehouseController.localPathField.getText()));
            });
        }
        if (response instanceof DeleteFileResponse && response.getAuthToken().equals((userToken))) {
            if (response.getErrorMessage().equals("OK")) {
                GetFilesListRequest getFilesListRequest = new GetFilesListRequest(userToken, netFileWarehouseController.currentServerPath);
                ctx.writeAndFlush(getFilesListRequest);
            } else {
                System.out.println("Ошибка удаления файла на сервере");
            }
        }
        if (response instanceof CheckUsedSpaceResponse && response.getAuthToken().equals(userToken)) {
            long usedSpace = ((CheckUsedSpaceResponse) response).getUsedSpaceInUserPath();
            Platform.runLater(() -> {
                netFileWarehouseController.updateUsedSpaceProgressBar(userQuote, usedSpace);
            });
        }
        if (response instanceof CreateDirResponse && response.getAuthToken().equals(userToken)) {
            if (response.getErrorMessage().equals("OK")) {
                GetFilesListRequest getFilesListRequest = new GetFilesListRequest(userToken, netFileWarehouseController.currentServerPath);
                ctx.writeAndFlush(getFilesListRequest);
            } else if (response.getErrorMessage().equals("EXIST")) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Невозможно создать уже существующую папку!", ButtonType.OK);
                    alert.showAndWait();
                });
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }
}

