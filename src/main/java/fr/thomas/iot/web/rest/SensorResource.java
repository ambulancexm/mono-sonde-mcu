package fr.thomas.iot.web.rest;

import fr.thomas.iot.repository.SensorRepository;
import fr.thomas.iot.service.SensorService;
import fr.thomas.iot.service.dto.SensorDTO;
import fr.thomas.iot.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.thomas.iot.domain.Sensor}.
 */
@RestController
@RequestMapping("/api")
public class SensorResource {

    private final Logger log = LoggerFactory.getLogger(SensorResource.class);

    private static final String ENTITY_NAME = "sensor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SensorService sensorService;

    private final SensorRepository sensorRepository;

    public SensorResource(SensorService sensorService, SensorRepository sensorRepository) {
        this.sensorService = sensorService;
        this.sensorRepository = sensorRepository;
    }

    /**
     * {@code POST  /sensors} : Create a new sensor.
     *
     * @param sensorDTO the sensorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sensorDTO, or with status {@code 400 (Bad Request)} if the sensor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sensors")
    public ResponseEntity<SensorDTO> createSensor(@RequestBody SensorDTO sensorDTO) throws URISyntaxException {
        log.debug("REST request to save Sensor : {}", sensorDTO);
        if (sensorDTO.getId() != null) {
            throw new BadRequestAlertException("A new sensor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SensorDTO result = sensorService.save(sensorDTO);
        return ResponseEntity
            .created(new URI("/api/sensors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sensors/:id} : Updates an existing sensor.
     *
     * @param id the id of the sensorDTO to save.
     * @param sensorDTO the sensorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sensorDTO,
     * or with status {@code 400 (Bad Request)} if the sensorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sensorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sensors/{id}")
    public ResponseEntity<SensorDTO> updateSensor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SensorDTO sensorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Sensor : {}, {}", id, sensorDTO);
        if (sensorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sensorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sensorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SensorDTO result = sensorService.save(sensorDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sensorDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sensors/:id} : Partial updates given fields of an existing sensor, field will ignore if it is null
     *
     * @param id the id of the sensorDTO to save.
     * @param sensorDTO the sensorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sensorDTO,
     * or with status {@code 400 (Bad Request)} if the sensorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sensorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sensorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sensors/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SensorDTO> partialUpdateSensor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SensorDTO sensorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Sensor partially : {}, {}", id, sensorDTO);
        if (sensorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sensorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sensorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SensorDTO> result = sensorService.partialUpdate(sensorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sensorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sensors} : get all the sensors.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sensors in body.
     */
    @GetMapping("/sensors")
    public List<SensorDTO> getAllSensors() {
        log.debug("REST request to get all Sensors");
        return sensorService.findAll();
    }

    /**
     * {@code GET  /sensors/:id} : get the "id" sensor.
     *
     * @param id the id of the sensorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sensorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sensors/{id}")
    public ResponseEntity<SensorDTO> getSensor(@PathVariable Long id) {
        log.debug("REST request to get Sensor : {}", id);
        Optional<SensorDTO> sensorDTO = sensorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sensorDTO);
    }

    /**
     * {@code DELETE  /sensors/:id} : delete the "id" sensor.
     *
     * @param id the id of the sensorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sensors/{id}")
    public ResponseEntity<Void> deleteSensor(@PathVariable Long id) {
        log.debug("REST request to delete Sensor : {}", id);
        sensorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
