package at.htl.mqtt.client.boundary;

import at.htl.mqtt.client.dto.ValueTypeDTO;
import at.htl.mqtt.client.dto.ValueTypesDTO;
import at.htl.mqtt.client.entity.Room;
import at.htl.mqtt.client.entity.Value;
import at.htl.mqtt.client.entity.ValueType;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/valueType")
public class ValueTypeRessource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getValueTypes(){
        return Response.ok(ValueType.listAll()).build();
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addValueType(ValueTypeDTO valueTypeDTO){
        ValueType vt = new ValueType(valueTypeDTO);
        ValueType.persist(vt);

        for (var r : Room.listAll()) {
            Value v = new Value((Room) r, vt);
            Value.persist(v);
        }

        return Response.ok(vt).build();
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("addMultipleValueTypes")
    public Response addMultipleValueTypes(ValueTypesDTO valueTypesDTO){

        for (ValueTypeDTO valueTypeDTO : valueTypesDTO.valueTypeDTOS) {
            addValueType(valueTypeDTO);
        }

        return Response.status(200).build();
    }

    @DELETE
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response deleteValueType(@PathParam("id") Long id){
        ValueType vt = ValueType.findById(id);
        ValueType.deleteById(id);
        return Response.ok(vt).build();
    }

    @PUT
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response updateValueType(@PathParam("id") Long id, ValueTypeDTO valueTypeDTO){
        ValueType entity = ValueType.findById(id);
        if(entity == null) {
            throw new NotFoundException();
        }

        entity.setName(valueTypeDTO.name);
        entity.setMinValue(valueTypeDTO.minValue);
        entity.setMaxValue(valueTypeDTO.maxValue);

        return Response.ok(entity).build();
    }
}
