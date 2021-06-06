package fr.thomas.iot.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.thomas.iot.domain.Iot} entity.
 */
public class IotDTO implements Serializable {

    private Long id;

    private String mac;

    private SensorDTO sensor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public SensorDTO getSensor() {
        return sensor;
    }

    public void setSensor(SensorDTO sensor) {
        this.sensor = sensor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IotDTO)) {
            return false;
        }

        IotDTO iotDTO = (IotDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, iotDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IotDTO{" +
            "id=" + getId() +
            ", mac='" + getMac() + "'" +
            ", sensor=" + getSensor() +
            "}";
    }
}
