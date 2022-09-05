package ru.gb.service;

import ru.gb.cloudmessages.DownloadFileRequest;
import ru.gb.cloudmessages.DownloadFileResponse;
import ru.gb.netfilewarehouse.NetFileWarehouseController;
import ru.gb.netfilewarehouse.NetworkNetty;
import ru.gb.netfilewarehouse.ObjectRegistry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class DownloadFileService {
    String userToken;

    public void sendRequest(String filename, String currentServerPath) {
        userToken = ObjectRegistry.getInstance(AuthService.class).getAuthToken();
        DownloadFileRequest downloadFileRequest = new DownloadFileRequest(userToken, filename, currentServerPath);
        NetworkNetty networkNetty = ObjectRegistry.getInstance(NetworkNetty.class);
        networkNetty.sendDownloadRequest(downloadFileRequest);
    }

    public void saveDownloadFile(DownloadFileResponse response) {
        try {
            Path filePath = Paths.get(ObjectRegistry.getInstance(NetFileWarehouseController.class).localPathField.getText() + "//" + response.getFileName());
            Files.write(filePath, response.getFilePartData(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Невозможно сохранить файл");
        }
    }
}
