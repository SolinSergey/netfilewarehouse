package ru.gb.cloudmessages;

import ru.gb.netfilewarehouse.FileData;

import java.util.List;

public class GetFilesListResponse extends BasicResponse {
    private final List<FileData> list;

    public List<FileData> getList() {
        return list;
    }

    public GetFilesListResponse(String errorMessage, String authToken, List<FileData> list) {
        super(errorMessage, authToken);
        this.list = list;
    }


}
