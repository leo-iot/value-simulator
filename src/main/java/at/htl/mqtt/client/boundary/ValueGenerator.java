package at.htl.mqtt.client.boundary;

import at.htl.mqtt.client.entity.Room;
import at.htl.mqtt.client.entity.Value;
import at.htl.mqtt.client.entity.ValueType;
import io.quarkus.scheduler.Scheduled;
import io.smallrye.reactive.messaging.mqtt.MqttMessage;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;
import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ValueGenerator {

    @Inject
    @Channel("topic-values")
    @OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 1000)
    Emitter<byte[]> emitter;

    private List<Double> goodTemps = new LinkedList<>();

    @Scheduled(every = "20s")
    void sendValues() {
        var rooms = Room.listAll();
        for (var room : rooms) {
            sendValueForRoom((Room) room);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
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
        return newRandomValue(value);
    }

    private double newRandomValue(Value value){
        double r = Math.random() * value.getValueType().getMultiplier();
        double newVal = value.getLastValue();

        if(newVal == 0){
            newVal = (double) (value.getValueType().getMinValue() + value.getValueType().getMaxValue()) / 2;
        }

        if(Math.round(Math.random())==0){
            newVal += r;
        }
        else {
            newVal -= r;
        }

        if(newVal > value.getValueType().getMaxValue() || newVal < value.getValueType().getMinValue()){
            newVal = newRandomValue(value);
        }
        value.setLastValue(newVal);
        if(value.getValueType().isOnlyInteger()){
            return Math.round(newVal);
        }
        else{
            return newVal;
        }
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

