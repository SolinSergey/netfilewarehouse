package ru.gb.netfilewarehouse;

import io.netty.channel.Channel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ru.gb.cloudmessages.GetFilesListRequest;
import ru.gb.service.AuthService;
import ru.gb.service.DeleteFileService;
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
    public Button btnDelete;
    public TableView<FileData> localFileTable;
    public TableColumn<FileData, String> localFileTypeColumn;
    public TableColumn<FileData, String> localFileNameColumn;
    public TableColumn<FileData, Long> localFileSizeColumn;
    public TextField localPathField;
    public TextField serverPathField;
    public TableView<FileData> serverFileTable;
    public TableColumn<FileData, String> serverFileTypeColumn;
    public TableColumn<FileData, String> serverFileNameColumn;
    public TableColumn<FileData, Long> serverFileSizeColumn;

    private String userToken;
    public static Stage mainStage;
    public Button btnLocalToCloudCopy;

    public Channel channel;
    public ListView<String> localListView;
    public ListView<String> serverListView;
    private String token;

    private String userRights;
    private String userDir;
    private List<String> serverList;
    private List localList;
    String currentServerPath;

    boolean isClientInit = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObjectRegistry.reg(this.getClass(), this);

        //System.out.println("in Init");

        boolean isAuthorized = false;
        boolean isGetAuthResponse = false;
        boolean isGetUserRights = false;
        do {
            ObjectRegistry.getInstance(AuthService.class).showAuthDialog(mainStage);
            //String userName=ObjectRegistry.getInstance(AuthService.class).getUserName();
            //String userPassword = ObjectRegistry.getInstance(AuthService.class).getUserPassword();
            ObjectRegistry.getInstance(AuthService.class).sendAuthRequest();
            do {
                isGetAuthResponse = ObjectRegistry.getInstance(AuthService.class).isGetAuthResponse();
                isGetUserRights = ObjectRegistry.getInstance(AuthService.class).isGetUserRights();
            } while (!isGetAuthResponse);//& !isGetUserRights


            token = ObjectRegistry.getInstance(AuthService.class).getAuthToken();
            //System.out.println("Token****** " + token);
            userRights = ObjectRegistry.getInstance(AuthService.class).getUserRights();
            userDir = ObjectRegistry.getInstance(AuthService.class).getUserDir();
            //System.out.println("userRights в контроллере "+ userRights);

            if (token.equals("NotAutorized")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Неправильные логин или пароль.\nПопробуйте авторизоваться повторно!", ButtonType.OK);
                alert.showAndWait();
            } else {
                if (userRights.equals("block")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Ваша учетная запись заблокирована! Доступ запрещен!", ButtonType.OK);
                    alert.showAndWait();
                } else {
                    isAuthorized = true;
                }
            }

            //System.out.println(isAuthorized);
            //System.out.println(ObjectRegistry.getInstance(AuthService.class).getAuthToken());
        } while (!isAuthorized);
        /*
        try {
            updateLocalList(getList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
        //Создаем локальную таблицу
        createLocalFilesTable();
        createServerFilesTable();

        //localFileSizeColumn.setCellValueFactory(param -> new SimpleLongProperty(param.getValue().fileSize));
        Path path = Paths.get(".", "local");

        setTextToTopLabel();

        if (userRights.equals("ro")) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Внимание! Ваша учетная запись имеет права только на чтение файлов из облака!\n", ButtonType.OK);
            alert.showAndWait();
        }
        String userDir = ObjectRegistry.getInstance(AuthService.class).getUserDir();
        System.out.println(userDir);
        GetFilesListRequest getFilesListRequest = new GetFilesListRequest(token, userDir);
        ObjectRegistry.getInstance(NetworkNetty.class).sendGetFileListRequest(getFilesListRequest);
        updateLocalTable(path);
        isClientInit = true;
        currentServerPath = userDir + "";
        path = Path.of(Paths.get(currentServerPath).normalize().toString());
        serverPathField.setText(currentServerPath);

    }

    public void createServerFilesTable() {
        serverFileTypeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFileType()));
        serverFileTypeColumn.setResizable(false);
        serverFileNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFileName()));
        serverFileTable.getSortOrder().add(localFileTypeColumn);
        serverFileSizeColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getFileSize()));
        serverFileSizeColumn.setCellFactory(column -> {
            return new TableCell<FileData, Long>() {
                @Override
                protected void updateItem(Long item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        String text = String.format("%,d bytes", item);
                        if (item == -1L) {
                            text = "";
                        }
                        setText(text);
                    }
                }
            };
        });
    }

    public void createLocalFilesTable() {
        localFileTypeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFileType()));
        localFileTypeColumn.setResizable(false);
        localFileNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFileName()));
        localFileTable.getSortOrder().add(localFileTypeColumn);
        localFileSizeColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getFileSize()));
        localFileSizeColumn.setCellFactory(column -> {
            return new TableCell<FileData, Long>() {
                @Override
                protected void updateItem(Long item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        String text = String.format("%,d bytes", item);
                        if (item == -1L) {
                            text = "";
                        }
                        setText(text);
                    }
                }
            };
        });
    }

    public void updateServerTable(List<FileData> list) {
        serverFileTable.getItems().clear();
        serverFileTable.getItems().addAll(list);
        serverFileTable.sort();
        serverFileTable.getSelectionModel().select(0);

    }

    public void updateLocalTable(Path path) {
        localFileTable.getItems().clear();
        try {
            localPathField.setText(path.normalize().toAbsolutePath().toString());
            localFileTable.getItems().addAll(Files.list(path).map(FileData::new).collect(Collectors.toList()));
            localFileTable.sort();
        } catch (IOException e) {
            e.printStackTrace();
        }
        localFileTable.getSelectionModel().select(0);
    }

    public void btnUpLocalDirectory(MouseEvent mouseEvent) {
        Path upperPath = Paths.get(localPathField.getText()).getParent();
        if (upperPath != null) {
            System.out.println(upperPath.toAbsolutePath());
            updateLocalTable(upperPath);
            return;
        }
    }

    public void clckLocalFileTable(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            Path path = Paths.get(localPathField.getText()).resolve(localFileTable.getSelectionModel().getSelectedItem().getFileName());
            if (Files.isDirectory(path)) {
                updateLocalTable(path);
            }
        }
    }

    public void setTextToTopLabel() {
        String textForLabel = "Вы авторизованы как пользователь: " + ObjectRegistry.getInstance(AuthService.class).getUserName();
        topLabel.setText(textForLabel);
    }

   /* public void updateServerTable(List <String> listFiles){
        serverListView.getItems().clear();
        System.out.println(listFiles.toString());
        serverListView.getItems().addAll(listFiles);
        serverListView.getItems().sorted();
        serverListView.getSelectionModel().selectIndices(0);
        serverList=listFiles;
        //if (listFiles.size()==0 && isClientInit){
        //    btnDelete.setDisable(true);
        //    System.out.println(btnDelete);
        ///}
        //else{
        //    btnDelete.setDisable(false);
        //}
    }
*/

    public void clickbtnLocalToCloudCopy(MouseEvent mouseEvent) {

        if (!userRights.equals("ro") && localFileTable.getSelectionModel().getSelectedItem().getFileType().equals("FILE")) {
            //System.out.println(userRights);
            //System.out.println("Копировать в облако");
            String selectFile = localFileTable.getSelectionModel().getSelectedItem().getFileName();
            //System.out.println(selectFile);
            UploadFileService uploadFileService = ObjectRegistry.getInstance(UploadFileService.class);
            uploadFileService.uploadFile(selectFile);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "У вас нет прав на загрузку файлов на сервер!!!\n Для изменения прав обратитесь к администратору!", ButtonType.OK);
            alert.showAndWait();
        }


    }

    public void clickbtnCloudToLocalCopy(MouseEvent mouseEvent) {
        //System.out.println("Копировать из облака на локальный компьютер");
        String selectFile;
        selectFile = serverFileTable.getSelectionModel().getSelectedItem().getFileName();
        //System.out.println(selectFile);

        //System.out.println(currentServerPath);
        if (serverFileTable.getSelectionModel().getSelectedItem().getFileType().equals("FILE")) {
            DownloadFileService downloadFileService;
            downloadFileService = ObjectRegistry.getInstance(DownloadFileService.class);
            downloadFileService.sendRequest(selectFile, currentServerPath);
        }
    }

    public void clickbtnDeleteFile(MouseEvent mouseEvent) {
        String fileForDelete;
        if (localListView.isFocused()) {
            fileForDelete = localListView.getSelectionModel().getSelectedItem().toString();
            ObjectRegistry.getInstance(DeleteFileService.class).deleteFileFromLocalDir(fileForDelete, userRights);
            //try {
            //updateLocalList(getList());
            // } catch (IOException e) {
            //  e.printStackTrace();
            // }
        }
        if (serverListView.isFocused()) {
            fileForDelete = serverListView.getSelectionModel().getSelectedItem().toString();
            ObjectRegistry.getInstance(DeleteFileService.class).deleteFileFromCloud(fileForDelete, userDir, userRights, token);
        }
    }

    public void clckServerFileTable(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            if (serverFileTable.getSelectionModel().getSelectedItem().getFileType().equals("DIR")) {
                currentServerPath = currentServerPath + "//" + serverFileTable.getSelectionModel().getSelectedItem().getFileName();
                System.out.println(serverFileTable.getSelectionModel().getSelectedItem().getFileType());
                System.out.println(currentServerPath);
                GetFilesListRequest getFilesListRequest = new GetFilesListRequest(token, currentServerPath);
                ObjectRegistry.getInstance(NetworkNetty.class).sendGetFileListRequest(getFilesListRequest);
                Path path = Paths.get(currentServerPath).normalize();
                serverPathField.setText("\\" + path);
            }

            //Path path = Paths.get(localPathField.getText()).resolve(localFileTable.getSelectionModel().getSelectedItem().getFileName());
            // if (Files.isDirectory(path)){
            //     updateLocalTable(path);
            // }
        }
    }

    public void btnUpServerDirectory(MouseEvent mouseEvent) {
        if (!currentServerPath.equals(userDir)) {
            int ii = currentServerPath.lastIndexOf("//");
            currentServerPath = currentServerPath.substring(0, ii);
            System.out.println(currentServerPath);
            GetFilesListRequest getFilesListRequest = new GetFilesListRequest(token, currentServerPath);
            ObjectRegistry.getInstance(NetworkNetty.class).sendGetFileListRequest(getFilesListRequest);
            Path path = Paths.get(currentServerPath).normalize();
            serverPathField.setText("\\" + path);
        }
    }


   /* public void keyPressedServerListView(KeyEvent keyEvent) {
        System.out.println("Активен localList");
        if (localListView.isFocused()){
            if (localList.size()==0){
                btnDelete.setDisable(true);
            }
            else btnDelete.setDisable(false);
        }

    }

    public void clkMouseServerListView(MouseEvent mouseEvent) {
        if (serverListView.isFocused()){
            if (serverListView.getSelectionModel().getSelectedItem()==null){
                btnDelete.setDisable(true);
            }
            else btnDelete.setDisable(false);
        }
        System.out.println("Активен ");
    }

    public void btnUpServerDirectory(MouseEvent mouseEvent) {
    }

/*
    public void clkMouseLocalListView(MouseEvent mouseEvent) {
        if (localListView.isFocused()){
            if (localListView.getSelectionModel().getSelectedItem()==null){
                btnDelete.setDisable(true);
            }
            else btnDelete.setDisable(false);
        }
    }

    public void keyPressedLocalListView(KeyEvent keyEvent) {
        System.out.println("активен serverList");
        if (serverListView.isFocused()){
            if (serverList.size()==0){
                btnDelete.setDisable(true);

            }
            else btnDelete.setDisable(false);
        }
    }

    public void updateLocalList (List files){
        //System.out.println(files.size());
        if (files.size()==0){
            btnDelete.setDisable(true);
            System.out.println(btnDelete);
        }
        else{
            btnDelete.setDisable(false);
        }
        localListView.getItems().clear();
        localListView.getItems().addAll(files);
        localListView.getItems().sorted();
        localListView.getSelectionModel().selectIndices(0);
        localList=files;


        //Alert alert = new Alert(Alert.AlertType.WARNING,"Не удалось обновить список файлов",ButtonType.OK);
        //alert.showAndWait();


    }

    public List<String> getList() throws IOException {
        List<String> files = null;
        try{
            Path path=Paths.get(System.getProperty("user.dir")+"//local");

            files = Files.list(path)
                    .map(p -> p.getFileName().toString())
                    .collect(Collectors.toList());

        }catch (IOException e){
            e.printStackTrace();
        }
        return files;


    }

 */
}