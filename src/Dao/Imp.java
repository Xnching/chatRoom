package Dao;

import JDBC.JDBCUtils;
import entity.Message;
import entity.Room;
import entity.User;

import java.sql.*;
import java.util.List;
import java.util.Vector;

public class Imp implements UserDao, MessageDao, RoomDao {


    @Override
    public boolean login(User user) {
        if(user == null)return false;
        Connection conn = JDBCUtils.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        boolean flag = false;
        String sql = "select * from user where id = ? and password = ?";
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,user.getId());
            preparedStatement.setString(2, user.getPassword());
            result = preparedStatement.executeQuery();
            //查到了就返回true
            while (result.next()){
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(conn,preparedStatement,result,null);
        }
        return flag;
    }

    @Override
    public boolean register(User user) {
        Connection conn = JDBCUtils.getConnection();
        PreparedStatement pstmt = null; // 使用 PreparedStatement
        ResultSet rs = null;
        boolean flag = false;
        String sql = "INSERT INTO user (id, password) VALUES (?, ?)";
        try {
            // 设置 PreparedStatement 返回自增主键
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getPassword());
            int line = pstmt.executeUpdate(sql);
            if (line > 0) {
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(conn, pstmt, rs, null); // 注意参数顺序
        }
        return flag;
    }

    @Override
    public User getUser(String id){
        User user = new User();
        Connection conn = JDBCUtils.getConnection();
        PreparedStatement pstmt = null; // 使用 PreparedStatement
        ResultSet rs = null;
        String sql = "select * from user where id = ?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,id);
            rs = pstmt.executeQuery();
            while (rs.next()){
                user.setPassword(rs.getString("password"));
                user.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(conn,pstmt,rs,null);
        }
        return user;
    }

    @Override
    public boolean isUser(String id) {
        Connection conn = JDBCUtils.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        boolean flag = false;
        String sql = "select * from user where id = ? ";
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,id);
            result = preparedStatement.executeQuery();
            //查到了就返回true
            while (result.next()){
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(conn,preparedStatement,result,null);
        }
        return flag;
    }


    @Override
    public boolean update(User user) throws SQLException {
        return false;
    }

    @Override
    public int insertMessage(Message message) {
        Connection conn = JDBCUtils.getConnection();
        PreparedStatement pstmt = null; // 使用 PreparedStatement
        ResultSet rs = null;
        String sql = "INSERT INTO message (sender,getter,content,sendTime,messageType) VALUES (?, ?, ?, ? ,?)";
        int rows=0;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,message.getSender());
            pstmt.setString(2,message.getGetter());
            pstmt.setString(3, message.getContent());
            pstmt.setString(4,message.getSendTime());
            pstmt.setString(5, message.getMessageType());
            rows = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(conn,pstmt,rs,null);
        }
        return rows;
    }

    @Override
    public Vector<Message> getMessage(String id){
        Vector<Message> v = new Vector<>();
        Connection conn = JDBCUtils.getConnection();
        PreparedStatement pstmt = null; // 使用 PreparedStatement
        ResultSet rs = null;
        String sql = "select * from message where getter = ?";
        String sender = null;
        String getter = null;
        String content = null;
        String sendTime = null;
        String messageType = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,id);
            rs = pstmt.executeQuery();
            while (rs.next()){
                sender = rs.getString("sender");
                getter = rs.getString("getter");
                content = rs.getString("content");
                sendTime = rs.getString("sendTime");
                messageType = rs.getString("messageType");
                v.add(new Message(sender,getter,content,sendTime,messageType));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(conn,pstmt,rs,null);
        }
        return v;
    }

    @Override
    public void deleteMessages(String id) {
        Connection conn = JDBCUtils.getConnection();
        PreparedStatement pstmt = null; // 使用 PreparedStatement
        int rs = 0;
        String sql = "DELETE FROM message WHERE getter = ?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,id);
            rs = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(conn,pstmt,null,null);
        }
    }

    @Override
    public boolean addChatRoom(String roomName) {
        Connection conn = JDBCUtils.getConnection();
        PreparedStatement pstmt = null; // 使用 PreparedStatement
        ResultSet rs = null;
        boolean flag = false;
        String sql = "INSERT INTO croom VALUES (?)";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, roomName);
            int line = pstmt.executeUpdate();
            if (line > 0) {
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(conn, pstmt, rs, null); // 注意参数顺序
        }
        return flag;
    }

    @Override
    public List<Room> getChatRoom() {
        List<Room> v = new Vector<>();
        Connection conn = JDBCUtils.getConnection();
        PreparedStatement pstmt = null; // 使用 PreparedStatement
        ResultSet rs = null;
        String sql = "select * from croom ";
        String id = null;
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()){
                id = rs.getString("id");
                v.add(new Room(id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(conn,pstmt,rs,null);
        }
        return v;
    }
}
