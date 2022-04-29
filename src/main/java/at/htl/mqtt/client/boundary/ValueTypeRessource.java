package at.htl.mqtt.client.boundary;

import at.htl.mqtt.client.dto.ValueTypeDTO;
import at.htl.mqtt.client.entity.ValueType;

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
        return Response.ok(ValueType.getEntityManager().merge(new ValueType(valueTypeDTO))).build();
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
