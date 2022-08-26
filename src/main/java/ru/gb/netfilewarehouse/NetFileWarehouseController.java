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
import ru.gb.service.UploadFileService;

import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static ru.gb.netfilewarehouse.NetworkNetty.TOKEN;

public class NetFileWarehouseController implements Initializable {


    private static final int MAX_SIZE_OBJECT = 1_000_000_000;
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

        System.out.println("in Init");

        //showAuthDialog(mainStage);
        //initLocalPanel();
        //initServerPanel();
        try {
            initLocalPanel();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //try {
        //    getList();
        //} catch (IOException e) {
        //    throw new RuntimeException(e);
        //}

        //disksBox.getItems().clear();
        //for (Path p: FileSystems.getDefault().getRootDirectories()){
        //    disksBox.getItems().add(p.toString());
        //}
        //disksBox.getSelectionModel().select(0);

        //updateList(Paths.get("."));


    }
    public void getList() throws IOException {
        Path path=Paths.get("/");
        List<String> files;
        files = Files.list(path)
                .map(p -> p.getFileName().toString())
                .collect(Collectors.toList());
        System.out.println(files);

    }

    public void initLocalPanel() throws IOException {
        Path path=Paths.get(System.getProperty("user.dir")+"//test");
        List<String> files;
        files = Files.list(path)
                .map(p -> p.getFileName().toString())
                .collect(Collectors.toList());
        updateLocalList(files);

    }
    public void updateServerList(List listFiles){
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
    public void showAuthDialog(Stage stage) {
        Stage dialogAuth = new Stage();
        dialogAuth.setResizable(false);
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(20));
        pane.setHgap(100);
        pane.setVgap(25);
        //pane.setAlignment(Pos.CENTER);
        Scene dialogScene = new Scene(pane,400,400);
        dialogAuth.setScene(dialogScene);
        dialogAuth.setTitle("Authorization");
        dialogAuth.initOwner(stage);
        dialogAuth.initModality(Modality.APPLICATION_MODAL);

        Label label = new Label("Авторизуйтесь:");
        GridPane.setHalignment(label, HPos.CENTER);
        pane.add(label,1,0);

        TextField loginArea = new TextField("Введите логин");
        //loginArea.setPromptText("Введите логин");
        pane.add(loginArea,1,2);

        PasswordField passwordArea = new PasswordField();
        passwordArea.setPromptText("Введите пароль");
        pane.add(passwordArea,1,3);

        Button okButton = new Button("OK");
        okButton.setPrefSize(60,20);
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println(loginArea.getText());
                System.out.println(passwordArea.getText());
                dialogAuth.close();
            }
        });
        GridPane.setHalignment(okButton, HPos.CENTER);
        pane.add(okButton,1,4);

        dialogAuth.showAndWait();
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
        GetFilesListRequest getFilesListRequest = new GetFilesListRequest(TOKEN,"");
        //channel.writeAndFlush(getFilesListRequest);

    }

}