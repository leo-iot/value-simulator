package at.htl.mqtt.client.repository;

import at.htl.mqtt.client.boundary.MyValueGenerator;
import at.htl.mqtt.client.entity.Room;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;

@ApplicationScoped
public class RoomRepository
{
    @Inject
    MyValueGenerator myValueGenerator;

    public boolean addRoom(String roomName) {
        Room currRoom = new Room(roomName);
        if(myValueGenerator.subscriptions.containsKey(roomName))
        {
            System.out.println("Room already exists");
            return false;
        }

        myValueGenerator.roomData(currRoom);
        return true;
    }

    public boolean deleteRoom(String roomName) {
        myValueGenerator.stop(roomName);
        return true;
    }

    public boolean updateRoom(String roomName, String newName){
        if(!myValueGenerator.subscriptions.containsKey(roomName))
        {
            deleteRoom(roomName);
            addRoom(newName);
            return true;
        }
        System.out.println("Room doesnt exist");
        return false;
    }
}
