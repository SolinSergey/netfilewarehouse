package ru.gb.service;

import ru.gb.cloudmessages.AuthRequest;
import ru.gb.netfilewarehouse.NetworkNetty;
import ru.gb.netfilewarehouse.ObjectRegistry;

public class AuthService {

    public void sendAuthRequest(String userName, String userPassword){
        AuthRequest authRequest = new AuthRequest(userName,userPassword);
        ObjectRegistry.getInstance(NetworkNetty.class).sendAuthRequest(authRequest);
    }
    public boolean auth(String token) {
        return ObjectRegistry.getInstance(CryptService.class).getUserToken().equals(token);
    }
}
