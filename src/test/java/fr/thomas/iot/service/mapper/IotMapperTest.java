package fr.thomas.iot.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IotMapperTest {

    private IotMapper iotMapper;

    @BeforeEach
    public void setUp() {
        iotMapper = new IotMapperImpl();
    }
}
