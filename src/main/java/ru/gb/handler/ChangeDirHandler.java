package ru.gb.handler;

import io.netty.channel.ChannelHandlerContext;
import ru.gb.cloudmessages.CheckUsedSpaceRequest;
import ru.gb.cloudmessages.GetFilesListResponse;

import java.io.IOException;

public class ChangeDirHandler implements RequestHandler<CheckUsedSpaceRequest, GetFilesListResponse> {
    @Override
    public GetFilesListResponse handle(CheckUsedSpaceRequest request, ChannelHandlerContext channelHandlerContext) throws IOException {



        return null;
    }
}
