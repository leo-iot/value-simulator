package at.htl.mqtt.client.entity;

public class Room {

    private String name;
    private String floor;

    public Room(String name, String floor) {
        this.name = name;
        this.floor = floor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getPath(){
        return floor + "/" + name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
