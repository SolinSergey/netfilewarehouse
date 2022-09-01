package ru.gb.service;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import ru.gb.cloudmessages.DeleteFileRequest;
import ru.gb.netfilewarehouse.NetworkNetty;
import ru.gb.netfilewarehouse.ObjectRegistry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DeleteFileService {
    private String userRights;
    public void deleteFileFromLocalDir(String fileName, String userRights){
        Path path = Path.of(System.getProperty("user.dir")+"//local//"+fileName);
        if (!Files.isDirectory(path)){
            try {
                Files.delete(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteFileFromCloud (String fileName, String userDir, String userRights, String authToken){
        if (userRights.equals("full")){
            DeleteFileRequest deleteFileRequest = new DeleteFileRequest(authToken,fileName,userDir);
            ObjectRegistry.getInstance(NetworkNetty.class).sendDeleteFileRequest(deleteFileRequest);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR,"У вас нет прав на удаление файлов на сервере!!!\n Для изменения прав обратитесь к администратору!", ButtonType.OK);
            alert.showAndWait();
        }
    }
}
