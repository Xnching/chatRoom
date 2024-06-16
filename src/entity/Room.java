package entity;

public class Room {
    private static final long serialVersionUID=1L;
    String id;

    public Room(String id) {
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
