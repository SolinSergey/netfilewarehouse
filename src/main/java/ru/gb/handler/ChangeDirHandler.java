package ru.gb.handler;

import io.netty.channel.ChannelHandlerContext;
import ru.gb.cloudmessages.ChangeDirRequest;
import ru.gb.cloudmessages.GetFilesListResponse;

import java.io.IOException;

public class ChangeDirHandler implements RequestHandler<ChangeDirRequest, GetFilesListResponse> {
    @Override
    public GetFilesListResponse handle(ChangeDirRequest request, ChannelHandlerContext channelHandlerContext) throws IOException {



        return null;
    }
}
