package ru.gb.handler;


import io.netty.channel.ChannelHandlerContext;
import ru.gb.cloudmessages.AuthRequest;
import ru.gb.cloudmessages.AuthResponse;

import java.io.IOException;

public class AuthHandler implements RequestHandler<AuthRequest, AuthResponse>{
    @Override
    public AuthResponse handle(AuthRequest request, ChannelHandlerContext channelHandlerContext) throws IOException {
        return null;
    }
}
