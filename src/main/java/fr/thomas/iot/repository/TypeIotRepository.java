package fr.thomas.iot.repository;

import fr.thomas.iot.domain.TypeIot;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TypeIot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeIotRepository extends JpaRepository<TypeIot, Long> {}
