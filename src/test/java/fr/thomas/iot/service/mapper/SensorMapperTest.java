package fr.thomas.iot.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SensorMapperTest {

    private SensorMapper sensorMapper;

    @BeforeEach
    public void setUp() {
        sensorMapper = new SensorMapperImpl();
    }
}
