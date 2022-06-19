package at.htl.mqtt.client.dto;


import java.util.List;

public class ValueTypesDTO {
    public List<ValueTypeDTO> valueTypeDTOS;

    public ValueTypesDTO(List<ValueTypeDTO> valueTypeDTOS) {
        this.valueTypeDTOS = valueTypeDTOS;
    }

    public ValueTypesDTO() {
    }
}
