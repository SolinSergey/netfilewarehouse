package ru.gb.handler;

import io.netty.channel.ChannelHandlerContext;
import ru.gb.cloudmessages.DownloadFileRequest;
import ru.gb.cloudmessages.DownloadFileResponse;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


public class DownloadFileHandler implements RequestHandler<DownloadFileRequest, DownloadFileResponse> {

    private static final String SERVER_PATH = System.getProperty("user.dir");
    private static final Map<ChannelHandlerContext, RandomAccessFile> FILE_DESCRIPTOR_MAP = new ConcurrentHashMap<>();

    @Override
    public DownloadFileResponse handle(DownloadFileRequest request, ChannelHandlerContext channelHandlerContext) {
        System.out.println("*********************************");
        String fileName = request.getFileName();
        System.out.println("Файл для отправки на локаль: "+ fileName);
        Path path = Paths.get(SERVER_PATH+ "//"+request.getUserDir()+"//"+fileName.toString());
        System.out.println("Путь отправки на локаль:" + path.toString());
        byte[] filePartData;
        try {
            filePartData = Files.readAllBytes(path);
            DownloadFileResponse downloadFileResponse = new DownloadFileResponse("",request.getAuthToken(),fileName,filePartData);
            System.out.println("DownloadFileResponse Отправлен: " + downloadFileResponse.toString());
            return downloadFileResponse;
        } catch (IOException e) {
            return new DownloadFileResponse("Не удалось передать файл", request.getAuthToken(), request.getFileName(), null);
        }
    }


    public List<String> getList() throws IOException {
        Path path = Paths.get(SERVER_PATH + "//test2//");
        List<String> files;
        files = Files.list(path)
                .map(p -> p.getFileName().toString())
                .collect(Collectors.toList());
        System.out.println(files);
        return files;
    }

}

