package ru.gb.service;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.gb.cloudmessages.AuthRequest;
import ru.gb.netfilewarehouse.NetFileWarehouse;
import ru.gb.netfilewarehouse.NetworkNetty;
import ru.gb.netfilewarehouse.ObjectRegistry;

public class AuthService {
    private String authToken;
    private boolean isGetAuthResponse;

    private boolean isGetUserRights;

    private String userName = "";
    private String userPassword = "";
    private String userDir = "";

    private long userQuote = 0;

    private String userRights;

    public void sendAuthRequest() {
        isGetAuthResponse = false;
        AuthRequest authRequest = new AuthRequest(userName, userPassword);
        ObjectRegistry.getInstance(NetworkNetty.class).sendAuthRequest(authRequest);
    }

    public boolean auth(String token) {

        return ObjectRegistry.getInstance(CryptService.class).getUserToken().equals(token);
    }

    public String getAuthToken() {
        return this.authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
        //System.out.println("Сохраненный токен: "+this.authToken);
        isGetAuthResponse = true;
    }

    public void showAuthDialog(Stage stage) {
        Stage dialogAuth = new Stage();
        dialogAuth.setResizable(false);
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(20));
        pane.setHgap(100);
        pane.setVgap(25);
        //pane.setAlignment(Pos.CENTER);
        Scene dialogScene = new Scene(pane, 400, 400);
        dialogAuth.setScene(dialogScene);
        dialogAuth.setTitle("Authorization");
        dialogAuth.initOwner(stage);
        dialogAuth.initModality(Modality.APPLICATION_MODAL);
        Image image = new Image(AuthService.class.getResourceAsStream("/icons/cloud.png"));
        dialogAuth.getIcons().add(image);
        Label label = new Label("Авторизуйтесь:");
        GridPane.setHalignment(label, HPos.CENTER);
        pane.add(label, 1, 0);

        TextField loginArea = new TextField("Введите логин");
        //loginArea.setPromptText("Введите логин");
        pane.add(loginArea, 1, 2);

        PasswordField passwordArea = new PasswordField();
        passwordArea.setPromptText("Введите пароль");
        pane.add(passwordArea, 1, 3);

        Button okButton = new Button("OK");
        okButton.setPrefSize(60, 20);
        GridPane.setHalignment(okButton, HPos.CENTER);
        pane.add(okButton, 1, 4);
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //System.out.println(loginArea.getText());
                //System.out.println(passwordArea.getText());
                userName = loginArea.getText();
                userPassword = passwordArea.getText();
                dialogAuth.close();
            }
        });
        passwordArea.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                userName = loginArea.getText();
                userPassword = passwordArea.getText();
                dialogAuth.close();
            }
        });
        dialogAuth.showAndWait();
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public boolean isGetAuthResponse() {
        return isGetAuthResponse;
    }

    public void setUserDir(String userDir) {
        this.userDir = userDir;
    }

    public void setUserRights(String userRights) {
        this.userRights = userRights;
        //System.out.println("THIS AUTH SERVICE!!   "+ this.userRights);
        isGetUserRights = true;
    }

    public String getUserDir() {
        return userDir;
    }

    public String getUserRights() {
        return userRights;
    }

    public void setGetAuthResponse(boolean getAuthResponse) {
        isGetAuthResponse = getAuthResponse;
    }

    public boolean isGetUserRights() {
        return isGetUserRights;
    }

    public long getUserQuote() {
        return userQuote;
    }

    public void setUserQuote(long userQuote) {
        this.userQuote = userQuote;
    }


}
