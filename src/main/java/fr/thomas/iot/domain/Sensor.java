package fr.thomas.iot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.thomas.iot.domain.enumeration.TypeSensor;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Sensor.
 */
@Entity
@Table(name = "sensor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Sensor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_sensor")
    private TypeSensor typeSensor;

    @Column(name = "value")
    private Double value;

    @OneToMany(mappedBy = "sensor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sensor", "typeIots" }, allowSetters = true)
    private Set<Iot> iots = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sensor id(Long id) {
        this.id = id;
        return this;
    }

    public TypeSensor getTypeSensor() {
        return this.typeSensor;
    }

    public Sensor typeSensor(TypeSensor typeSensor) {
        this.typeSensor = typeSensor;
        return this;
    }

    public void setTypeSensor(TypeSensor typeSensor) {
        this.typeSensor = typeSensor;
    }

    public Double getValue() {
        return this.value;
    }

    public Sensor value(Double value) {
        this.value = value;
        return this;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Set<Iot> getIots() {
        return this.iots;
    }

    public Sensor iots(Set<Iot> iots) {
        this.setIots(iots);
        return this;
    }

    public Sensor addIot(Iot iot) {
        this.iots.add(iot);
        iot.setSensor(this);
        return this;
    }

    public Sensor removeIot(Iot iot) {
        this.iots.remove(iot);
        iot.setSensor(null);
        return this;
    }

    public void setIots(Set<Iot> iots) {
        if (this.iots != null) {
            this.iots.forEach(i -> i.setSensor(null));
        }
        if (iots != null) {
            iots.forEach(i -> i.setSensor(this));
        }
        this.iots = iots;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sensor)) {
            return false;
        }
        return id != null && id.equals(((Sensor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sensor{" +
            "id=" + getId() +
            ", typeSensor='" + getTypeSensor() + "'" +
            ", value=" + getValue() +
            "}";
    }
}
