package ru.gb.netfilewarehouse;

import io.netty.channel.Channel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ru.gb.cloudmessages.CheckUsedSpaceRequest;
import ru.gb.cloudmessages.GetFilesListRequest;
import ru.gb.service.*;

import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class NetFileWarehouseController implements Initializable {
    @FXML
    public Label topLabel;
    @FXML
    public Button btnDelete;
    @FXML
    public TableView<FileData> localFileTable;
    @FXML
    public TableColumn<FileData, String> localFileTypeColumn;
    @FXML
    public TableColumn<FileData, String> localFileNameColumn;
    @FXML
    public TableColumn<FileData, Long> localFileSizeColumn;
    @FXML
    public TextField localPathField;
    @FXML
    public TextField serverPathField;
    @FXML
    public TableView<FileData> serverFileTable;
    @FXML
    public TableColumn<FileData, String> serverFileTypeColumn;
    @FXML
    public TableColumn<FileData, String> serverFileNameColumn;
    @FXML
    public TableColumn<FileData, Long> serverFileSizeColumn;
    @FXML
    public ProgressBar serverQwoteProgress;
    @FXML
    public Label freeSpaceProgressLabel;
    @FXML
    public ComboBox diskBox;
    public static Stage mainStage;
    @FXML
    public Button btnLocalToCloudCopy;

    public Channel channel;
    private String token;

    private String userRights;

    private String userDir;
    private long freeSpaceUserDir;
    private List<FileData> localList;
    private List<FileData> serverList;
    String currentServerPath;
    boolean isClientInit = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObjectRegistry.reg(this.getClass(), this);
        boolean isAuthorized = false;
        boolean isGetAuthResponse = false;
        boolean isGetUserRights = false;
        do {
            ObjectRegistry.getInstance(AuthService.class).showAuthDialog(mainStage);
            ObjectRegistry.getInstance(AuthService.class).sendAuthRequest();
            do {
                isGetAuthResponse = ObjectRegistry.getInstance(AuthService.class).isGetAuthResponse();
                isGetUserRights = ObjectRegistry.getInstance(AuthService.class).isGetUserRights();
            } while (!isGetAuthResponse);//& !isGetUserRights
            token = ObjectRegistry.getInstance(AuthService.class).getAuthToken();
            userRights = ObjectRegistry.getInstance(AuthService.class).getUserRights();
            userDir = ObjectRegistry.getInstance(AuthService.class).getUserDir();
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
        } while (!isAuthorized);
        createLocalFilesTable();
        createServerFilesTable();
        Path path = Paths.get(".", "local");
        setTextToTopLabel();
        if (userRights.equals("ro")) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Внимание! Ваша учетная запись имеет права только на чтение файлов из облака!\n", ButtonType.OK);
            alert.showAndWait();
        }
        String userDir = ObjectRegistry.getInstance(AuthService.class).getUserDir();
        GetFilesListRequest getFilesListRequest = new GetFilesListRequest(token, userDir);
        ObjectRegistry.getInstance(NetworkNetty.class).sendGetFileListRequest(getFilesListRequest);
        CheckUsedSpaceRequest checkUsedSpaceRequest = new CheckUsedSpaceRequest(token, userDir);
        ObjectRegistry.getInstance(NetworkNetty.class).sendCheckFreeSpaceRequest(checkUsedSpaceRequest);
        updateLocalTable(path);
        isClientInit = true;
        currentServerPath = userDir + "";
        serverPathField.setText(currentServerPath);
        diskBox.getItems().clear();
        for (Path p : FileSystems.getDefault().getRootDirectories()) {
            diskBox.getItems().add(p.toString());
        }
        diskBox.getSelectionModel().select(0);
        localFileTable.requestFocus();
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
        serverList = list;
        serverFileTable.getItems().clear();
        serverFileTable.getItems().addAll(list);
        serverFileTable.sort();
        serverFileTable.getSelectionModel().select(0);
        CheckUsedSpaceRequest checkUsedSpaceRequest = new CheckUsedSpaceRequest(token, userDir);
        ObjectRegistry.getInstance(NetworkNetty.class).sendCheckFreeSpaceRequest(checkUsedSpaceRequest);
    }

    public void updateLocalTable(Path path) {
        localFileTable.getItems().clear();
        try {
            localPathField.setText(path.normalize().toAbsolutePath().toString());
            localList = Files.list(path).map(FileData::new).collect(Collectors.toList());
            localFileTable.getItems().addAll(localList);
            localFileTable.sort();
        } catch (IOException e) {
            e.printStackTrace();
        }
        localFileTable.getSelectionModel().select(0);
    }

    @FXML
    public void btnUpLocalDirectory(MouseEvent mouseEvent) {
        Path upperPath = Paths.get(localPathField.getText()).getParent();
        if (upperPath != null) {
            updateLocalTable(upperPath);
            localFileTable.requestFocus();
            return;
        }
        localFileTable.requestFocus();
    }

    @FXML
    public void clckLocalFileTable(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            Path path = Paths.get(localPathField.getText()).resolve(localFileTable.getSelectionModel().getSelectedItem().getFileName());
            if (Files.isDirectory(path)) {
                updateLocalTable(path);
            }
        }
        localFileTable.requestFocus();
    }

    public void setTextToTopLabel() {
        String textForLabel = "Вы авторизованы как пользователь: " + ObjectRegistry.getInstance(AuthService.class).getUserName();
        topLabel.setText(textForLabel);
    }

    @FXML
    public void clickbtnLocalToCloudCopy(MouseEvent mouseEvent) {
        if (localFileTable.isFocused()) {
            if (localList.size() > 0) {
                if (!userRights.equals("ro")
                        && localFileTable.getSelectionModel().getSelectedItem().getFileType().equals("FILE")) {
                    String selectFile = localFileTable.getSelectionModel().getSelectedItem().getFileName();
                    boolean isSelected = false;
                    for (FileData f : serverList) {
                        if (selectFile.equals(f.getFileName())) {
                            isSelected = true;
                        }
                    }
                    long fileSize;
                    if (!isSelected) {
                        Path path = Paths.get(localPathField.getText() + "//" + selectFile).normalize().toAbsolutePath();
                        try {
                            fileSize = Files.size(path);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        if (fileSize < freeSpaceUserDir) {
                            UploadFileService uploadFileService = ObjectRegistry.getInstance(UploadFileService.class);
                            uploadFileService.uploadFile(selectFile);
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING, "В удаленном хранилище недостаточно свободного места!\nНевозможно скопировать файл!", ButtonType.OK);
                            alert.showAndWait();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Копирование невозможно!!!\nВ удаленном хранилище уже имеется указанный файл.", ButtonType.OK);
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "У вас нет прав на загрузку файлов на сервер!!!\n Для изменения прав обратитесь к администратору!", ButtonType.OK);
                    alert.showAndWait();
                }
            }
            localFileTable.requestFocus();
        }

    }

    @FXML
    public void clickbtnCloudToLocalCopy(MouseEvent mouseEvent) {
        if (serverFileTable.isFocused()) {
            if (serverList.size() > 0) {
                String selectFile;
                boolean isSelected = false;
                selectFile = serverFileTable.getSelectionModel().getSelectedItem().getFileName();
                for (FileData f : localList) {
                    if (selectFile.equals(f.getFileName())) {
                        isSelected = true;
                    }
                }
                if (serverFileTable.getSelectionModel().getSelectedItem().getFileType().equals("FILE")) {
                    if (!isSelected) {
                        DownloadFileService downloadFileService;
                        downloadFileService = ObjectRegistry.getInstance(DownloadFileService.class);
                        downloadFileService.sendRequest(selectFile, currentServerPath);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Копирование невозможно!!!\nВ локальной папке уже имеется указанный файл.", ButtonType.OK);
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "У вас нет прав на загрузку папок c сервера!!!\n Для изменения прав обратитесь к администратору!", ButtonType.OK);
                    alert.showAndWait();
                }
            }
            serverFileTable.requestFocus();
        }

    }

    @FXML
    public void clickbtnDeleteFile(MouseEvent mouseEvent) {
        String fileForDelete;
        if (localFileTable.isFocused() && localList.size()>0) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Вы уверены, что хотите удалить?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                fileForDelete = localFileTable.getSelectionModel().getSelectedItem().getFileName();
                ObjectRegistry.getInstance(DeleteFileService.class).deleteFileFromLocalDir(fileForDelete, userRights);
                updateLocalTable(Paths.get(localPathField.getText()));
            }
            localFileTable.requestFocus();
        }
        if (serverFileTable.isFocused() && serverList.size()>0) {
            if (userRights.equals("full")) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Вы уверены, что хотите удалить?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                    fileForDelete = serverFileTable.getSelectionModel().getSelectedItem().getFileName();
                    ObjectRegistry.getInstance(DeleteFileService.class).deleteFileFromCloud(fileForDelete, currentServerPath, userRights, token);
                } else System.out.println("Ну нет, так нет...");
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "У вас нет прав на удаление файлов/папок в удаленном хранилище!!!\n Для изменения разрешений обратитесь к администратору!", ButtonType.OK);
                alert.showAndWait();
            }
            serverFileTable.requestFocus();
        }
    }

    @FXML
    public void clckServerFileTable(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            if (serverFileTable.getSelectionModel().getSelectedItem().getFileType().equals("DIR")) {
                currentServerPath = currentServerPath + "//" + serverFileTable.getSelectionModel().getSelectedItem().getFileName();
                GetFilesListRequest getFilesListRequest = new GetFilesListRequest(token, currentServerPath);
                ObjectRegistry.getInstance(NetworkNetty.class).sendGetFileListRequest(getFilesListRequest);
                Path path = Paths.get(currentServerPath).normalize();
                serverPathField.setText("\\" + path);
            }
        }
        serverFileTable.requestFocus();
    }

    @FXML
    public void btnUpServerDirectory(MouseEvent mouseEvent) {
        if (!currentServerPath.equals(userDir)) {
            int ii = currentServerPath.lastIndexOf("//");
            currentServerPath = currentServerPath.substring(0, ii);
            GetFilesListRequest getFilesListRequest = new GetFilesListRequest(token, currentServerPath);
            ObjectRegistry.getInstance(NetworkNetty.class).sendGetFileListRequest(getFilesListRequest);
            Path path = Paths.get(currentServerPath).normalize();
            serverPathField.setText("\\" + path);
        }
        serverFileTable.requestFocus();
    }

    public void updateUsedSpaceProgressBar(long userQuote, long usedSpace) {
        final int BYTES_PER_MEGABYTE = 1024 * 1024;
        freeSpaceUserDir = userQuote * BYTES_PER_MEGABYTE - usedSpace;
        double progressUsed = usedSpace * 1.0 / (userQuote * BYTES_PER_MEGABYTE);
        serverQwoteProgress.setProgress(progressUsed);
        String labelText = "Использовано " + Math.round(progressUsed * 100) + "% (" + Math.round(usedSpace / BYTES_PER_MEGABYTE) + "Mб/" + userQuote + "Мб)";
        freeSpaceProgressLabel.setText(labelText);
    }

    @FXML
    public void selectLocalDisk(ActionEvent actionEvent) {
        ComboBox<String> comboBox = (ComboBox<String>) actionEvent.getSource();
        updateLocalTable(Paths.get(comboBox.getSelectionModel().getSelectedItem()));
        localFileTable.requestFocus();
    }

    @FXML
    public void clickbtnCreateDirectory(MouseEvent mouseEvent) {
        CreateDirService createDirService = ObjectRegistry.getInstance(CreateDirService.class);
        String result;
        if (localFileTable.isFocused()) {
            result = createDirService.createDirectoryDialog(mainStage);
            if (!result.equals("")) {
                createDirService.creatLocalDirectory(localPathField.getText(), result);
                Path path = Path.of(localPathField.getText());
                updateLocalTable(path);
            }
            localFileTable.requestFocus();
            return;
        }
        if (userRights.equals("full")) {
            if (serverFileTable.isFocused()) {
                result = createDirService.createDirectoryDialog(mainStage);
                if (!result.equals("")) {
                    createDirService.createServerDirectory(token, currentServerPath, result);
                    System.out.println("clickbtnCreateDirectory " + token + " " + currentServerPath + " " + result);
                    GetFilesListRequest getFilesListRequest = new GetFilesListRequest(token, currentServerPath);
                    ObjectRegistry.getInstance(NetworkNetty.class).sendGetFileListRequest(getFilesListRequest);
                }
                serverFileTable.requestFocus();
            }
        } else {
            if (userRights.equals("ro")) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "У Вас нет прав на создание папок в удаленном хранилище!\nДля изменения режима доступа обратитесь к администратору.", ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

    public String getUserRights() {
        return userRights;
    }
}