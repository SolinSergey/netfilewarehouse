package ru.gb.netfilewarehouse;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class NetFileWarehouseController implements Initializable {
    public static Stage mainStage;
    @FXML
    public TableView<FileInfo> filesTableLocal;
    public ComboBox disksBox;
    public TextField pathField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("in Init");
        showAuthDialog(mainStage);

        TableColumn<FileInfo,String> fileTypeColumn = new TableColumn<>();
        fileTypeColumn.setCellValueFactory(param->new SimpleStringProperty(param.getValue().getType().getName()));
        fileTypeColumn.setPrefWidth(20);

        TableColumn<FileInfo,String> filenameColumn = new TableColumn<>("Имя");
        filenameColumn.setCellValueFactory(param->new SimpleStringProperty(param.getValue().getFilename()));
        filenameColumn.setPrefWidth(267);

        TableColumn<FileInfo,Long> fileSizeColumn=new TableColumn<>("Размер");
        fileSizeColumn.setCellValueFactory(param->new SimpleObjectProperty<>(param.getValue().getSize()));
        fileSizeColumn.setPrefWidth(141);
        fileSizeColumn.setCellFactory(column->{
            return new TableCell<FileInfo,Long>(){
                @Override
                protected void updateItem(Long item,boolean empty){
                    if (item==null||empty){
                        setText(null);
                        setStyle("");
                    } else {
                        String text=String.format("%,d bytes",item);
                        if (item==-1L){
                            text="[DIR]";
                        }
                        setText(text);
                    }
                }
            };
        });

        filesTableLocal.getColumns().addAll(fileTypeColumn,filenameColumn,fileSizeColumn);
        filesTableLocal.getSortOrder().add(fileTypeColumn);

        disksBox.getItems().clear();
        for (Path p: FileSystems.getDefault().getRootDirectories()){
            disksBox.getItems().add(p.toString());
        }
        disksBox.getSelectionModel().select(0);

        updateList(Paths.get("."));

    }

    public void updateList (Path path){
        try {
            pathField.setText(path.normalize().toAbsolutePath().toString());
            filesTableLocal.getItems().clear();
            filesTableLocal.getItems().addAll(Files.list(path).map(FileInfo::new).collect(Collectors.toList()));
            filesTableLocal.sort();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING,"Не удалось обновить список файлов",ButtonType.OK);
            alert.showAndWait();
        }

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
           updateList(upperPath);
       }
    }
}