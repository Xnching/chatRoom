package server.controller;

import Dao.Imp;
import entity.Message;
import entity.MessageType;
import entity.Room;
import server.SToCThread;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/*
*
* 管理线程的类
*
* */
public class SToCThreadController {

    // 存储用户所在的聊天室，key 为用户ID，value 为用户加入的聊天室名称列表
    private static ConcurrentHashMap<String, HashSet<String>> roomUsers = new ConcurrentHashMap<>();
    // 存储用户对应的线程，key 为用户ID，value 为用户对应的 SToCThread 线程
    private static ConcurrentHashMap<String, SToCThread> userThreads = new ConcurrentHashMap<>();


    // 静态变量
    private static Imp imp = new Imp();

    // 创建聊天室
    public static void createRoom(Message message){
        String roomName = message.getRoomName();
        imp.addChatRoom(roomName);
    }

    // 加入聊天室
    public static void joinRoom(Message message, SToCThread thread){
        String roomName = message.getRoomName();
        // 将用户加入聊天室
        roomUsers.computeIfAbsent(roomName, k -> new HashSet<>()).add(thread.user.getId());
        Message message1 = new Message();
        sendAllUsers(message1,roomName);
    }

    //获取聊天室列表
    public static String getRooms(){
        StringBuilder builder = new StringBuilder();
        List<Room> rooms = imp.getChatRoom();
        for (Room room: rooms) {
            builder.append(room.getId()).append(" ");
        }
        return builder.toString();
    }

    /*
     *获取所有的在线用户
     * @param roomName 想要获取所有在线用户的的聊天室
     * */
    public static String getOnlineFriends(String roomName){
        StringBuilder builder = new StringBuilder();
        HashSet<String> users = roomUsers.get(roomName);
        if (users != null) {
            for (String userId : users) {
                builder.append(userId).append(" ");
            }
        }
        return builder.toString();
    }


    /*
     *通过id找到socket
     * @param 用户id
     * */
    public static Socket getSocketById(String id){
        SToCThread thread = userThreads.get(id);
        System.out.println("打印线程id"+thread.user.getId());
        if (thread != null) {
            return thread.getSocket();
        }
        return null;
    }


    //当有用户加入房间时再次给所有用户发送在线用户列表
    public static void sendAllUsers( Message message, String roomName){
        // 获取聊天室的用户 ID 列表
        HashSet<String> userIds = roomUsers.get(roomName);
        if (userIds == null) {
            System.out.println("聊天室不存在或者为空！！！");
            return; // 聊天室不存在或为空
        }
        // 遍历用户 ID 列表，发送消息
        for (String userId : userIds) {
            Socket receiverSocket = getSocketById(userId);
            if (receiverSocket != null) {
                try {
                    message.setSender(null);
                    message.setContent(getOnlineFriends(roomName));
                    message.setGetter(userId);
                    message.setRoomName(roomName);
                    message.setMessageType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    System.out.println("将要发送消息给："+userId);
                    System.out.println("消息接收人："+message.getGetter());
                    System.out.println("消息类型："+message.getMessageType());
                    System.out.println("房间名"+message.getRoomName());
                    System.out.println("消息内容:"+message.getContent());
                    ObjectOutputStream oos = new ObjectOutputStream(receiverSocket.getOutputStream());
                    oos.writeObject(message);
                    System.out.println("消息已经发出去了！！！");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }//ABDC
        }
    }
    /*
     *向所有的Socket发送消息，群发
     * @param senderSocket 除了这个socket
     * @param message   发送的消息
     * @param roomName  发送群发所在的聊天室
     * */
    public static void sendAll(Socket senderSocket, Message message, String roomName) {
        // 获取聊天室的用户 ID 列表
        HashSet<String> userIds = roomUsers.get(roomName);
        if (userIds == null) {
            System.out.println("聊天室不存在或者为空！！！");
            return; // 聊天室不存在或为空
        }
        // 遍历用户 ID 列表，发送消息
        for (String userId : userIds) {
            // 跳过发送者自己
            if (userThreads.get(userId).getSocket() == senderSocket) {
                System.out.println("跳过发送者自己！！！");
                continue;
            }
            Socket receiverSocket = getSocketById(userId);
            if (receiverSocket != null) {
                try {
                    System.out.println("将要发送消息给："+userId);
                    System.out.println("消息发送人："+message.getSender());
                    System.out.println("消息接收人："+message.getGetter());
                    System.out.println("消息类型："+message.getMessageType());
                    System.out.println("房间名"+message.getRoomName());
                    System.out.println("消息内容:"+message.getContent());
                    ObjectOutputStream oos = new ObjectOutputStream(receiverSocket.getOutputStream());
                    oos.writeObject(message);
                    System.out.println("消息已经发出去了！！！");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }//ABDC
        }
    }

    /*
     * 服务器暂存离线信息
     * @param id 接受者ID
     * @param message 需要发送的信息
     */
    public static void addMessage(String id,Message message){
        message.setGetter(id);
        message.setMessageType(MessageType.MESSAGE_RECEIVE_OFFLINE_MESSAGE);
        imp.insertMessage(message);
    }


    /*
     * 尝试将服务器库存信息进行发送
     * @param id 接受者ID
     *
     */
    public static void sendOffLineMessage(String id){
        System.out.println("将要发送离线消息！");
        Vector<Message> vector = imp.getMessage(id);//得到库存信息
        if(!(vector == null || vector.isEmpty())){
            try {
                //说明当前用户有待发送消息
                Socket socket = getSocketById(id);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                while (!vector.isEmpty()){
                    Message message = vector.get(0);
                    //将消息按顺序发出去
                    oos.writeObject(message);
                    vector.remove(message);
                }
                System.out.println("发送完离线消息！");
                oos.flush();  // 刷新缓冲区，确保数据发送
                // 发送完毕后，删除已发送的消息
                imp.deleteMessages(id);  // 新增方法，用于删除多个消息
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("已经没有离线消息需要发送");
    }//CABAC


    // 用户退出登录
    public static void logout(SToCThread thread) {
        String userId = thread.user.getId();
        // 从所有聊天室中移除用户
        for (HashSet<String> users : roomUsers.values()) {
            users.removeIf(u -> u.equals(userId));
        }
        // 从用户线程列表中移除用户
        userThreads.remove(userId);
    }
    public static void addThread(String id,SToCThread thread){
        userThreads.put(id,thread);
    }
}
