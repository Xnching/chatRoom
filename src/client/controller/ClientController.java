package client.controller;

import client.CToSThread;
import entity.Message;
import entity.MessageType;
import entity.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Calendar;

public class ClientController {
    private User user = new User(); //当前客户
    private Socket socket; //当前客户对应的Socket
    private boolean flag = false; //登录是否成功的标志
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private String currentRoomName;

    public boolean connectToServer(String id ,String password){
        try {
            //封装一个User对象，发送到服务器进行检查
            user.setId(id);
            user.setPassword(password);
            //连接到服务器
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
            oos = new ObjectOutputStream(socket.getOutputStream());
            //将用户对象发送出去
            oos.writeObject(user);
            ois = new ObjectInputStream(socket.getInputStream());
            //对面会将消息封装为一个Message对象
            Message message = (Message) ois.readObject();
            if(message.getMessageType().equals(MessageType.MESSAGE_LOGIN_SUCCESS)){
                CToSThread thread = new CToSThread(socket,id,this);
                thread.start();
                flag = true;
            }else {
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    // 选择聊天室
    public void chooseChatRoom(String roomName) {
        this.currentRoomName = roomName;
        try {
            Message chooseRoomMessage = new Message();
            chooseRoomMessage.setMessageType(MessageType.MESSAGE_CHOOSE_ROOM);
            chooseRoomMessage.setRoomName(currentRoomName);
            oos.writeObject(chooseRoomMessage);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * 拉取在线客户
     */
    public void onlineFriendList(){
        Message message = new Message(user.getId(), MessageType.MESSAGE_GET_ONLINE_FRIEND);
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
     * 发送一条结束通道的信息给服务器
     */
    public void closedComm(){
        try {
            Message message = new Message(user.getId(), MessageType.MESSAGE_CLIENT_EXIT);
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            socket.close();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * 与某人私聊，并发送信息
     */
    public void Send(String name,String contents){
        try {
            Message message1 = new Message();
            message1.setMessageType(MessageType.MESSAGE_COMM_MES);
            message1.setGetter(name);
            message1.setContent(contents);
            message1.setSender(user.getId());
            message1.setSendTime(formatTime());
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendAll(String contents){
        Send("ALL",contents);
    }
    public String formatTime() {
        Calendar instance = Calendar.getInstance();
        int hour = instance.get(Calendar.HOUR_OF_DAY);
        int min = instance.get(Calendar.MINUTE);
        StringBuilder builder = new StringBuilder("\n");
        if (hour < 10)
            builder.append("0");
        builder.append(hour+":");
        if (min < 10)
            builder.append("0");
        builder.append(min);
        return builder.toString();
    }

    public String getCurrentRoomName() {
        return currentRoomName;
    }
}
