package ru.gb.netfilewarehouse;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class NetFileWarehouse extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(NetFileWarehouse.class.getResource("nfw.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 960);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        NetFileWarehouseController.mainStage=stage;
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}