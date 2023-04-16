package at.htl.mqtt.client;

import at.htl.mqtt.client.entity.Config;
import at.htl.mqtt.client.entity.Room;
import at.htl.mqtt.client.entity.Value;
import io.quarkus.scheduler.Scheduled;
import io.smallrye.reactive.messaging.mqtt.MqttMessage;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.json.JSONObject;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

public class GeneratingValuesUtils {
    private static List<Double> goodTemps = new LinkedList<>();
    private static final int AMOUNT_OF_ITERATIONS = 60;

    public static boolean checkSendingValues() {
        List<Config> c = Config.listAll();
        if (c.size() == 0)
            return true;
        return c.get(0).sendValues;
    }

    @Scheduled(every = "60s")
    public void getValuesForRoom1() {
        //List<Room> rooms = Room.listAll();
        List<Value> vs = getValuesForRoom();
    }


    @Transactional
    public static List<Value> getValuesForRoom() {
        List<Room> rooms = Room.listAll();
        Room room = rooms.get(1);

        int index = 0;
        Random rand = new Random();

        for (Value v : room.getValues()) {

            // PREPARE VALUE
            if (v.getLastFullValue() == 0) {
                v.setLastFullValue(
                        rand.nextDouble(
                                v.getValueType().getMinValue(),
                                v.getValueType().getMaxValue()
                        ));
            }
            // update next Full-Value when not set
            if (v.getNextFullValue() == 0) {
                v.setNextFullValue(
                        rand.nextDouble(
                                v.getValueType().getMinValue(),
                                v.getValueType().getMaxValue()
                        ));
            }
            if (v.getAmountOfIterations() != AMOUNT_OF_ITERATIONS) {
                v.setAmountOfIterations(AMOUNT_OF_ITERATIONS);
            }

            double currYValue = calcPointOnStraight(
                    v.getLastFullValue(), // Y-1
                    v.getAmountOfIterations(), // X-2
                    v.getNextFullValue(), // Y-2
                    v.getIterationsCount() // X-Current
            );

            double currentDeviatingPoint = getIntDeviation(
                    v.getValueType().getMinValue(),
                    v.getValueType().getMaxValue(),
                    currYValue
            );

            if (v.getIterationsCount() == v.getAmountOfIterations()) {
                // reset count
                v.setIterationsCount(0);

                // update next value to random
                v.setNextFullValue(
                        rand.nextDouble(
                                v.getValueType().getMinValue(),
                                v.getValueType().getMaxValue()
                        ));
                // set "last"-value to current
                v.setLastFullValue(currYValue);

            } else {
                // Increase count
                v.setIterationsCount(v.getIterationsCount() + 1);
            }

            //room.persist();
            room.getValues().get(index).setLastValue(currentDeviatingPoint);
            index++;
        }
        return room.getValues();
    }


    // returns the Y-Value of a given X-Value
    // on a straight between 2 given Points
    public static Double calcPointOnStraight(
            double y1, // X-1 = 0
            int x2, double y2,
            int xCurrent) {
        int x1 = 0;

        double k = (y2 - y1) / (x2 - x1);

        // y(x) = k * x + d
        // y2 = k * x2 + d     // - (k * x2)
        // d = y2 - (k * x2)
        double d = y2 - (k * x2);

        // y(x) = k * x + d
        return k * xCurrent + d;
    }

    // returns a 10-percent deviation of value
    public static Double getIntDeviation(int min, int max, double value) {
        Random rand = new Random();

        // if value is not set -> set value to random value
        if (value <= 0 && min <= 0) return rand.nextDouble(min, max);

        // Check if value is valid
        if (value < min) value = min;
        if (value > max) value = max;

        double temp = value * rand.nextDouble(0.01, 0.10);

        // Randomize if value increases/decreases
        boolean subtract = rand.nextBoolean();

        double subtractedValue = value - temp;
        double addedValue = value + temp;

        // Check if CURRENT value is valid
        if (subtractedValue < min) subtractedValue = min;
        if (addedValue > max) addedValue = max;


        if (subtract) {
            // SUBTRACT 10 PERCENT

            if (subtractedValue <= min) return addedValue;

            return subtractedValue;
        } else {
            // ADD 10 PERCENT

            if (addedValue >= max) return subtractedValue;

            return addedValue;
        }
    }


    public static void sendValueForRoom(Room room, Emitter<byte[]> emitter) {
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

    public static Object getValue(Value value) {
        return newRandomValue(value);
    }

    private static double newRandomValue(Value value) {
        double r = Math.random() * value.getValueType().getMultiplier();
        double newVal = value.getLastValue();

        if (newVal == 0) {
            newVal = (double) (value.getValueType().getMinValue() + value.getValueType().getMaxValue()) / 2;
        }

        if (Math.round(Math.random()) == 0) {
            newVal += r;
        } else {
            newVal -= r;
        }

        if (newVal > value.getValueType().getMaxValue() || newVal < value.getValueType().getMinValue()) {
            newVal = newRandomValue(value);
        }
        value.setLastValue(newVal);
        if (value.getValueType().isOnlyInteger()) {
            return Math.round(newVal);
        } else {
            return newVal;
        }
    }


    public static byte[] getBytes(Object value, long timeStamp) {
        JSONObject json = new JSONObject();
        return json.put("value", value).put("timestamp", timeStamp).toString().getBytes();
    }


    public static double goodTemp() {
        double hours = LocalDateTime.now().getHour() + ((LocalDateTime.now().getMinute() / 60.00 * 100.00) / 100.00);
        //return (-0.01 * Math.pow(hours,3) + 0.2 * Math.pow(hours,2) + 10)+10;

        double sum1 = -0.014 * Math.pow(hours, 3);
        double sum2 = 0.4 * Math.pow(hours, 2);
        double sum3 = -1.8 * hours + 11;

        //System.out.println(sum1);
        //System.out.println(sum2);
        //System.out.println(sum3);
        //System.out.println(hours);
        //System.out.println(LocalDateTime.now().getMinute());

        double sum = sum1 + sum2 + sum3;

        goodTemps.add(sum);

        return sum1 + sum2 + sum3;
    }
}
