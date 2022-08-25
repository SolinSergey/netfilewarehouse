package ru.gb.netfilewarehouse;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import ru.gb.cloudmessages.FileInfo;
import ru.gb.cloudmessages.ListFiles;
import ru.gb.transferobjects.GetFilesListRequest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class NetFileWarehouseController implements Initializable {
    private static final NetFileWarehouseController INSTANCE = new NetFileWarehouseController();
    public static NetFileWarehouseController getInstance(){
        return INSTANCE;
    }
    private static final int MAX_SIZE_OBJECT = 1_000_000_000;
    public static Stage mainStage;
    @FXML
    public TableView<FileInfo> filesTableLocal;
    public TableView <FileInfo> filesTableServer;
    public ComboBox disksBox;
    public TextField pathField;
    public Button btnLocalToCloudCopy;
    public Channel channel;
    public ListView <String> localListView;
    public ListView <String> serverListView;

    ByteBuf buffer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initNetClient();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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

        disksBox.getItems().clear();
        for (Path p: FileSystems.getDefault().getRootDirectories()){
            disksBox.getItems().add(p.toString());
        }
        disksBox.getSelectionModel().select(0);

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
    public void initServerPanel(List<String> files){

        //serverListView.getItems().clear();
        //serverListView.getItems().addAll(files);
        //serverListView.getItems().sorted();

    }
    public void initLocalPanel() throws IOException {
        Path path=Paths.get(".//test");
        List<String> files;
        files = Files.list(path)
                .map(p -> p.getFileName().toString())
                .collect(Collectors.toList());

        localListView.getItems().clear();
        localListView.getItems().addAll(files);
        localListView.getItems().sorted();




        //localListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


      /*  TableColumn<FileInfo,String> fileTypeColumn = new TableColumn<>();
        fileTypeColumn.setCellValueFactory(param->new SimpleStringProperty(param.getValue().getType().getName()));
        fileTypeColumn.setPrefWidth(20);

        TableColumn<FileInfo,String> filenameColumn = new TableColumn<>("Имя");
        filenameColumn.setCellValueFactory(param->new SimpleStringProperty(param.getValue().getFilename()));
        filenameColumn.setPrefWidth(267);
        filenameColumn.setResizable(false);

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
        filesTableLocal.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);*/
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

    public void selectDiskAction(ActionEvent actionEvent) {
        ComboBox <String> element = (ComboBox<String>) actionEvent.getSource();
        updateList(Paths.get(element.getSelectionModel().getSelectedItem()));
    }

    public void onMouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount()==2){
            Path path=Paths.get(pathField.getText()).resolve(filesTableLocal.getSelectionModel().getSelectedItem().getFilename());
            if (Files.isDirectory(path)){
                updateList(path);
            }
        }
    }

    public String getSelectedFilename(){
        if (!filesTableLocal.isFocused()){
            return null;
        }
        return filesTableLocal.getSelectionModel().getSelectedItem().getFilename();
    }

    public String getCurrentPath(){
        return pathField.getText();
    }


    public void clickbtnLocalToCloudCopy(MouseEvent mouseEvent) {
        System.out.println("Копировать в облако");
        GetFilesListRequest getFilesListRequest = new GetFilesListRequest("Bogdan:1234", "/");
        channel.writeAndFlush(getFilesListRequest);

    }

    public void initNetClient() throws InterruptedException {

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .remoteAddress("localhost", 8189)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(
                                new ObjectDecoder(MAX_SIZE_OBJECT, ClassResolvers.cacheDisabled(null)),
                                new ObjectEncoder(),
                                new ClientHandler()
                        );
                    }
                });
        try {
            ChannelFuture channelFuture = bootstrap.connect().sync();
            channel = channelFuture.channel();
            buffer = channel.alloc().buffer();
            //channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }



    }
}