package at.htl.mqtt.client.boundary;

import at.htl.mqtt.client.dto.RoomDTO;
import at.htl.mqtt.client.dto.RoomsDTO;
import at.htl.mqtt.client.dto.ValueTypeDTO;
import at.htl.mqtt.client.entity.Room;
import at.htl.mqtt.client.entity.ValueType;
import at.htl.mqtt.client.repository.RoomRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/room")
public class RoomRessource {

    @Inject
    RoomRepository roomRepo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRooms(){
        return Response.ok(Room.listAll()).build();
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRoom(RoomDTO roomDTO){
        Room r = roomRepo.addRoom(new Room(roomDTO));
        return Response.ok(r).build();
    }

    @DELETE
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response deleteRoom(@PathParam("id") Long id){
        Room r = Room.findById(id);
        Room.deleteById(id);
        return Response.ok(r).build();
    }

    @PUT
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response updateRoom(@PathParam("id") Long id, RoomDTO roomDTO){
        Room entity = Room.findById(id);
        if(entity == null) {
            throw new NotFoundException();
        }

        entity.setName(roomDTO.name);
        entity.setFloor(roomDTO.floor);

        return Response.ok(entity).build();
    }

    @POST
    @Path("addMultipleRooms")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addMultipleRooms(RoomsDTO rooms){
        roomRepo.addMultipleRooms(rooms);
        return Response.status(400).build();
    }


}
