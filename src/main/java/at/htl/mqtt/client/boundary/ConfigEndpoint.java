package at.htl.mqtt.client.boundary;

import at.htl.mqtt.client.repository.RoomRepository;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

 @RequestScoped
@Path("/api")
public class ConfigEndpoint {

    @Inject
    RoomRepository roomRepo;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance temperatures(List<Double> temps);
    }

    @GET
    @Path("temperature")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get() {
        return Templates.temperatures(roomRepo.getCurrTemp());
    }

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

    @DELETE
    @Path("deleteRoom/{roomName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRoom(@PathParam("roomName") String roomName) {
        boolean success = roomRepo.deleteRoom(roomName);

        if (success){
            return Response.ok().build();
        }

        return Response.status(400).build();
    }

    @PUT
    @Path("updateRoom/{roomName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRoom(@PathParam("roomName") String roomName, @QueryParam("newName") String newName) {
        boolean success = roomRepo.updateRoom(roomName, newName);

        if (success){
            return Response.ok().build();
        }

        return Response.status(400).build();
    }


}
