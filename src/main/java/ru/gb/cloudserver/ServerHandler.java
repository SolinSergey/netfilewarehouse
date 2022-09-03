package ru.gb.cloudserver;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.gb.cloudmessages.*;
import ru.gb.handler.DownLoadFileHandler1;
import ru.gb.handler.HandlerRegistry;
import ru.gb.handler.RequestHandler;
import ru.gb.cloudmessages.BasicRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;


public class ServerHandler extends ChannelInboundHandlerAdapter {

    private String token;

    private static final String SERVER_PATH = System.getProperty("user.dir");

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws IOException {
        System.out.println(ctx.channel().remoteAddress());
        //GetFilesListResponse getFilesListResponse = new GetFilesListResponse("",getList());
        //ctx.writeAndFlush(getFilesListResponse);
    }
    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws IOException {
        BasicRequest request = (BasicRequest) msg;
        if (request instanceof AuthRequest){
            //System.out.println("Пришел запрос на авторизацию: "+ ((AuthRequest) request).getUsername() + " " + ((AuthRequest) request).getPassword());
            RequestHandler handler = HandlerRegistry.getHandler(request.getClass());
            BasicResponse response = handler.handle(request, channelHandlerContext);
            //System.out.println("AuthResponse отправлен: " + response.getErrorMessage() + response.getAuthToken());
            if (!response.getAuthToken().equals("NotAutorized")) token=response.getAuthToken();
            channelHandlerContext.writeAndFlush(response);
        }
        else{
            if (token.equals(request.getAuthToken()) && request instanceof DownloadFileRequest){
                DownLoadFileHandler1 downLoadFileHandler1=new DownLoadFileHandler1();
                downLoadFileHandler1.downloadFile(token,((DownloadFileRequest) request).getFileName(),((DownloadFileRequest) request).getUserDir(),channelHandlerContext);
            }

            if (token.equals(request.getAuthToken())){
                System.out.println(token);
                RequestHandler handler = HandlerRegistry.getHandler(request.getClass());
                BasicResponse response = handler.handle(request, channelHandlerContext);
                System.out.println("ОТПРАВЛЕН!!!" +  response.getClass().toString()+ " " + LocalTime.now().toString());
                channelHandlerContext.writeAndFlush(response);
                System.out.println("ОТПРАВЛЕН АГА!!!" +  response.getClass().toString()+ " " + LocalTime.now().toString());
            }

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }

    //public List<String> getList() throws IOException {
     //   Path path= Paths.get(SERVER_PATH+"//test2//");
    //    List<String> files;
    //    files = Files.list(path)
     //           .map(p -> p.getFileName().toString())
     //           .collect(Collectors.toList());
      //  System.out.println(files);
     //   return files;
   // }

   // public String getToken() {
     //   return token;
   // }

}


