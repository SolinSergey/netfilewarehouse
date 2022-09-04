package ru.gb.service;

import java.sql.*;

public class DAO {
    public Connection connection;

    public String getUserPasswordFromDB(String userName) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:user_db/user_db.db");
        String userPassword = null;
        ResultSet resultSet;
        try (Statement stmt = connection.createStatement()){
            String s = "SELECT password FROM user WHERE login = \'" + userName + "\';";
            //System.out.println(s);
            resultSet = stmt.executeQuery(s);
            if (!resultSet.isClosed()) {
                userPassword = resultSet.getString(1);
                //System.out.println(userPassword);
            } else System.out.println("Пользователь не найден");
            //System.out.println("Права пользователя: "+getUserRightsFromDB(userName));
            //System.out.println("Папка пользователя: "+getUserWorkDirFromDB(userName));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
        return userPassword;
    }

    public String getUserRightsFromDB(String userName) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:user_db/user_db.db");
        ResultSet resultSet;
        String s = "";
        try (Statement stmt = connection.createStatement();){

            s = "select user_rights from user_access_rights where id_right=(Select id_user from user where login=\'" + userName + "\');";
            //System.out.println(s);
            resultSet = stmt.executeQuery(s);
            if (!resultSet.isClosed()) {
                s = resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
        return s;
    }

    public String getUserWorkDirFromDB(String userName) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:user_db/user_db.db");
        ResultSet resultSet;
        String s = "";
        try (Statement stmt = connection.createStatement()){

            s = "select work_dir from user_dir where id_dir=(Select id_user from user where login=\'" + userName + "\');";
            //System.out.println(s);
            resultSet = stmt.executeQuery(s);
            if (!resultSet.isClosed()) {
                s = resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
        return s;
    }

    public long getUserQuoteFromDB(String userName) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:user_db/user_db.db");
        ResultSet resultSet;
        long q = 0;
        String s;

        try (Statement stmt = connection.createStatement()) {

            s = "SELECT user_quote FROM user WHERE login = \'" + userName + "\';";
            System.out.println(q);
            resultSet = stmt.executeQuery(s);
            if (!resultSet.isClosed()) {
                q = (long) resultSet.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
        return q;
    }

}
