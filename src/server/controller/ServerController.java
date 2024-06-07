package server.controller;

import entity.ChatRoom;
import entity.Message;
import entity.MessageType;
import entity.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import Dao.Imp;
import server.SToCThread;

public class ServerController {
    ServerSocket serverSocket;

    public static void main(String[] args) {
        new ServerController();
    }

    private static ConcurrentHashMap<String, ChatRoom>chatRooms = new ConcurrentHashMap<String, ChatRoom>();
    private static Imp imp = new Imp(); // 静态变量
    public ServerController(){
        try {
            System.out.println("此处为一直处于监听端口。。。。");
            serverSocket = new ServerSocket(9999);
            while (true){
                //服务器接受客户端的连接请求，并返回一个套接字，客户机通过此套接字与服务器通信。
                Socket socket = serverSocket.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                User user = (User) ois.readObject();
                //构建一个消息对象，准备用来回复
                Message message = new Message();
                //检验密码是否正确
                if(imp.login(user)){
                    message.setMessageType(MessageType.MESSAGE_LOGIN_SUCCESS);
                    oos.writeObject(message);

                    // 接收客户端选择的聊天室
                    Message chooseRoomMessage = (Message) ois.readObject();
                    String roomName = chooseRoomMessage.getRoomName();
                    // 创建或获取聊天室
                    ChatRoom chatRoom = chatRooms.computeIfAbsent(roomName, k -> new ChatRoom(roomName));

                    //启动服务器连接客户端线程
                    SToCThread thread = new SToCThread(socket,user.getId(),chatRoom);
                    thread.start();
                    chatRoom.addClient(user.getId(), thread);
                    SToCThreadController.addThread(user.getId(),thread,chatRoom.getName());
                    SToCThreadController.sendOffLineMessage(user.getId(), chatRoom.getName());
                }else {
                    //失败就发送失败消息
                    message.setMessageType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    socket.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
