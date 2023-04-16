package at.htl.mqtt.client.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;

@Entity
public class Value extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double lastValue;


    // Y-Value 1    X-Value 1 = 0
    private double lastFullValue;

    // Y-Value 2
    private double nextFullValue;

    //  X-Value 2
    private int amountOfIterations;



    // X-Value of current element
    private int iterationsCount;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "valueType_id")
    private ValueType valueType;


    public Value(Room room, ValueType valueType) {
        this.room = room;
        room.addValue(this);
        this.valueType = valueType;
        valueType.addValue(this);
        this.lastValue = 0;
    }

    public Value() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public void setValueType(ValueType valueType) {
        this.valueType = valueType;
    }

    public double getLastValue() {
        return lastValue;
    }

    public void setLastValue(double lastValue) {
        this.lastValue = lastValue;
    }

    public double getLastFullValue() {
        return lastFullValue;
    }

    public void setLastFullValue(double lastFullValue) {
        this.lastFullValue = lastFullValue;
    }

    public double getNextFullValue() {
        return nextFullValue;
    }

    public void setNextFullValue(double nextFullValue) {
        this.nextFullValue = nextFullValue;
    }

    public int getAmountOfIterations() {
        return amountOfIterations;
    }

    public void setAmountOfIterations(int amountOfIterations) {
        this.amountOfIterations = amountOfIterations;
    }

    public int getIterationsCount() {
        return iterationsCount;
    }

    public void setIterationsCount(int iterationsCount) {
        this.iterationsCount = iterationsCount;
    }

    @Override
    public String toString() {
        return "Value{" +
                "id=" + id +
                ", lastFullValue=" + lastFullValue +
                ", nextFullValue=" + nextFullValue +
                ", amountOfIterations=" + amountOfIterations +
                ", iterationsCount=" + iterationsCount +
                ", valueType=" + valueType +
                '}';
    }
}
