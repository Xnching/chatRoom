package test;


import java.net.*;

public class JavaWeb {
    public static void main(String[] args) throws UnknownHostException {
        //InetAddress address = InetAddress.getByName("DreamDragon");
        //InetAddress address = InetAddress.getByName("10.19.65.91");
        InetAddress address = InetAddress.getByName("www.cn.bing.com");

        String name = address.getHostName();
        String ip = address.getHostAddress();

        System.out.println("主机名：" + name);
        System.out.println("IP地址: " + ip);
    }
}

class UDPClient {
    public static void main(String[] args) throws Exception {
        System.out.println("发送方启动中...");

        // 1、使用DatagramSocket指定端口，创建发送端
        DatagramSocket client = new DatagramSocket(8888);

        // 2、准备数据，一定要转成字节数组
        String data = "Java YYDS";

        // 3、封装成DatagramPacket包裹，需要指定目的地(IP+port)
        byte[] datas = data.getBytes();
        DatagramPacket packet = new DatagramPacket(datas, 0, datas.length, new InetSocketAddress("localhost", 9999));

        // 4、发送包裹 void send(DatagramPacket p)
        client.send(packet);

        // 5、释放资源
        client.close();
    }
}
