package at.htl.mqtt.client.boundary;

import at.htl.mqtt.client.dto.ValueTypesDTO;
import at.htl.mqtt.client.entity.Config;
import at.htl.mqtt.client.entity.Room;
import at.htl.mqtt.client.entity.ValueType;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("view")
public class ConfigEndpoint {

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance temperatures(
                List<Integer> temps,
                List<Room> rooms,
                List<ValueType> types,
                List<Config> config
        );
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public TemplateInstance get() {
        List<Room> rooms = Room.listAll();
        List<Config> c = Config.listAll();
        List<Integer> myNum = new ArrayList<>();
        myNum.add(12);
        myNum.add(15);
        myNum.add(16);
        myNum.add(18);
        myNum.add(18);

        return Templates.temperatures(
                myNum,
                rooms,
                ValueType.listAll(),
                c
        );
    }
}
