package server.controller;

import entity.Room;
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

    private static ConcurrentHashMap<String, Room>chatRooms = new ConcurrentHashMap<String, Room>();
    private static Imp imp = new Imp(); // 静态变量
    public ServerController(){
        try {
            System.out.println("此处为一直处于监听端口。。。。");
            serverSocket = new ServerSocket(5176);
            while (true){
                //服务器接受客户端的连接请求，并返回一个套接字，客户机通过此套接字与服务器通信。
                Socket socket = serverSocket.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                User user = (User) ois.readObject();
                System.out.println("接收到用户"+user+"发起请求！");
                //构建一个消息对象，准备用来回复
                Message message = new Message();
                //检验密码是否正确
                if(imp.login(user)){
                    message.setMessageType(MessageType.MESSAGE_LOGIN_SUCCESS);
                    oos.writeObject(message);

                    //启动服务器连接客户端线程
                    SToCThread thread = new SToCThread(socket,user.getId());
                    thread.start();
                    SToCThreadController.addThread(user.getId(),thread);
                    SToCThreadController.sendOffLineMessage(user.getId());
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
