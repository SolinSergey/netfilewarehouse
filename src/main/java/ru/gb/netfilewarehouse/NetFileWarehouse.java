package ru.gb.netfilewarehouse;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.gb.service.AuthService;

import java.io.IOException;

public class NetFileWarehouse extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(NetFileWarehouse.class.getResource("nfw_simple.fxml"));

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