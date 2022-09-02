package ru.gb.netfilewarehouse;

import io.netty.bootstrap.Bootstrap;
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
import ru.gb.cloudmessages.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class NetworkNetty {

    public static final int MAX_OBJECT_SIZE = 300 * 1_000_000;

    private final Channel clientChannel;

    public NetworkNetty() {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .remoteAddress("localhost", 8189)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline().addLast(
                                new ObjectDecoder(MAX_OBJECT_SIZE, ClassResolvers.cacheDisabled(null)),
                                new ObjectEncoder(),
                                new ClientHandler()
                        );
                    }
                });

        ChannelFuture channelFuture = bootstrap.connect();
        this.clientChannel = channelFuture.channel();
    }

    public Channel getClientChannel() {
        return clientChannel;
    }

    public void uploadFile(UploadFileRequest uploadFileRequest) {
        clientChannel.writeAndFlush(uploadFileRequest);
    }

    public void sendDownloadRequest(DownloadFileRequest downloadFileRequest) {
        //System.out.println("NettyNetwork.sendDownloadRequest    " + downloadFileRequest.getFileName());
        clientChannel.writeAndFlush(downloadFileRequest);
    }

    public void sendAuthRequest(AuthRequest authRequest){
        //System.out.println("NettyNetwork.sendAuthRequest    " + authRequest.getUsername() + " " + authRequest.getPassword());
        clientChannel.writeAndFlush(authRequest);
    }

    public void sendGetFileListRequest(GetFilesListRequest getFilesListRequest){
        System.out.println("NettyNetwork. "+getFilesListRequest.getClass().toString()+ " "+ LocalTime.now().toString());
        clientChannel.writeAndFlush(getFilesListRequest);
        System.out.println("Ушел из NetworkNetty");
    }

    public void sendDeleteFileRequest(DeleteFileRequest deleteFileRequest){
        clientChannel.writeAndFlush(deleteFileRequest);
    }
}