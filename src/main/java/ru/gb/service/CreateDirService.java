package ru.gb.service;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.gb.cloudmessages.CreateDirRequest;
import ru.gb.netfilewarehouse.NetFileWarehouseController;
import ru.gb.netfilewarehouse.NetworkNetty;
import ru.gb.netfilewarehouse.ObjectRegistry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CreateDirService {
    private String result;

    public void creatLocalDirectory(String currentLocalDir, String name) {
        Path path = Path.of(currentLocalDir + "//" + name).normalize().toAbsolutePath();
        try {
            if (Files.notExists(path)) {
                Files.createDirectory(path);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Невозможно создать уже существующую папку!", ButtonType.OK);
                alert.showAndWait();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createServerDirectory(String userToken, String currentServerDir, String nameNewDir) {

            CreateDirRequest createDirRequest = new CreateDirRequest(userToken, nameNewDir, currentServerDir);
            ObjectRegistry.getInstance(NetworkNetty.class).sendCreateDirRequest(createDirRequest);


    }

    public String createDirectoryDialog(Stage mainStage) {
        Stage createDirectoryDialog = new Stage();
        createDirectoryDialog.setResizable(false);
        Pane pane = new Pane();
        Scene dialogScene = new Scene(pane, 400, 200);
        createDirectoryDialog.setScene(dialogScene);
        dialogScene.getStylesheets().add("my.css");
        createDirectoryDialog.setTitle("Ввод имени папки");
        createDirectoryDialog.initOwner(mainStage);
        createDirectoryDialog.initModality(Modality.APPLICATION_MODAL);
        Image image = new Image(AuthService.class.getResourceAsStream("/icons/cloud.png"));
        createDirectoryDialog.getIcons().add(image);

        Label label = new Label("Введите имя папки");
        label.setPrefSize(302, 17);
        label.setLayoutX(43);
        label.setLayoutY(28);
        label.textAlignmentProperty().setValue(TextAlignment.LEFT);
        label.fontProperty().setValue(new Font("System", 16));
        label.setTextFill(Color.color(1, 1, 1));
        pane.getChildren().add(label);

        TextField nameDir = new TextField("Имя папки");
        nameDir.setPrefSize(315, 25);
        nameDir.setLayoutX(43);
        nameDir.setLayoutY(75);
        nameDir.fontProperty().setValue(new Font("System", 16));
        pane.getChildren().add(nameDir);

        Button okButton = new Button("OK");
        okButton.setPrefSize(85, 38);
        okButton.setLayoutX(89);
        okButton.setLayoutY(141);
        okButton.textAlignmentProperty().setValue(TextAlignment.CENTER);
        okButton.fontProperty().setValue(new Font("System", 16));
        okButton.setStyle("-fx-background-color:  #E6D6F1FF");
        pane.getChildren().add(okButton);

        Button cancelButton = new Button("Отмена");
        cancelButton.setPrefSize(85, 38);
        cancelButton.setLayoutX(222);
        cancelButton.setLayoutY(141);
        cancelButton.textAlignmentProperty().setValue(TextAlignment.CENTER);
        cancelButton.fontProperty().setValue(new Font("System", 16));
        cancelButton.setStyle("-fx-background-color:  #E6D6F1FF");
        pane.getChildren().add(cancelButton);
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                result = nameDir.getText();
                createDirectoryDialog.close();
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                result = "";
                createDirectoryDialog.close();
            }
        });
        nameDir.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                result = nameDir.getText();
                createDirectoryDialog.close();
            }
        });
        createDirectoryDialog.showAndWait();
        return result;
    }
}
