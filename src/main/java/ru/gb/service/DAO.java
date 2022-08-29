package ru.gb.service;

import java.sql.*;

public class DAO {
    public Connection connection;
    public DAO() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:user_db/user_db.db");
        } catch (SQLException e) {
            System.out.println("При подключении к БД пользователей возникла проблема!!!");
        }
    }

    public String getUserPasswordFromDB(String userName){
        String userPassword=null;
        ResultSet resultSet;
        try {
            Statement stmt = connection.createStatement();
            String s = "SELECT password FROM user WHERE login = \'"+userName+"\';";
            System.out.println(s);
            resultSet=stmt.executeQuery(s);
            if (!resultSet.isClosed()){
                userPassword=resultSet.getString(1);
                System.out.println(userPassword);
            }else System.out.println("Пользователь не найден");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userPassword;
    }

}
