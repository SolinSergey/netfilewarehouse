package ru.gb.handler;


import io.netty.channel.ChannelHandlerContext;
import ru.gb.cloudmessages.AuthRequest;
import ru.gb.cloudmessages.AuthResponse;
import ru.gb.service.DAO;

import java.io.IOException;

public class AuthHandler implements RequestHandler<AuthRequest, AuthResponse> {
    @Override
    public AuthResponse handle(AuthRequest request, ChannelHandlerContext channelHandlerContext) throws IOException {
        DAO dao = new DAO();
        AuthResponse authResponse;
        if (request.getPassword().equals(dao.getUserPasswordFromDB(request.getUsername()))) {
            String token = request.getUsername() + ":" + request.getPassword();
            authResponse = new AuthResponse("", token);
        } else {
            authResponse = new AuthResponse("Пользователь не найден!!!", "NotAutorized");
        }
        System.out.println("AuthResponse подготовлен: " + authResponse.getErrorMessage() + authResponse.getAuthToken());
        return authResponse;
    }
}
