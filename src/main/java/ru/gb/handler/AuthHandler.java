package ru.gb.handler;

import io.netty.channel.ChannelHandlerContext;
import ru.gb.cloudmessages.AuthRequest;
import ru.gb.cloudmessages.AuthResponse;
import ru.gb.service.DAO;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.SQLException;

public class AuthHandler implements RequestHandler<AuthRequest, AuthResponse> {
    private String token;
    String userDir;
    String userRights;
    long userQuote;

    @Override
    public AuthResponse handle(AuthRequest request, ChannelHandlerContext channelHandlerContext) throws IOException {
        DAO dao = new DAO();
        AuthResponse authResponse;
        try {
            if (getCryptString(request.getPassword()).equals(dao.getUserPasswordFromDB(request.getUsername()))) {
                token = request.getUsername() + ":" + getCryptString(request.getPassword());
                userDir = dao.getUserWorkDirFromDB(request.getUsername());
                userRights = dao.getUserRightsFromDB(request.getUsername());
                userQuote = dao.getUserQuoteFromDB(request.getUsername());
                authResponse = new AuthResponse("", token, userDir, userRights, userQuote);
            } else {
                authResponse = new AuthResponse("Пользователь не найден!!!", "NotAutorized", "NotAutorized", "NotAutorized", 0);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return authResponse;
    }

    public String getCryptString(String stringToCrypt) {
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
