package entity;

import server.SToCThread;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

public class ChatRoom implements Serializable {
    private static final long serialVersionUID=1L;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private ConcurrentHashMap<String, SToCThread> clients = new ConcurrentHashMap<>();

    public ChatRoom(String name) {
        this.name = name;
    }

    // 添加客户端
    public void addClient(String userId, SToCThread thread) {
        clients.put(userId, thread);
    }

    // 移除客户端
    public void removeClient(String userId) {
        clients.remove(userId);
    }

    // 发送消息
    public void sendMessage(Message message, SToCThread sender) {
        clients.forEach((clientId, client) -> {
            if (!clientId.equals(sender.getId())) {
                try {
                    client.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}