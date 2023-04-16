package at.htl.mqtt.client.entity;

import at.htl.mqtt.client.dto.RoomDTO;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Room extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String floor;

    @JsonbTransient
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Value> values = new ArrayList<>();

    public Room(String name, String floor) {
        this.name = name;
        this.floor = floor;
    }

    public Room(RoomDTO roomDTO) {
        this.name = roomDTO.name;
        this.floor = roomDTO.floor;
    }

    public Room() {

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Value> getValues() {
        return values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }

    public void addValue(Value value) {
        this.values.add(value);
    }

    public String mqttPath() {
        return (floor + "/" + name).toLowerCase();
    }

    @Override
    public String toString() {
        return getName();
    }
}
