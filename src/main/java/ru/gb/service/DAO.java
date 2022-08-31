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
            //System.out.println("Права пользователя: "+getUserRightsFromDB(userName));
            //System.out.println("Папка пользователя: "+getUserWorkDirFromDB(userName));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userPassword;
    }

    public String getUserRightsFromDB(String userName){
        ResultSet resultSet;
        String s="";
        try{
            Statement stmt = connection.createStatement();
            s = "select user_rights from user_access_rights where id_right=(Select id_user from user where login=\'"+userName+"\');";
            System.out.println(s);
            resultSet=stmt.executeQuery(s);
            if (!resultSet.isClosed()){
                s=resultSet.getString(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return s;
    }

    public String getUserWorkDirFromDB(String userName){
        ResultSet resultSet;
        String s="";
        try{
            Statement stmt = connection.createStatement();
            s = "select work_dir from user_dir where id_dir=(Select id_user from user where login=\'"+userName+"\');";
            System.out.println(s);
            resultSet=stmt.executeQuery(s);
            if (!resultSet.isClosed()){
                s=resultSet.getString(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return s;
    }
}
