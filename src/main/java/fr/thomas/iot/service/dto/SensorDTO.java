package fr.thomas.iot.service.dto;

import fr.thomas.iot.domain.enumeration.TypeSensor;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.thomas.iot.domain.Sensor} entity.
 */
public class SensorDTO implements Serializable {

    private Long id;

    private TypeSensor typeSensor;

    private Double value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeSensor getTypeSensor() {
        return typeSensor;
    }

    public void setTypeSensor(TypeSensor typeSensor) {
        this.typeSensor = typeSensor;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SensorDTO)) {
            return false;
        }

        SensorDTO sensorDTO = (SensorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sensorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SensorDTO{" +
            "id=" + getId() +
            ", typeSensor='" + getTypeSensor() + "'" +
            ", value=" + getValue() +
            "}";
    }
}
