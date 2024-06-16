package Dao;

import entity.Room;

import java.util.List;

public interface RoomDao {
    boolean addChatRoom(String roomName);
    List<Room> getChatRoom();
}
