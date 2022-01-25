package at.htl.mqtt.client.boundary;

import at.htl.mqtt.client.repository.RoomRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

@Path("/api")
public class ConfigEndpoint {

    @Inject
    RoomRepository roomRepo;

    @POST
    @Path("addRoom/{roomName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRoom(@PathParam("roomName") String roomName) {
        boolean success = roomRepo.addRoom(roomName);

        if (success){
            return Response.status(201).build();
        }

        return Response.status(400).build();
    }

    @POST
    @Path("deleteRoom/{roomName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean deleteRoom(@PathParam("roomName") String roomName) {
        return roomRepo.deleteRoom(roomName);
    }

    @PUT
    @Path("updateRoom/{roomName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean updateRoom(@PathParam("roomName") String roomName, @QueryParam("newName") String newName) {
        return roomRepo.updateRoom(roomName, newName);
    }
}
