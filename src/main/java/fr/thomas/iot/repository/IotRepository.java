package fr.thomas.iot.repository;

import fr.thomas.iot.domain.Iot;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Iot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IotRepository extends JpaRepository<Iot, Long> {}
