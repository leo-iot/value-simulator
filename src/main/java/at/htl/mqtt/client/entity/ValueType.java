package at.htl.mqtt.client.entity;

import at.htl.mqtt.client.dto.ValueTypeDTO;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ValueType extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int minValue;
    private int maxValue;

    @JsonbTransient
    @OneToMany(mappedBy = "valueType", cascade = CascadeType.ALL)
    private List<Value> values = new ArrayList<>();

    public ValueType(String name, int minValue, int maxValue) {
        this.name = name;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public ValueType(ValueTypeDTO valueTypeDTO) {
        this.name = valueTypeDTO.name;
        this.minValue = valueTypeDTO.minValue;
        this.maxValue = valueTypeDTO.maxValue;
    }

    public ValueType() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public List<Value> getValues() {
        return values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }

    public void addValue(Value value) {
        values.add(value);
    }
}
