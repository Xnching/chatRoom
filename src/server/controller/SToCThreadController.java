package server.controller;

import Dao.Imp;
import entity.Message;
import entity.MessageType;
import server.SToCThread;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/*
* 管理线程的类
*
* */
public class SToCThreadController {

    // <聊天室名称, <用户ID, 线程>>
    public static ConcurrentHashMap<String, ConcurrentHashMap<String, SToCThread>> map = new ConcurrentHashMap<>();
    private static Imp imp = new Imp(); // 静态变量

    /*
     *获取所有的在线用户
     * @param 用户id
     * */
    public static String getOnlineFriends(String roomName){
        StringBuilder builder = new StringBuilder();
        ConcurrentHashMap<String, SToCThread> roomMap = map.get(roomName);
        if (roomMap != null) {
            for (Map.Entry<String, SToCThread> entry : roomMap.entrySet()) {
                builder.append(entry.getKey() + " ");
            }
        }
        return builder.toString();
    }
    /*
     *通过id找到socket
     * @param 用户id
     * */
    public static Socket getSocketById(String id,String roomName){
        ConcurrentHashMap<String, SToCThread> roomMap = map.get(roomName);
        if (roomMap != null) {
            SToCThread thread = roomMap.get(id);
            if (thread != null) {
                return thread.getSocket();
            }
        }
        return null;
    }

    /*
     *向所有的Socket发送消息，群发
     * @param socket 除了这个socket
     * @param oos
     * */
    public static void sendAll(Socket socket, Message message, String roomName){
        try {
            ConcurrentHashMap<String, SToCThread> roomMap = map.get(roomName);
            if (roomMap != null) {
                for (Map.Entry<String, SToCThread> entry : roomMap.entrySet()) {
                    Socket socket1 = getSocketById(entry.getKey(), roomName);
                    if (socket1 != socket) {
                        ObjectOutputStream oos = new ObjectOutputStream(socket1.getOutputStream());
                        oos.writeObject(message);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
     * @param oos 输出流
     */
    public static void sendOffLineMessage(String id, String roomName){
        Vector<Message> vector = imp.getMessage(id);//得到库存信息
        if(!(vector == null || vector.isEmpty())){
            try {
                //说明当前用户有待发送消息
                Socket socket = getSocketById(id,roomName);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                while (!vector.isEmpty()){
                    Message message = vector.get(0);
                    //将消息按顺序发出去
                    oos.writeObject(message);
                    vector.remove(message);
                }
                oos.flush();  // 刷新缓冲区，确保数据发送
                // 发送完毕后，删除已发送的消息
                imp.deleteMessages(id);  // 新增方法，用于删除多个消息
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void deleteSocket(String id, String roomName) {
        ConcurrentHashMap<String, SToCThread> roomMap = map.get(roomName);
        if (roomMap != null) {
            roomMap.remove(id);
        }
    }
    public static void addThread(String id, SToCThread thread, String roomName) {
        ConcurrentHashMap<String, SToCThread> roomMap = map.computeIfAbsent(roomName, k -> new ConcurrentHashMap<>());
        roomMap.put(id, thread);
    }
}
