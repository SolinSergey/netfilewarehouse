package ru.gb.service;

import ru.gb.cloudmessages.DownloadFileRequest;
import ru.gb.cloudmessages.DownloadFileResponse;
import ru.gb.netfilewarehouse.NetworkNetty;
import ru.gb.netfilewarehouse.ObjectRegistry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



public class DownloadFileService {
    String userToken;
    public void sendRequest(String filename){
        userToken = ObjectRegistry.getInstance(CryptService.class).getUserToken();
        System.out.println("DownloadFileService.sendRequest    " + filename);
        DownloadFileRequest downloadFileRequest=new DownloadFileRequest(userToken,filename);
        NetworkNetty networkNetty= ObjectRegistry.getInstance(NetworkNetty.class);
        networkNetty.sendDownloadRequest(downloadFileRequest);
    }

    public void saveDownloadFile(DownloadFileResponse response){
        System.out.println("На сохранение поступил файл: " + response.getFileName());
        try {
            Path filePath = Paths.get(System.getProperty("user.dir")+"//test//"+response.getFileName());
            System.out.println("Путь сохранения: "+filePath.toString());
            Files.write(filePath, response.getFilePartData());
        } catch (IOException e) {
            System.out.println("Невозможно сохранить файл");
        }
    }
}
