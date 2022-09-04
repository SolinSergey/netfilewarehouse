package ru.gb.cloudmessages;

public class AuthRequest extends BasicRequest {

    private final String username;

    private final String password;

    public AuthRequest(String username, String password) {
        super(null);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
