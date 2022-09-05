package ru.gb.service;

import java.sql.*;

public class DAO {
    public Connection connection;

    public String getUserPasswordFromDB(String userName) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:user_db/user_db.db");
        String userPassword = null;
        ResultSet resultSet;
        try (Statement stmt = connection.createStatement()) {
            String s = "SELECT password FROM user WHERE login = \'" + userName + "\';";
            resultSet = stmt.executeQuery(s);
            if (!resultSet.isClosed()) {
                userPassword = resultSet.getString(1);
            } else System.out.println("Пользователь не найден");
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
        try (Statement stmt = connection.createStatement();) {
            s = "select user_rights from user_access_rights where id_right=(Select id_user from user where login=\'" + userName + "\');";
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
        try (Statement stmt = connection.createStatement()) {
            s = "select work_dir from user_dir where id_dir=(Select user_dir from user where login=\'" + userName + "\');";
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

    public boolean registerUserInDB(String userName, String password) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:user_db/user_db.db");
        connection.setAutoCommit(false);
        long q = 0;
        String s;
        try (Statement stmt = connection.createStatement()) {
            s = "INSERT INTO user_dir (work_dir) VALUES (\'" + userName + "\');";
            stmt.executeUpdate(s);
            CryptService cryptService = new CryptService();
            password = cryptService.getCryptString(password);
            s = "INSERT INTO user (login,password,user_rights,user_dir,user_quote)" +
                    " VALUES (\'" + userName + "\',\'" + password + "\',(select id_right from user_access_rights where user_rights='full')," +
                    "(select id_dir from user_dir where work_dir=\'" + userName + "\'),200);";
            stmt.executeUpdate(s);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
            connection.close();
            return false;
        }
        connection.close();
        return true;
    }

}
