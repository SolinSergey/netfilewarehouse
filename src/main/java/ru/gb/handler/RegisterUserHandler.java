package ru.gb.handler;

import io.netty.channel.ChannelHandlerContext;
import ru.gb.cloudmessages.RegisterUserRequest;
import ru.gb.cloudmessages.RegisterUserResponse;

public class RegisterUserHandler implements RequestHandler<RegisterUserRequest, RegisterUserResponse> {

    @Override
    public RegisterUserResponse handle(RegisterUserRequest request, ChannelHandlerContext context) {
        //... логика регистрации
        return new RegisterUserResponse("OK");
    }
}