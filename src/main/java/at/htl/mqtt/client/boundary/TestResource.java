package at.htl.mqtt.client.boundary;

import at.htl.mqtt.client.entity.Room;
import at.htl.mqtt.client.entity.Value;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

@Path("test")
public class TestResource {

    @GET
    public List<Value> testForRoom(){
        List<Room> currRoom = Room.listAll();

        return currRoom.get(0).getValues();
    }
}
