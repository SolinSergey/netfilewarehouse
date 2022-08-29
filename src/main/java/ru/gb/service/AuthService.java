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
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.gb.cloudmessages.AuthRequest;
import ru.gb.netfilewarehouse.NetworkNetty;
import ru.gb.netfilewarehouse.ObjectRegistry;

public class AuthService {
    private String authToken;
    private boolean isGetAuthResponse;
    private String userName="";
    private String userPassword="";
    public void sendAuthRequest(String userName, String userPassword){
        isGetAuthResponse = false;
        AuthRequest authRequest = new AuthRequest(userName,userPassword);
        ObjectRegistry.getInstance(NetworkNetty.class).sendAuthRequest(authRequest);
    }

    public boolean auth(String token) {

        return ObjectRegistry.getInstance(CryptService.class).getUserToken().equals(token);
    }

    public String getAuthToken (){
        return this.authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
        System.out.println("Сохраненный токен: "+this.authToken);
        isGetAuthResponse=true;
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
                userName = loginArea.getText();
                userPassword = ObjectRegistry.getInstance(CryptService.class).getCryptString(passwordArea.getText());
                dialogAuth.close();
            }
        });
        GridPane.setHalignment(okButton, HPos.CENTER);
        pane.add(okButton,1,4);

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

    public void setGetAuthResponse(boolean getAuthResponse) {
        isGetAuthResponse = getAuthResponse;
    }



}
