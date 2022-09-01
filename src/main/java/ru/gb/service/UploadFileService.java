package ru.gb.service;

import ru.gb.cloudmessages.UploadFileRequest;
import ru.gb.netfilewarehouse.NetworkNetty;
import ru.gb.netfilewarehouse.ObjectRegistry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UploadFileService {

    private String userToken;
    private String userDir;
    public void uploadFile(String fileName) {
        Path filePath = Paths.get(System.getProperty("user.dir")+"//local//"+fileName);
        userToken = ObjectRegistry.getInstance(AuthService.class).getAuthToken();
        userDir = ObjectRegistry.getInstance(AuthService.class).getUserDir();
       try{
            //System.out.println(filePath);
            byte[] allFileBytes = Files.readAllBytes(filePath);
            //System.out.println(filePath);
            //System.out.println(Arrays.toString(allFileBytes));
            NetworkNetty networkNetty=ObjectRegistry.getInstance(NetworkNetty.class);
            //String fileName= filePath.getFileName().toString();
            UploadFileRequest uploadFileRequest = new UploadFileRequest(userToken,fileName,userDir,allFileBytes);
            networkNetty.uploadFile(uploadFileRequest);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
