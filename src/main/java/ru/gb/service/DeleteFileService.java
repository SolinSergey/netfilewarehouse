package ru.gb.service;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import ru.gb.cloudmessages.DeleteFileRequest;
import ru.gb.client.NetFileWarehouseController;
import ru.gb.client.NetworkNetty;
import ru.gb.client.ObjectRegistry;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DeleteFileService {

    public void deleteFileFromLocalDir(String fileName, String userRights) {
        Path path = Path.of(ObjectRegistry.getInstance(NetFileWarehouseController.class).localPathField.getText() + "//" + fileName).normalize();
        if (ObjectRegistry.getInstance(NetFileWarehouseController.class).localFileTable.getSelectionModel().getSelectedItem().getFileType().equals("FILE")) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            folderdel(path.toString());
        }
    }

    public void deleteFileFromCloud(String fileName, String currentServerDir, String userRights, String authToken) {
        if (userRights.equals("full")) {
            DeleteFileRequest deleteFileRequest = new DeleteFileRequest(authToken, fileName, currentServerDir);
            ObjectRegistry.getInstance(NetworkNetty.class).sendDeleteFileRequest(deleteFileRequest);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "У вас нет прав на удаление файлов на сервере!!!\n Для изменения прав обратитесь к администратору!", ButtonType.OK);
            alert.showAndWait();
        }
    }
    public static void folderdel(String path){
        File f= new File(path);
        if(f.exists()){
            String[] list= f.list();
            if(list.length==0){
                if(f.delete()){
                    System.out.println("folder deleted");
                    return;
                }
            }
            else {
                for(int i=0; i<list.length ;i++){
                    File f1= new File(path+"\\"+list[i]);
                    if(f1.isFile()&& f1.exists()){
                        f1.delete();
                    }
                    if(f1.isDirectory()){
                        folderdel(""+f1);
                    }
                }
                folderdel(path);
            }
        }
    }
}
