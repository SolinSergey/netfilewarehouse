package ru.gb.cloudmessages;

public class RegisterUserRequest extends BasicRequest {

    private final String login;

    private final String password;

    public RegisterUserRequest(String login, String password) {
        super("NOPE");
        this.login = login;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }
}