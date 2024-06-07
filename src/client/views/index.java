package client.views;

import client.controller.ClientController;
import entity.Utility;

public class index {
    private boolean loop = true; //控制是否显示菜单
    private String key = ""; //接收用户键盘输入
    private ClientController clientController = new ClientController();

    private String id;
    public static void main(String[] args) {
        new index().mainMenu();
    }
    public void mainMenu() {
        while (loop) {
            System.out.println("============欢迎登录网络通信系统============");
            System.out.println("\t\t 1 登录系统");
            System.out.println("\t\t 9 退出系统");
            System.out.print("请输入你的选择：");
            key = Utility.readString();
            switch (key) {
                case "1":
                    System.out.print("请输入用户号：");
                    String userId = Utility.readString();
                    id=userId;
                    System.out.print("请输入密 码：");
                    String pwd = Utility.readString();
                    //去服务端看看用户是否合法
                    if (clientController.connectToServer(userId, pwd)) {
                        System.out.println("============欢迎【" + userId + "】登录网络通信系统============");

                        //选择聊天室
                        System.out.println("请选择聊天室：");
                        System.out.println("1. 房间1");
                        System.out.println("2. 房间2");
                        System.out.print("请输入你的选择：");
                        String roomChoice = Utility.readString();
                        String roomName = roomChoice.equals("1") ? "房间1" : "房间2";
                        clientController.chooseChatRoom(roomName); // 通知Controller选择聊天室

                        chatMenu(userId);

                    } else {
                        System.out.println("登录失败！");

                    }
                    break;
                case "9":
                    loop = false;
                    break;
                default:
                    System.out.println("输入有误，请重新输入！");
            }
        }
    }
    private void chatMenu(String userId){
        //由此进入二级菜单
        while (loop) {
            System.out.println("============【" + userId + "】网络通信系统二级菜单============");
            System.out.println("\t\t 1 显示在线用户列表");
            System.out.println("\t\t 2 群发消息");
            System.out.println("\t\t 3 私聊消息");
            System.out.println("\t\t 9 退出系统");
            System.out.print("请输入你的选择：");
            key = Utility.readString();
            String name; //发送给谁
            String contents; //消息内容
            switch (key) {
                case "1":
                    clientController.onlineFriendList();
                    System.out.println("[客户端] 发送请求：MESSAGE_GET_ONLINE_FRIEND，目标聊天室：" + clientController.getCurrentRoomName()); // 添加日志输出
                    break;
                case "2":
                    System.out.print("群发内容：" );
                    contents = Utility.readString();
                    clientController.sendAll(contents);
                    break;
                case "3":
                    System.out.print("发送给：");
                    name = Utility.readString();
                    if(name.equals(id)){
                        System.out.println("不能私信自己！");
                    }else {
                        System.out.print("内容：" );
                        contents = Utility.readString();
                        clientController.Send(name,contents);
                    }
                    break;
                case "9":
                    clientController.closedComm();
                    System.out.println("客户端退出...");
                    loop = false;
                    break;
                default:
                    System.out.println("输入有误，请重新输入！");
            }
            try {
                Thread.sleep(5); //为了输出好看些
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }


}


