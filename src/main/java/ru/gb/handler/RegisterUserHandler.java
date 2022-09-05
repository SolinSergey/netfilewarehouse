package ru.gb.handler;

import io.netty.channel.ChannelHandlerContext;
import ru.gb.cloudmessages.RegisterUserRequest;
import ru.gb.cloudmessages.RegisterUserResponse;
import ru.gb.service.DAO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

public class RegisterUserHandler implements RequestHandler<RegisterUserRequest, RegisterUserResponse> {
    private static final String SERVER_PATH = System.getProperty("user.dir");
    @Override
    public RegisterUserResponse handle(RegisterUserRequest request, ChannelHandlerContext channelHandlerContext) throws IOException {
        System.out.println("Запрос на регистрацию поступил на обработку");
        DAO dao = new DAO();
        String message="";
        try {
            if (dao.registerUserInDB(request.getLogin(), request.getPassword())){
                message="OK";
                Path path= Paths.get(SERVER_PATH+"//"+request.getLogin());
                if (Files.notExists(path)){
                    Files.createDirectory(path);
                }
            }
        } catch (SQLException e) {
            message="error";
            throw new RuntimeException(e);
        }
        RegisterUserResponse registerUserResponse=new RegisterUserResponse(message,"NOP");
        return registerUserResponse;
    }
}
