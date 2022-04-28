package at.htl.mqtt.client.boundary;

import at.htl.mqtt.client.entity.Room;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.smallrye.reactive.messaging.mqtt.MqttMessage;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;

/**
 * https://stackoverflow.com/questions/62883516/publish-subscribe-mqtt-using-smallrye-reactive-messaging-dynamically
 * https://github.com/quarkusio/quarkus-quickstarts/blob/main/mqtt-quickstart/src/main/java/org/acme/mqtt/PriceGenerator.java
 */
@ApplicationScoped
public class MyValueGenerator {

    @Inject
    @Channel("topic-values")
    Emitter<byte[]> emitter;

    public HashMap<String, Disposable> subscriptions = new HashMap<>();

    private List<Double> goodTemps = new LinkedList<>();

    public void roomData(Room room){
        subscriptions.put(room.getName(), Observable.interval(0, 20, TimeUnit.SECONDS)
                .subscribe(value -> {
                    Map values = new HashMap<String, Object>();
                    values.put("temp", System.currentTimeMillis());
                    values.put("noise", Math.random() * 100);
                    values.put("trafficlight", Math.floor(Math.random()));
                    values.put("temperature", goodTemp());
                    values.put("humidity", Math.random() * 20);
                    values.put("pressure", Math.random() * 1000);
                    values.put("luminosity", Math.random() * 2000);
                    values.put("co2", Math.random() * 700);
                    values.put("motion", Math.floor(Math.random()));

                    JSONObject jsonValue = new JSONObject(values);

                    long timeStamp = jsonValue.getLong("temp");
                    String topicPath = "";
                    String stateStr = "/state";
                    emitter.send(MqttMessage.of(topicPath + room.getPath() + "/" + "noise" + stateStr, getBytes(jsonValue.getDouble("noise"), timeStamp)));
                    emitter.send(MqttMessage.of(topicPath + room.getPath() + "/" + "trafficlight" + stateStr, getBytes(jsonValue.getDouble("trafficlight"), timeStamp)));
                    emitter.send(MqttMessage.of(topicPath + room.getPath() + "/" + "temperature" + stateStr, getBytes(jsonValue.getDouble("temperature"), timeStamp)));
                    emitter.send(MqttMessage.of(topicPath + room.getPath() + "/" + "humidity" + stateStr, getBytes(jsonValue.getDouble("humidity"), timeStamp)));
                    emitter.send(MqttMessage.of(topicPath + room.getPath() + "/" + "pressure" + stateStr, getBytes(jsonValue.getDouble("pressure"), timeStamp)));
                    emitter.send(MqttMessage.of(topicPath + room.getPath() + "/" + "luminosity" + stateStr, getBytes(jsonValue.getDouble("luminosity"), timeStamp)));
                    emitter.send(MqttMessage.of(topicPath + room.getPath() + "/" + "co2" + stateStr, getBytes(jsonValue.getDouble("co2"), timeStamp)));
                    emitter.send(MqttMessage.of(topicPath + room.getPath() + "/" + "motion" + stateStr, getBytes(jsonValue.getDouble("motion"), timeStamp)));

                    System.out.println("Sending value -> " + jsonValue);
                }));
    }

    public void stop(String roomName) {
        if (subscriptions.containsKey(roomName)){
            subscriptions.get(roomName).dispose();
            subscriptions.remove(roomName);
        }
    }

    Double oldValue = 20.00;
    public double temperatureValue()
    {
        Double returnValue;
        Double random = Math.random();

       if (oldValue < 18.00) {
           returnValue = oldValue + random/5;
           oldValue = returnValue;
       }
       else if (oldValue > 23.00) {
           returnValue = oldValue- random/5;
           oldValue = returnValue;
       }
       else {
           if (random > 0.6) {
               returnValue = oldValue + random/2;
               oldValue = returnValue;
           }
           else {
               returnValue = oldValue- random/2;
               oldValue = returnValue;
           }
       }
       return returnValue;
    }

    public List<Double> getGoodTemps(){
        return goodTemps;
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

    public byte[] getBytes(Object value, long timeStamp) {
        JSONObject json = new JSONObject();
        return json.put("value", value).put("timestamp", timeStamp).toString().getBytes();
    }
}
