package ru.gb.cloudmessages;

import java.nio.file.Path;
import java.util.List;

public class GetFilesListResponse extends BasicResponse {

    private final List list;

    public List<String> getList() {
        return list;
    }

    public GetFilesListResponse(String errorMessage, String authToken, List<String> list) {
        super(errorMessage,authToken);
        this.list = list;
    }


}
