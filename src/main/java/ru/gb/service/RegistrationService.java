package ru.gb.service;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.gb.cloudmessages.RegisterUserRequest;
import ru.gb.netfilewarehouse.NetworkNetty;
import ru.gb.netfilewarehouse.ObjectRegistry;

public class RegistrationService {
    private String userName;
    private String userPassword;
    private String registrationStatus = "bad";
    public void sendRegistrationRequest (String userName, String userPassword){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest(userName,userPassword);
        ObjectRegistry.getInstance(NetworkNetty.class).sendRegisterUserRequest(registerUserRequest);
    }
    public void showRegistrationDialog(Stage stage) {
        Stage dialogReg = new Stage();
        dialogReg.setResizable(false);
        Pane pane = new Pane();
        Scene dialogScene = new Scene(pane, 300, 400);
        dialogReg.setScene(dialogScene);
        dialogScene.getStylesheets().add("my.css");
        dialogReg.setTitle("Регистрация");
        dialogReg.initOwner(stage);
        dialogReg.initModality(Modality.APPLICATION_MODAL);

        Image image = new Image(AuthService.class.getResourceAsStream("/icons/cloud.png"));
        dialogReg.getIcons().add(image);

        Label label = new Label("Зарегистрируйтесь:");
        label.setPrefSize(223, 25);
        label.setLayoutX(38);
        label.setLayoutY(48);
        label.setAlignment(Pos.CENTER);
        label.fontProperty().setValue(new Font("System", 15));
        label.setTextFill(Color.color(1, 1, 1));
        pane.getChildren().add(label);

        TextField loginArea = new TextField();
        loginArea.setPromptText("Введите логин");
        loginArea.setPrefSize(186, 31);
        loginArea.setLayoutX(55);
        loginArea.setLayoutY(101);
        loginArea.fontProperty().setValue(new Font("System", 15));
        pane.getChildren().add(loginArea);

        PasswordField passwordArea = new PasswordField();
        passwordArea.setPromptText("Введите пароль");
        passwordArea.setPrefSize(186, 31);
        passwordArea.setLayoutX(55);
        passwordArea.setLayoutY(159);
        passwordArea.fontProperty().setValue(new Font("System", 15));
        pane.getChildren().add(passwordArea);

        PasswordField passwordConfirmArea = new PasswordField();
        passwordConfirmArea.setPromptText("Повторите пароль");
        passwordConfirmArea.setPrefSize(186, 31);
        passwordConfirmArea.setLayoutX(55);
        passwordConfirmArea.setLayoutY(219);
        passwordConfirmArea.fontProperty().setValue(new Font("System", 15));
        pane.getChildren().add(passwordConfirmArea);

        Button okButton = new Button("OK");
        okButton.setPrefSize(75, 35);
        okButton.setLayoutX(65);
        okButton.setLayoutY(290);
        okButton.textAlignmentProperty().setValue(TextAlignment.CENTER);
        okButton.fontProperty().setValue(new Font("System", 15));
        okButton.setStyle("-fx-background-color:  #E6D6F1FF");
        pane.getChildren().add(okButton);

        Button cancelButton = new Button("Отмена");
        cancelButton.setPrefSize(75, 35);
        cancelButton.setLayoutX(157);
        cancelButton.setLayoutY(290);
        cancelButton.textAlignmentProperty().setValue(TextAlignment.CENTER);
        cancelButton.fontProperty().setValue(new Font("System", 15));
        cancelButton.setStyle("-fx-background-color:  #E6D6F1FF");
        pane.getChildren().add(cancelButton);

        loginArea.setText("");
        passwordArea.setText("");
        passwordConfirmArea.setText("");
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (loginArea.getText().matches("[a-zA-z0-9]{1,10}")){
                    userName=loginArea.getText();
                    if (passwordArea.getText().length()>=3){
                        if (!passwordArea.getText().equals("") &&!passwordConfirmArea.getText().equals("") && passwordArea.getText().equals(passwordConfirmArea.getText())){
                            userPassword = passwordArea.getText();
                            sendRegistrationRequest(userName,userPassword);
                            do {

                            }while (registrationStatus.equals("success"));
                            dialogReg.close();
                        }
                        else {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Пароли не совпадают!\nПовторите ввод", ButtonType.OK);
                            alert.showAndWait();
                        }
                    }else{
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Заданный пароль имеет длину меньше 3-х символов!\nПовторите ввод", ButtonType.OK);
                        alert.showAndWait();
                    }
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Некорректно задан логин\nПовторите ввод (Латинские буквы и цыфры (до 10 символов)", ButtonType.OK);
                    alert.showAndWait();
                }
            }
        });
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                dialogReg.close();
            }
        });

        passwordArea.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                userName = loginArea.getText();
                userPassword = passwordArea.getText();
                dialogReg.close();
            }
        });
        loginArea.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                userName = loginArea.getText();
                userPassword = passwordArea.getText();
                dialogReg.close();
            }
        });
        dialogReg.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.exit(0);
            }
        });
        dialogReg.showAndWait();
    }
    public void setRegisterSuccess(String registrationStatus) {
        this.registrationStatus = registrationStatus;
    }
}
