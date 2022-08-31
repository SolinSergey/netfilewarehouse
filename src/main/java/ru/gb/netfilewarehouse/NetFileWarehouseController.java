package ru.gb.netfilewarehouse;

import io.netty.channel.Channel;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ru.gb.service.AuthService;
import ru.gb.service.DownloadFileService;
import ru.gb.service.UploadFileService;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class NetFileWarehouseController implements Initializable {

    public Label topLabel;

    private String userToken;
    public static Stage mainStage;
    public Button btnLocalToCloudCopy;

    public Channel channel;
    public ListView <String> localListView;
    public ListView <String> serverListView;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObjectRegistry.reg(this.getClass(),this);

        System.out.println("in Init");

        boolean isAuthorized=false;
        boolean isGetAuthResponse=false;
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
            String userRights = ObjectRegistry.getInstance(AuthService.class).getUserRights();
            System.out.println("userRights в контроллере "+ userRights);



            if (!token.equals("NotAutorized") && !userRights.equals("block") ) {
                isAuthorized = true;
            }
            else{
                if (token.equals("NotAutorized")){
                    Alert alert = new Alert(Alert.AlertType.ERROR,"Неправильные логин или пароль.\nПопробуйте авторизоваться повторно!",ButtonType.OK);
                    alert.showAndWait();
                }
                if (userRights.equals("block")){
                    Alert alert = new Alert(Alert.AlertType.ERROR,"Ваша учетная запись заблокирована! Доступ запрещен!",ButtonType.OK);
                    alert.showAndWait();
                }
            }
            System.out.println(isAuthorized);
            System.out.println(ObjectRegistry.getInstance(AuthService.class).getAuthToken());
        }while (!isAuthorized);

        try {
            updateLocalList(getList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setTextToTopLabel();

    }

    public void setTextToTopLabel(){
        String textForLabel="Вы авторизованы как пользователь: "+ ObjectRegistry.getInstance(AuthService.class).getUserName();
        topLabel.setText(textForLabel);
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


    }

    public void updateServerList(List <String> listFiles){
        serverListView.getItems().clear();
        System.out.println(listFiles.toString());
        serverListView.getItems().addAll(listFiles);
        serverListView.getItems().sorted();
        serverListView.getSelectionModel().selectIndices(0);
    }


    public void updateLocalList (List files){
        localListView.getItems().clear();
        localListView.getItems().addAll(files);
        localListView.getItems().sorted();
        localListView.getSelectionModel().selectIndices(0);

        //Alert alert = new Alert(Alert.AlertType.WARNING,"Не удалось обновить список файлов",ButtonType.OK);
        //alert.showAndWait();


    }

    public void clickbtnLocalToCloudCopy(MouseEvent mouseEvent) {
        System.out.println("Копировать в облако");
        String selectFile;
        selectFile = localListView.getSelectionModel().getSelectedItem().toString();
        System.out.println(selectFile);
        UploadFileService uploadFileService;
        uploadFileService = ObjectRegistry.getInstance(UploadFileService.class);
        uploadFileService.uploadFile(selectFile);


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