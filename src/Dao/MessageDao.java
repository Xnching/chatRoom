package Dao;

import entity.Message;

import java.util.Vector;

public interface MessageDao {
    int insertMessage(Message message);
    Vector<Message> getMessage(String id);

    void deleteMessages(String id);
}
