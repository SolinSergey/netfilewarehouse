package ru.gb.cloudmessages;

public class CheckUsedSpaceResponse extends BasicResponse {
    private final long usedSpaceInUserPath;

    public CheckUsedSpaceResponse(String errorMessage, String authToken, long usedSpaceInUserPath) {
        super(errorMessage, authToken);
        this.usedSpaceInUserPath = usedSpaceInUserPath;
    }

    public long getUsedSpaceInUserPath() {
        return usedSpaceInUserPath;
    }
}
