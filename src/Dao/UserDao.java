package Dao;

import entity.User;

import java.sql.SQLException;

public interface UserDao {
    boolean login(User user);
    boolean register(User user);
    boolean update(User user) throws SQLException;
    User getUser(String id);
    boolean isUser(String id);
}
