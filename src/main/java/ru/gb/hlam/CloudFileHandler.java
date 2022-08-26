package ru.gb.hlam;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.gb.hlam.CloudMessage;
import ru.gb.hlam.FileMessage;
import ru.gb.hlam.FileRequest;
import ru.gb.hlam.ListFiles;

import java.nio.file.Files;
import java.nio.file.Path;

public class CloudFileHandler extends SimpleChannelInboundHandler<CloudMessage> {
    private Path currentDir;

    public CloudFileHandler(){
        currentDir=Path.of("test2");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(new ListFiles(currentDir));
    }

    protected void channelRead0(ChannelHandlerContext ctx, CloudMessage cloudMessage) throws Exception {
        if (cloudMessage instanceof FileRequest fileRequest){
           ctx.writeAndFlush(new FileMessage(currentDir.resolve(fileRequest.getName())));
        } else if (cloudMessage instanceof FileMessage fileMessage){
           Files.write(currentDir.resolve(fileMessage.getName()),fileMessage.getData());
           ctx.writeAndFlush(new ListFiles(currentDir));
        }
    }
}
