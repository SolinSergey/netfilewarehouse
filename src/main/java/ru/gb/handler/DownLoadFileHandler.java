package ru.gb.handler;

import io.netty.channel.ChannelHandlerContext;
import ru.gb.cloudmessages.DownloadFileResponse;
import ru.gb.service.FileSaw;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class DownLoadFileHandler {
    public void downloadFile(String userToken, String fileName, String currentDir, ChannelHandlerContext ctx) {
        Path filePath = Paths.get(System.getProperty("user.dir") + "//" + currentDir + "//" + fileName).normalize().toAbsolutePath();
        Consumer<byte[]> filePartConsumer = filePartBytes -> {
            DownloadFileResponse downloadFileResponse = new DownloadFileResponse("", userToken, fileName, filePartBytes);
            ctx.writeAndFlush(downloadFileResponse);
        };
        FileSaw fileSaw = new FileSaw();
        fileSaw.saw(filePath, filePartConsumer);
    }
}
