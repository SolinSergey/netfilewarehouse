package ru.gb.service;

import ru.gb.cloudmessages.UploadFileRequest;
import ru.gb.netfilewarehouse.NetFileWarehouseController;
import ru.gb.netfilewarehouse.NetworkNetty;
import ru.gb.netfilewarehouse.ObjectRegistry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class UploadFileService {

    private String userToken;
    private String currentServerDir;

    public void uploadFile(String fileName) {
        Path filePath = Paths.get(ObjectRegistry.getInstance(NetFileWarehouseController.class).localPathField.getText() + "//" + fileName);
        userToken = ObjectRegistry.getInstance(AuthService.class).getAuthToken();
        currentServerDir = ObjectRegistry.getInstance(NetFileWarehouseController.class).serverPathField.getText();
        NetworkNetty networkNetty = ObjectRegistry.getInstance(NetworkNetty.class);
        Consumer<byte[]> filePartConsumer = filePartBytes -> {
            UploadFileRequest uploadFileRequest = new UploadFileRequest(userToken, fileName, currentServerDir, filePartBytes);
            networkNetty.uploadFile(uploadFileRequest);
        };
        FileSaw fileSaw = new FileSaw();
        fileSaw.saw(filePath, filePartConsumer);


        //try{
        //System.out.println(filePath);
        //      byte[] allFileBytes = Files.readAllBytes(filePath);
        //System.out.println(filePath);
        //System.out.println(Arrays.toString(allFileBytes));
        //NetworkNetty networkNetty=ObjectRegistry.getInstance(NetworkNetty.class);
        //String fileName= filePath.getFileName().toString();
        //      UploadFileRequest uploadFileRequest = new UploadFileRequest(userToken,fileName,currentServerDir,allFileBytes);
        //     networkNetty.uploadFile(uploadFileRequest);
        // }catch (IOException e) {
        //     e.printStackTrace();
        //  }
    }
}
