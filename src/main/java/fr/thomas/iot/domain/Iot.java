package fr.thomas.iot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Iot.
 */
@Entity
@Table(name = "iot")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Iot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mac")
    private String mac;

    @ManyToOne
    @JsonIgnoreProperties(value = { "iots" }, allowSetters = true)
    private Sensor sensor;

    @OneToMany(mappedBy = "iot")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "iot" }, allowSetters = true)
    private Set<TypeIot> typeIots = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Iot id(Long id) {
        this.id = id;
        return this;
    }

    public String getMac() {
        return this.mac;
    }

    public Iot mac(String mac) {
        this.mac = mac;
        return this;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Sensor getSensor() {
        return this.sensor;
    }

    public Iot sensor(Sensor sensor) {
        this.setSensor(sensor);
        return this;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public Set<TypeIot> getTypeIots() {
        return this.typeIots;
    }

    public Iot typeIots(Set<TypeIot> typeIots) {
        this.setTypeIots(typeIots);
        return this;
    }

    public Iot addTypeIot(TypeIot typeIot) {
        this.typeIots.add(typeIot);
        typeIot.setIot(this);
        return this;
    }

    public Iot removeTypeIot(TypeIot typeIot) {
        this.typeIots.remove(typeIot);
        typeIot.setIot(null);
        return this;
    }

    public void setTypeIots(Set<TypeIot> typeIots) {
        if (this.typeIots != null) {
            this.typeIots.forEach(i -> i.setIot(null));
        }
        if (typeIots != null) {
            typeIots.forEach(i -> i.setIot(this));
        }
        this.typeIots = typeIots;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Iot)) {
            return false;
        }
        return id != null && id.equals(((Iot) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Iot{" +
            "id=" + getId() +
            ", mac='" + getMac() + "'" +
            "}";
    }
}
