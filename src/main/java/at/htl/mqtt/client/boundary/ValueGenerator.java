package at.htl.mqtt.client.boundary;

import at.htl.mqtt.client.entity.Config;
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

import static at.htl.mqtt.client.GeneratingValuesUtils.checkSendingValues;
import static at.htl.mqtt.client.GeneratingValuesUtils.sendValueForRoom;

@ApplicationScoped
public class ValueGenerator {

    @Inject
    @Channel("topic-values")
    @OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 1000)
    Emitter<byte[]> emitter;

    @Scheduled(every = "200s")
    void sendValues() {
        if(!checkSendingValues()){
            System.out.println("Not Sending Values");
            return;
        }
        System.out.println("Sending Values");
        var rooms = Room.listAll();
        for (var room : rooms) {
            sendValueForRoom((Room) room, emitter);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

