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
public class NetworkNetty {
    public static final int MAX_OBJECT_SIZE = 20_000_000;
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
        try {
            clientChannel.writeAndFlush(uploadFileRequest).sync();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    public void sendDownloadRequest(DownloadFileRequest downloadFileRequest) {
        clientChannel.writeAndFlush(downloadFileRequest);
    }

    public void sendAuthRequest(AuthRequest authRequest) {
        clientChannel.writeAndFlush(authRequest);
    }

    public void sendGetFileListRequest(GetFilesListRequest getFilesListRequest) {
        clientChannel.writeAndFlush(getFilesListRequest);

    }

    public void sendDeleteFileRequest(DeleteFileRequest deleteFileRequest) {
        clientChannel.writeAndFlush(deleteFileRequest);
    }

    public void sendCheckFreeSpaceRequest(CheckUsedSpaceRequest checkUsedSpaceRequest) {
        clientChannel.writeAndFlush(checkUsedSpaceRequest);
    }

    public void sendCreateDirRequest(CreateDirRequest createDirRequest) {
        clientChannel.writeAndFlush(createDirRequest);
    }

    public void sendRegisterUserRequest(RegisterUserRequest registerUserRequest) {
        clientChannel.writeAndFlush(registerUserRequest);
    }
}