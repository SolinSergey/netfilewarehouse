package ru.gb.handler;

import io.netty.channel.ChannelHandlerContext;
import ru.gb.cloudmessages.DownloadFileResponse;
import ru.gb.cloudmessages.UploadFileRequest;
import ru.gb.netfilewarehouse.NetFileWarehouseController;
import ru.gb.netfilewarehouse.NetworkNetty;
import ru.gb.netfilewarehouse.ObjectRegistry;
import ru.gb.service.AuthService;
import ru.gb.service.FileSaw;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class DownLoadFileHandler1 {
    private String currentServerDir;
    public void downloadFile(String userToken, String fileName, String currentDir, ChannelHandlerContext ctx) {
        Path filePath = Paths.get(System.getProperty("user.dir")+"//"+currentDir+"//"+fileName).normalize().toAbsolutePath();
        //currentServerDir = ObjectRegistry.getInstance(NetFileWarehouseController.class).serverPathField.getText();
        //NetworkNetty networkNetty=ObjectRegistry.getInstance(NetworkNetty.class);
        Consumer<byte[]> filePartConsumer= filePartBytes ->{
           // try {
                DownloadFileResponse downloadFileResponse = new DownloadFileResponse("", userToken,fileName,filePartBytes);
                System.out.println("ПАКЕТ!!!! " + ctx.writeAndFlush(downloadFileResponse));

          // } catch (InterruptedException e) {
           //     Thread.currentThread().interrupt();
           //    throw new RuntimeException(e);
          //  }
        };
        FileSaw fileSaw=new FileSaw();
        fileSaw.saw(filePath,filePartConsumer);



        //try{
        //System.out.println(filePath);
        //      byte[] allFileBytes = Files.readAllBytes(filePath);
        //System.out.println(filePath);
        //System.out.println(Arrays.toString(allFileBytes));
        //NetworkNetty networkNetty=ObjectRegistry.getInstance(NetworkNetty.class);
        //String fileName= filePath.getFileName().toString();
        //      UploadFileRequest uploadFileRequest = new UploadFileRequest(userToken,fileName,currentServerDir,allFileBytes);
        //     networkNetty.uploadFile(uploadFileRequest);
        // }catch (IOException e) {
        //     e.printStackTrace();
        //  }
    }
}
