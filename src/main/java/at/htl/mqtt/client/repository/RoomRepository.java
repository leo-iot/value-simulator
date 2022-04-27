package at.htl.mqtt.client.repository;

import at.htl.mqtt.client.boundary.MyValueGenerator;
import at.htl.mqtt.client.dto.RoomsDTO;
import at.htl.mqtt.client.entity.Room;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class RoomRepository
{
    @Inject
    MyValueGenerator myValueGenerator;

    public boolean addRoom(String roomName, String floor) {
        Room currRoom = new Room(roomName, floor);
        if(myValueGenerator.subscriptions.containsKey(roomName))
        {
            System.out.println("Room already exists");
            return false;
        }

        myValueGenerator.roomData(currRoom);
        return true;
    }

    public List<Double> getCurrTemp(){
        return myValueGenerator.getGoodTemps();
    }

    public boolean deleteRoom(String roomName) {
        myValueGenerator.stop(roomName);
        return true;
    }

    public boolean updateRoom(String roomName, String newName){
        if(!myValueGenerator.subscriptions.containsKey(roomName))
        {
            deleteRoom(roomName);
            //addRoom(newName);
            return true;
        }
        System.out.println("Room doesnt exist");
        return false;
    }

    public void addMultipleRooms(RoomsDTO rooms) {
        addFloor(rooms.og2,"og2");
        addFloor(rooms.og,"og");
        addFloor(rooms.eg,"eg");
        addFloor(rooms.ug,"ug");
    }

    private void addFloor(ArrayList<String> rooms, String floor){
        for (var room :
                rooms) {
            addRoom(room, floor);
        }
    }
}
