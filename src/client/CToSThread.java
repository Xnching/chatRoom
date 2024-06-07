package client;

import client.controller.ClientController;
import entity.Message;
import entity.MessageType;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class CToSThread extends Thread{
    private Socket socket;
    private String id;
    private ObjectInputStream ois;
    private ClientController clientController; // 添加clientController引用
    public CToSThread(Socket socket, String id, ClientController clientController) {
        this.id=id;
        this.socket=socket;
        this.clientController=clientController;
    }

    @Override
    public void run(){
        //后台Socket服务器要一直保持通讯
        try {
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true){
            try {

                //如果在流中没有读取到这一对象，则会停顿在此处
                Message message = (Message) ois.readObject();
                System.out.println("[客户端 - " + Thread.currentThread().getName() +
                        "] 收到消息：" + message.getMessageType()); // 添加日志输出
                System.out.println("[客户端 - " + Thread.currentThread().getName() + "] message.getRoomName()：" + message.getRoomName());
                System.out.println("[客户端 - " + Thread.currentThread().getName() + "] clientController.getCurrentRoomName()：" + clientController.getCurrentRoomName());

                // 如果消息中没有聊天室信息，或者聊天室信息与当前聊天室一致，则处理该消息
                if (message.getRoomName() == null ||
                        message.getRoomName().equals(clientController.getCurrentRoomName())) {
                    actionByMessageType(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }


    public void actionByMessageType(Message message){
        switch (message.getMessageType()) {
            case MessageType.MESSAGE_RET_ONLINE_FRIEND:
                System.out.println("[客户端 - " + Thread.currentThread().getName() + "] 处理消息：MESSAGE_RET_ONLINE_FRIEND");
                System.out.println("[客户端 - " + Thread.currentThread().getName() + "] 接收到的消息内容：" + message.getContent()); // 添加日志输出
                //客户收到服务器的返回信息，信息内容就是在线人数
                String[] split = message.getContent().split(" ");
                System.out.println("在线用户如下：");
                for (String name : split) {
                    System.out.println("用户：" + name);
                }
                break;
            case MessageType.MESSAGE_COMM_MES:
                //收到普通消息，提出内容、发送者、发送时间打印在控制台
                System.out.println(message.getSendTime());
                if (message.getGetter().equals("All")) {
                    //说明这是群发消息
                    System.out.println("【" + message.getSender() + "】对【所有人】说：");
                } else {
                    System.out.println("【" + message.getSender() + "】对【我】说：");
                }
                System.out.print(message.getContent() + "\n\n请输入你的选择：");
                break;
            case MessageType.MESSAGE_CLIENT_NO_EXIST:
                //私聊目标不存在
                System.out.println("客户【" + message.getGetter() + "】不存在，无法发送！");
                break;
            case MessageType.MESSAGE_CLIENT_OFFLINE:
                System.out.println("客户【" + message.getGetter() + "】不在线，其在线后会收到消息！");
                break;
            case MessageType.MESSAGE_RECEIVE_OFFLINE_MESSAGE:
                System.out.println("[离线消息] " + message.getSender() + " 对你说: " + message.getContent());
                break;
        }
    }
    public Socket getSocket() {
        return socket;
    }
}
