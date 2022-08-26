package ru.gb.netfilewarehouse;

import ru.gb.service.AuthService;
import ru.gb.service.UploadFileService;

import java.util.HashMap;
import java.util.Map;

public class ObjectRegistry {
    private static final Map<Class<?>,Object> INSTANCE_REGISTRY = new HashMap<>();

    static {
        NetworkNetty networkNetty = new NetworkNetty();
        reg(NetworkNetty.class,networkNetty);

        UploadFileService uploadFileService = new UploadFileService();
        reg(UploadFileService.class, uploadFileService);

        AuthService authService = new AuthService();
        reg(AuthService.class, authService);
    }

    public static void reg(Class<?> clazz,Object instance){
        INSTANCE_REGISTRY.put(clazz,instance);
    }

    public static <T> T getInstance(Class <T> clazz){
        return(T) INSTANCE_REGISTRY.get(clazz);
    }
}
