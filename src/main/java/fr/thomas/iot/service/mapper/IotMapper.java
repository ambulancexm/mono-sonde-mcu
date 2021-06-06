package fr.thomas.iot.service.mapper;

import fr.thomas.iot.domain.*;
import fr.thomas.iot.service.dto.IotDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Iot} and its DTO {@link IotDTO}.
 */
@Mapper(componentModel = "spring", uses = { SensorMapper.class })
public interface IotMapper extends EntityMapper<IotDTO, Iot> {
    @Mapping(target = "sensor", source = "sensor", qualifiedByName = "id")
    IotDTO toDto(Iot s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    IotDTO toDtoId(Iot iot);
}
