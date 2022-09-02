package ru.gb.handler;

import ru.gb.cloudmessages.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HandlerRegistry {

    private static final Map<Class<? extends BasicRequest>, RequestHandler<?, ?>> HANDLERS_MAP;

    static {
        Map<Class<? extends BasicRequest>, RequestHandler<?, ?>> handlerMap = new HashMap<>();
        handlerMap.put(GetFilesListRequest.class, new GetFilesListHandler());
        handlerMap.put(RegisterUserRequest.class, new RegisterUserHandler());
        handlerMap.put(UploadFileRequest.class, new UploadFileHandler());
        handlerMap.put(DownloadFileRequest.class, new DownloadFileHandler());
        handlerMap.put(AuthRequest.class, new AuthHandler());
        handlerMap.put(DeleteFileRequest.class, new DeleteFileHandler());
        handlerMap.put(ChangeDirRequest.class, new ChangeDirHandler());


        HANDLERS_MAP = Collections.unmodifiableMap(handlerMap);
    }

    public static RequestHandler<?, ?> getHandler(Class<? extends BasicRequest> request) {
        return HANDLERS_MAP.get(request);
    }

}
