package at.htl.mqtt.client.boundary;


import at.htl.mqtt.client.dto.ConfigDto;
import at.htl.mqtt.client.entity.Config;

import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;

@Path("config")
public class ConfigResource {

    @POST
    @Transactional
    public boolean changeConfig(ConfigDto configDto){
        var c = new Config();
        c.sendValues = configDto.sendValues;
        List<Config> configs = Config.listAll();
        if(configs.size() == 0){
            c.persist();
            return c.sendValues;
        }
        Config.update("sendvalues = ?1",configDto.sendValues);
        return configDto.sendValues;
    }
}
