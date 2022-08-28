package ru.gb.service;

import ru.gb.cloudmessages.UploadFileRequest;
import ru.gb.netfilewarehouse.NetworkNetty;
import ru.gb.netfilewarehouse.ObjectRegistry;
import ru.gb.service.FileSaw;
import ru.gb.service.UploadFileService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.BiConsumer;


public class UploadFileService {

    private String userToken;
    public void uploadFile(String pathToFile) {
        Path filePath = Paths.get(System.getProperty("user.dir")+"//test//"+pathToFile);
        userToken = ObjectRegistry.getInstance(CryptService.class).getUserToken();
       try{
            //System.out.println(filePath);
            byte[] allFileBytes = Files.readAllBytes(filePath);
            //System.out.println(filePath);
            //System.out.println(Arrays.toString(allFileBytes));
            NetworkNetty networkNetty=ObjectRegistry.getInstance(NetworkNetty.class);
            String fileName=filePath.getFileName().toString();
            UploadFileRequest uploadFileRequest = new UploadFileRequest(userToken,pathToFile,allFileBytes);
            networkNetty.uploadFile(uploadFileRequest);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
