package ru.gb.netfilewarehouse;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class NetFileWarehouse extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        stage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(NetFileWarehouse.class.getResource("nfw_simple.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 960);
        stage.setTitle("Моё облако");
        stage.setScene(scene);
        NetFileWarehouseController.mainStage = stage;
        Image image = new Image(NetFileWarehouse.class.getResourceAsStream("/icons/cloud.png"));
        stage.getIcons().add(image);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                try {
                    System.exit(0);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    public static void main(String[] args) {
        launch();
    }
}