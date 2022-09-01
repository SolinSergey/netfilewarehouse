package ru.gb.handler;

import io.netty.channel.ChannelHandlerContext;
import ru.gb.cloudmessages.AuthRequest;
import ru.gb.cloudmessages.AuthResponse;
import ru.gb.service.DAO;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;

public class AuthHandler implements RequestHandler<AuthRequest, AuthResponse> {
    private String token;

    String userDir;
    String userRights;
    @Override
    public AuthResponse handle(AuthRequest request, ChannelHandlerContext channelHandlerContext) throws IOException {
        DAO dao = new DAO();
        AuthResponse authResponse;
        if (getCryptString(request.getPassword()).equals(dao.getUserPasswordFromDB(request.getUsername()))) {
            token = request.getUsername() + ":" + getCryptString(request.getPassword());
            userDir=dao.getUserWorkDirFromDB(request.getPassword());
            userRights= dao.getUserRightsFromDB(request.getPassword());
            authResponse = new AuthResponse("", token,userDir,userRights);
        } else {
            authResponse = new AuthResponse("Пользователь не найден!!!", "NotAutorized","NotAutorized","NotAutorized");
        }
        //System.out.println("AuthResponse подготовлен: " + authResponse.getErrorMessage() + authResponse.getAuthToken());
        return authResponse;
    }
    public String getCryptString(String stringToCrypt){
        String toReturn = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(stringToCrypt.getBytes("utf8"));
            toReturn = String.format("%064x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return toReturn;
    }

}
