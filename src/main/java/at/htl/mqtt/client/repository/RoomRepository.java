package at.htl.mqtt.client.repository;

import at.htl.mqtt.client.dto.RoomsDTO;
import at.htl.mqtt.client.entity.Room;
import at.htl.mqtt.client.entity.Value;
import at.htl.mqtt.client.entity.ValueType;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class RoomRepository
{
    public void addMultipleRooms(RoomsDTO rooms) {
        addFloor(rooms.og2,"og2");
        addFloor(rooms.og,"og");
        addFloor(rooms.eg,"eg");
        addFloor(rooms.ug,"ug");
    }

    public void addFloor(ArrayList<String> rooms, String floor){
        for (var room :
                rooms) {
            addRoom(new Room(room, floor));
        }
    }

    @Transactional
    public Room addRoom(Room room){
        var vts = ValueType.listAll();
        for (var vt : vts) {
            Value v = new Value(room, (ValueType) vt);
            v.setRoom(room);
            v.setValueType((ValueType) vt);
            Value.persist(v);
        }
        return Room.getEntityManager().merge(room);
    }
}
