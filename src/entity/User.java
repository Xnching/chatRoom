package entity;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID=1L;
    String password;
    String id;

    public User(String password, String id) {
        this.password = password;
        this.id = id;
    }

    public User() {

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
