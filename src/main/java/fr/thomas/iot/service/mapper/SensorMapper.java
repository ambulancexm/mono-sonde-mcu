package fr.thomas.iot.service.mapper;

import fr.thomas.iot.domain.*;
import fr.thomas.iot.service.dto.SensorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sensor} and its DTO {@link SensorDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SensorMapper extends EntityMapper<SensorDTO, Sensor> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SensorDTO toDtoId(Sensor sensor);
}
