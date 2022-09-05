package ru.gb.service;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.gb.cloudmessages.AuthRequest;
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

    public String getAuthToken() {
        return this.authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
        isGetAuthResponse = true;
    }

    public void showAuthDialog(Stage stage) {
        Stage dialogAuth = new Stage();
        dialogAuth.setResizable(false);
        Pane pane = new Pane();
        Scene dialogScene = new Scene(pane, 300, 400);
        dialogAuth.setScene(dialogScene);
        dialogAuth.setTitle("Авторизация");
        dialogAuth.initOwner(stage);
        dialogAuth.initModality(Modality.APPLICATION_MODAL);
        Image image = new Image(AuthService.class.getResourceAsStream("/icons/cloud.png"));
        dialogAuth.getIcons().add(image);

        Label label = new Label("Авторизуйтесь:");
        label.setPrefSize(223, 25);
        label.setLayoutX(38);
        label.setLayoutY(48);
        label.setAlignment(Pos.CENTER);
        label.fontProperty().setValue(new Font("System", 15));
        pane.getChildren().add(label);


        TextField loginArea = new TextField();
        loginArea.setPromptText("Введите логин");
        loginArea.setPrefSize(186, 31);
        loginArea.setLayoutX(55);
        loginArea.setLayoutY(103);
        loginArea.fontProperty().setValue(new Font("System", 15));
        pane.getChildren().add(loginArea);

        PasswordField passwordArea = new PasswordField();
        passwordArea.setPromptText("Введите пароль");
        passwordArea.setPrefSize(186, 31);
        passwordArea.setLayoutX(55);
        passwordArea.setLayoutY(169);
        passwordArea.fontProperty().setValue(new Font("System", 15));
        pane.getChildren().add(passwordArea);

        Button okButton = new Button("OK");
        okButton.setPrefSize(75, 35);
        okButton.setLayoutX(65);
        okButton.setLayoutY(245);
        okButton.textAlignmentProperty().setValue(TextAlignment.CENTER);
        okButton.fontProperty().setValue(new Font("System", 15));
        pane.getChildren().add(okButton);

        Button cancelButton = new Button("Отмена");
        cancelButton.setPrefSize(75, 35);
        cancelButton.setLayoutX(157);
        cancelButton.setLayoutY(245);
        cancelButton.textAlignmentProperty().setValue(TextAlignment.CENTER);
        cancelButton.fontProperty().setValue(new Font("System", 15));
        pane.getChildren().add(cancelButton);

        Button registrationButton = new Button("Регистрация");
        registrationButton.setPrefSize(167, 35);
        registrationButton.setLayoutX(65);
        registrationButton.setLayoutY(310);
        registrationButton.textAlignmentProperty().setValue(TextAlignment.CENTER);
        registrationButton.fontProperty().setValue(new Font("System", 15));
        pane.getChildren().add(registrationButton);
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                userName = loginArea.getText();
                userPassword = passwordArea.getText();
                dialogAuth.close();
            }
        });
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
               System.exit(0);
            }
        });
        registrationButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("регистрация");
                RegistrationService registrationService=ObjectRegistry.getInstance(RegistrationService.class);
                registrationService.showRegistrationDialog(dialogAuth);
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
        loginArea.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                userName = loginArea.getText();
                userPassword = passwordArea.getText();
                dialogAuth.close();
            }
        });
        dialogAuth.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
              System.exit(0);
            }
        });
        dialogAuth.showAndWait();
    }

    public String getUserName() {
        return userName;
    }


    public boolean isGetAuthResponse() {
        return isGetAuthResponse;
    }

    public void setUserDir(String userDir) {
        this.userDir = userDir;
    }

    public void setUserRights(String userRights) {
        this.userRights = userRights;
        isGetUserRights = true;
    }

    public String getUserDir() {
        return userDir;
    }

    public String getUserRights() {
        return userRights;
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
