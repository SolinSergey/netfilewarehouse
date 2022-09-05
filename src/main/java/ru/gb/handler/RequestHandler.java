package ru.gb.handler;

import io.netty.channel.ChannelHandlerContext;
import ru.gb.cloudmessages.BasicRequest;
import ru.gb.cloudmessages.BasicResponse;
import ru.gb.cloudmessages.DownloadFileResponse;
import ru.gb.cloudmessages.UploadFileRequest;

import java.io.IOException;

public interface RequestHandler<REQUEST extends BasicRequest, RESPONSE extends BasicResponse> {
    RESPONSE handle(REQUEST request, ChannelHandlerContext channelHandlerContext) throws IOException;
}