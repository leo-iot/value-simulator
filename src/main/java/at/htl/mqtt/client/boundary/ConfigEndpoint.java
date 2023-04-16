package at.htl.mqtt.client.boundary;

import at.htl.mqtt.client.GeneratingValuesUtils;
import at.htl.mqtt.client.dto.ValueTypesDTO;
import at.htl.mqtt.client.entity.Config;
import at.htl.mqtt.client.entity.Room;
import at.htl.mqtt.client.entity.Value;
import at.htl.mqtt.client.entity.ValueType;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Path("view")
public class ConfigEndpoint {

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance temperatures(
                List<Double> temps,
                List<Room> rooms,
                List<ValueType> types,
                List<Config> config,
                List<Double> tempsMidLane
        );
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public TemplateInstance get() {
        List<Room> rooms = Room.listAll();
        List<Config> c = Config.listAll();
        List<Double> myNum = new ArrayList<>();
        List<Double> tempsMidLane = new ArrayList<>();

        List<Value> values = new ArrayList<>();

        // Generate Test Data
        for (int i = 0; i < 240; i++) {
            values = GeneratingValuesUtils.getValuesForRoom();

            for (Value v : values) {
                if (Objects.equals(v.getValueType().getName(), "temperature")) {
                    myNum.add(v.getLastValue());
                    tempsMidLane.add(v.getNextFullValue());
                }
            }
        }

        return Templates.temperatures(
                myNum,
                rooms,
                ValueType.listAll(),
                c,
                tempsMidLane
        );
    }
}
