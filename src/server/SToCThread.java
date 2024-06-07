package server;

import Dao.Imp;
import entity.ChatRoom;
import entity.Message;
import entity.MessageType;
import entity.User;
import server.controller.SToCThreadController;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/*
 * 该类的一个对象和某个客户端保持通讯
 */
public class SToCThread extends Thread{
    private Socket socket; //这个线程对应的Socket
    private String id; //对应客户的ID
    private boolean flag = true; //是否结束线程的标志
    private ObjectInputStream ois; //输入流
    private ObjectOutputStream oos; //输出流
    private User user;
    private static Imp imp = new Imp(); // 静态变量
    private ChatRoom chatRoom;
    public SToCThread(Socket socket, String id,ChatRoom chatRoom) {
        this.socket=socket;
        this.id=id;
        user = imp.getUser(id);
        this.chatRoom=chatRoom;
    }


    @Override
    public void run(){
        System.out.println("服务器与客户【" + id + "】保持通信……");
        while (flag) {
            try {
                ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                actionByMessageType(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void actionByMessageType(Message message){
        try {
            switch (message.getMessageType()){
                case MessageType.MESSAGE_GET_ONLINE_FRIEND :
                    //对方请求列表
                    System.out.println("【" + message.getSender() + "】要看在线用户列表……");
                    System.out.println("[服务器 - " + Thread.currentThread().getName() +
                            "] 收到来自用户 [" + message.getSender() + "] 的请求：MESSAGE_GET_ONLINE_FRIEND，聊天室：" + chatRoom.getName()); // 添加日志输出
                    Message message1 = new Message();
                    message1.setMessageType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    message1.setGetter(message.getSender());   //发送者变接受者
                    message1.setRoomName(chatRoom.getName()); // 设置聊天室名称

                    //消息内容为找到的内容
                    String onlineFriends = SToCThreadController.getOnlineFriends(chatRoom.getName());
                    System.out.println("[服务器 - " + Thread.currentThread().getName() +
                            "] 在线用户列表：" + onlineFriends); // 添加日志输出

                    message1.setContent(onlineFriends);
                    System.out.println("[服务器 - " + Thread.currentThread().getName() +
                            "] 发送消息给用户 [" + message1.getGetter() + "]：" + message1); // 添加日志输出
                    sendMessage(message1);
                    break;
                case MessageType.MESSAGE_CLIENT_EXIT:
                    SToCThreadController.deleteSocket(id,chatRoom.getName());
                    flag = false;       //此线程结束的标志
                    SToCThreadController.deleteSocket(id,chatRoom.getName()); //从线程集合中除名
                    socket.close();     //将这个Socket移除
                    System.out.println("用户【" + id + "】断开连接！");
                    break;
                case MessageType.MESSAGE_COMM_MES:
                    if(message.getGetter().equals("ALL")){
                        SToCThreadController.sendAll(socket, message,chatRoom.getName());
                        break;
                    }
                    Socket socket = SToCThreadController.getSocketById(message.getGetter(),chatRoom.getName());
                    Message message2 = new Message();
                    if(imp.isUser(message.getGetter())){  //看此用户是否在数据库中
                        //注册的用户里有这号人
                        if (socket == null){
                            //发回原处，告知当前用户离线，已经留言
                            socket = this.socket;
                            message2.setMessageType(MessageType.MESSAGE_CLIENT_OFFLINE);
                            message2.setGetter(message.getGetter());
                            //把留言存到数据库里
                            SToCThreadController.addMessage(message.getGetter(),message);
                        }else {
                            message2=message;
                        }
                    }else {
                        //数据库的用户里没有这号人
                        socket = this.socket;
                        message2.setMessageType(MessageType.MESSAGE_CLIENT_NO_EXIST);
                        message2.setGetter(message.getGetter());
                    }
                    sendMessage(message2);
                    break;
            }
        } catch (Exception e) {
            System.out.println("出现异常！");
            e.printStackTrace();
        }
    }
    public Socket getSocket() {
        return socket;
    }
    public void sendMessage(Message message) {
        try {
            if (oos == null) { // 如果 oos 为空，则创建新的对象
                oos = new ObjectOutputStream(socket.getOutputStream());
            }
            oos.writeObject(message);
            oos.flush(); // 刷新缓冲区
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
