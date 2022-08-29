package ru.gb.netfilewarehouse;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.gb.cloudmessages.GetFilesListRequest;
import ru.gb.cloudmessages.UploadFileRequest;
import ru.gb.hlam.FileInfo;
import ru.gb.hlam.ListFiles;
import ru.gb.service.AuthService;
import ru.gb.service.CryptService;
import ru.gb.service.DownloadFileService;
import ru.gb.service.UploadFileService;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class NetFileWarehouseController implements Initializable {


    private static final int MAX_SIZE_OBJECT = 1_000_000_000;
    private String userToken;
    public static Stage mainStage;
    public ComboBox disksBox;
    public TextField pathField;
    public Button btnLocalToCloudCopy;
    public Channel channel;
    public ListView <String> localListView;
    public ListView <String> serverListView;

    ByteBuf buffer;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObjectRegistry.reg(this.getClass(),this);
        boolean isAuthentic=false;


        System.out.println("in Init");

        try {
            updateLocalList(getList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        boolean isAuthorized=false;
        boolean isGetAuthResponse;
        do {
            ObjectRegistry.getInstance(AuthService.class).showAuthDialog(mainStage);
            String userName=ObjectRegistry.getInstance(AuthService.class).getUserName();
            String userPassword = ObjectRegistry.getInstance(AuthService.class).getUserPassword();
            ObjectRegistry.getInstance(AuthService.class).sendAuthRequest(userName,userPassword);
            do{
                isGetAuthResponse=ObjectRegistry.getInstance(AuthService.class).isGetAuthResponse();
            }while (!isGetAuthResponse);

            String token = ObjectRegistry.getInstance(AuthService.class).getAuthToken();
            System.out.println("Token****** " + token);

            if (!token.equals("NotAutorized")) {
                isAuthorized = true;
            }
            else isAuthorized = false;
            System.out.println(isAuthorized);
            System.out.println(ObjectRegistry.getInstance(AuthService.class).getAuthToken());
        }while (!isAuthorized);

    }
    public List<String> getList() throws IOException {
        List<String> files = null;
        try{
            Path path=Paths.get(System.getProperty("user.dir")+"//test");
            files = Files.list(path)
                    .map(p -> p.getFileName().toString())
                    .collect(Collectors.toList());

        }catch (IOException e){
            e.printStackTrace();
        }
        return files;
        //System.out.println(files);

    }

    public void initLocalPanel() throws IOException {
        List<String> files;
        files = getList();
        //Path path=Paths.get(System.getProperty("user.dir")+"//test");
        //files = Files.list(path)
         //       .map(p -> p.getFileName().toString())
         //       .collect(Collectors.toList());
        //updateLocalList(files);

    }
    public void updateServerList(List <String> listFiles){
        serverListView.getItems().clear();
        System.out.println(listFiles.toString());
        serverListView.getItems().addAll(listFiles);
        //serverListView.getItems().sorted();
    }


    public void updateLocalList (List files){
        localListView.getItems().clear();
        localListView.getItems().addAll(files);
        localListView.getItems().sorted();

        //Alert alert = new Alert(Alert.AlertType.WARNING,"Не удалось обновить список файлов",ButtonType.OK);
        //alert.showAndWait();


    }




    public void btnPathUpAction(ActionEvent actionEvent) {
       Path upperPath =Paths.get(pathField.getText()).getParent();
       if (upperPath!=null){
           //updateList(upperPath);
       }
    }

    public void selectDiskAction(ActionEvent actionEvent) {
        ComboBox <String> element = (ComboBox<String>) actionEvent.getSource();
        //updateList(Paths.get(element.getSelectionModel().getSelectedItem()));
    }

    public void onMouseClicked(MouseEvent mouseEvent) {

    }


    public String getCurrentPath(){
        return pathField.getText();
    }


    public void clickbtnLocalToCloudCopy(MouseEvent mouseEvent) {
        System.out.println("Копировать в облако");
        String selectFile;
        selectFile = localListView.getSelectionModel().getSelectedItem().toString();
        System.out.println(selectFile);
        UploadFileService uploadFileService;
        uploadFileService = ObjectRegistry.getInstance(UploadFileService.class);
        uploadFileService.uploadFile(selectFile);
        //GetFilesListRequest getFilesListRequest = new GetFilesListRequest(TOKEN,"");
        //channel.writeAndFlush(getFilesListRequest);

    }

    public void clickbtnCloudToLocalCopy(MouseEvent mouseEvent) {
        System.out.println("Копировать из облака на локальный компьютер");
        String selectFile;
        selectFile = serverListView.getSelectionModel().getSelectedItem().toString();
        System.out.println(selectFile);
        DownloadFileService downloadFileService;
        downloadFileService = ObjectRegistry.getInstance(DownloadFileService.class);
        downloadFileService.sendRequest(selectFile);
    }
}