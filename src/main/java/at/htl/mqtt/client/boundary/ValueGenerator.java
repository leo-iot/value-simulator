package at.htl.mqtt.client.boundary;

import at.htl.mqtt.client.entity.Room;
import at.htl.mqtt.client.entity.Value;
import at.htl.mqtt.client.entity.ValueType;
import io.quarkus.scheduler.Scheduled;
import io.smallrye.reactive.messaging.mqtt.MqttMessage;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ValueGenerator {

    @Inject
    @Channel("topic-values")
    Emitter<byte[]> emitter;

    private List<Double> goodTemps = new LinkedList<>();

    @Scheduled(every = "10s")
    void sendValues() {
        var rooms = Room.listAll();
        for (var room : rooms) {
            sendValueForRoom((Room) room);
        }
    }

    private void sendValueForRoom(Room room) {
        Map<String, Object> values = new HashMap<>();

        values.put("time", System.currentTimeMillis());
        for (Value value : room.getValues()) {
            values.put(value.getValueType().getName(), getValue(value));
        }

        JSONObject jsonValue = new JSONObject(values);

        long timeStamp = jsonValue.getLong("time");

        for (Value value : room.getValues()) {
            emitter.send(MqttMessage.of(room.mqttPath() + "/" + value.getValueType().getName() + "/state",
                    getBytes(jsonValue.getDouble(value.getValueType().getName()), timeStamp)));
        }

        System.out.println("Sending value -> " + jsonValue);
    }

    public Object getValue(Value value) {
        ValueType vt = value.getValueType();
        switch (vt.getName()){
            case "noise":
                return Math.random() * 100;
            case "trafficlight":
            case "motion":
                return Math.floor(Math.random());
            case "temperature":
                return goodTemp();
            case "humidity":
                return Math.random() * 20;
            case "pressure":
                return  Math.random() * 1000;
            case "luminosity":
                return Math.random() * 2000;
            case "co2":
                return Math.random() * 700;
        }
        return 0;
    }

    public byte[] getBytes(Object value, long timeStamp) {
        JSONObject json = new JSONObject();
        return json.put("value", value).put("timestamp", timeStamp).toString().getBytes();
    }

    public double goodTemp()
    {
        double hours = LocalDateTime.now().getHour()+((LocalDateTime.now().getMinute()/60.00*100.00)/100.00);
        //return (-0.01 * Math.pow(hours,3) + 0.2 * Math.pow(hours,2) + 10)+10;

        double sum1 = -0.014 * Math.pow(hours,3);
        double sum2 = 0.4 * Math.pow(hours,2);
        double sum3 = -1.8 * hours + 11;

        //System.out.println(sum1);
        //System.out.println(sum2);
        //System.out.println(sum3);
        //System.out.println(hours);
        //System.out.println(LocalDateTime.now().getMinute());

        double sum = sum1+sum2+sum3;

        goodTemps.add(sum);

        return  sum1+ sum2 + sum3;
    }

}

