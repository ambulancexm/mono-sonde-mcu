package fr.thomas.iot.web.rest;

import fr.thomas.iot.repository.IotRepository;
import fr.thomas.iot.service.IotService;
import fr.thomas.iot.service.dto.IotDTO;
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
 * REST controller for managing {@link fr.thomas.iot.domain.Iot}.
 */
@RestController
@RequestMapping("/api")
public class IotResource {

    private final Logger log = LoggerFactory.getLogger(IotResource.class);

    private static final String ENTITY_NAME = "iot";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IotService iotService;

    private final IotRepository iotRepository;

    public IotResource(IotService iotService, IotRepository iotRepository) {
        this.iotService = iotService;
        this.iotRepository = iotRepository;
    }

    /**
     * {@code POST  /iots} : Create a new iot.
     *
     * @param iotDTO the iotDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new iotDTO, or with status {@code 400 (Bad Request)} if the iot has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/iots")
    public ResponseEntity<IotDTO> createIot(@RequestBody IotDTO iotDTO) throws URISyntaxException {
        log.debug("REST request to save Iot : {}", iotDTO);
        if (iotDTO.getId() != null) {
            throw new BadRequestAlertException("A new iot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IotDTO result = iotService.save(iotDTO);
        return ResponseEntity
            .created(new URI("/api/iots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /iots/:id} : Updates an existing iot.
     *
     * @param id the id of the iotDTO to save.
     * @param iotDTO the iotDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iotDTO,
     * or with status {@code 400 (Bad Request)} if the iotDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the iotDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/iots/{id}")
    public ResponseEntity<IotDTO> updateIot(@PathVariable(value = "id", required = false) final Long id, @RequestBody IotDTO iotDTO)
        throws URISyntaxException {
        log.debug("REST request to update Iot : {}, {}", id, iotDTO);
        if (iotDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iotDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!iotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IotDTO result = iotService.save(iotDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, iotDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /iots/:id} : Partial updates given fields of an existing iot, field will ignore if it is null
     *
     * @param id the id of the iotDTO to save.
     * @param iotDTO the iotDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iotDTO,
     * or with status {@code 400 (Bad Request)} if the iotDTO is not valid,
     * or with status {@code 404 (Not Found)} if the iotDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the iotDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/iots/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<IotDTO> partialUpdateIot(@PathVariable(value = "id", required = false) final Long id, @RequestBody IotDTO iotDTO)
        throws URISyntaxException {
        log.debug("REST request to partial update Iot partially : {}, {}", id, iotDTO);
        if (iotDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iotDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!iotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IotDTO> result = iotService.partialUpdate(iotDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, iotDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /iots} : get all the iots.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of iots in body.
     */
    @GetMapping("/iots")
    public List<IotDTO> getAllIots() {
        log.debug("REST request to get all Iots");
        return iotService.findAll();
    }

    /**
     * {@code GET  /iots/:id} : get the "id" iot.
     *
     * @param id the id of the iotDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the iotDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/iots/{id}")
    public ResponseEntity<IotDTO> getIot(@PathVariable Long id) {
        log.debug("REST request to get Iot : {}", id);
        Optional<IotDTO> iotDTO = iotService.findOne(id);
        return ResponseUtil.wrapOrNotFound(iotDTO);
    }

    /**
     * {@code DELETE  /iots/:id} : delete the "id" iot.
     *
     * @param id the id of the iotDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/iots/{id}")
    public ResponseEntity<Void> deleteIot(@PathVariable Long id) {
        log.debug("REST request to delete Iot : {}", id);
        iotService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
