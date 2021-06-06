package fr.thomas.iot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TypeIot.
 */
@Entity
@Table(name = "type_iot")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TypeIot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JsonIgnoreProperties(value = { "sensor", "typeIots" }, allowSetters = true)
    private Iot iot;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeIot id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public TypeIot name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Iot getIot() {
        return this.iot;
    }

    public TypeIot iot(Iot iot) {
        this.setIot(iot);
        return this;
    }

    public void setIot(Iot iot) {
        this.iot = iot;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeIot)) {
            return false;
        }
        return id != null && id.equals(((TypeIot) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeIot{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
