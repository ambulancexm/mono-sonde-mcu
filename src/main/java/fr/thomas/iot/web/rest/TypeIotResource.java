package fr.thomas.iot.web.rest;

import fr.thomas.iot.domain.TypeIot;
import fr.thomas.iot.repository.TypeIotRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.thomas.iot.domain.TypeIot}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TypeIotResource {

    private final Logger log = LoggerFactory.getLogger(TypeIotResource.class);

    private static final String ENTITY_NAME = "typeIot";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeIotRepository typeIotRepository;

    public TypeIotResource(TypeIotRepository typeIotRepository) {
        this.typeIotRepository = typeIotRepository;
    }

    /**
     * {@code POST  /type-iots} : Create a new typeIot.
     *
     * @param typeIot the typeIot to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeIot, or with status {@code 400 (Bad Request)} if the typeIot has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-iots")
    public ResponseEntity<TypeIot> createTypeIot(@RequestBody TypeIot typeIot) throws URISyntaxException {
        log.debug("REST request to save TypeIot : {}", typeIot);
        if (typeIot.getId() != null) {
            throw new BadRequestAlertException("A new typeIot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeIot result = typeIotRepository.save(typeIot);
        return ResponseEntity
            .created(new URI("/api/type-iots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-iots/:id} : Updates an existing typeIot.
     *
     * @param id the id of the typeIot to save.
     * @param typeIot the typeIot to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeIot,
     * or with status {@code 400 (Bad Request)} if the typeIot is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeIot couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-iots/{id}")
    public ResponseEntity<TypeIot> updateTypeIot(@PathVariable(value = "id", required = false) final Long id, @RequestBody TypeIot typeIot)
        throws URISyntaxException {
        log.debug("REST request to update TypeIot : {}, {}", id, typeIot);
        if (typeIot.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeIot.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeIotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypeIot result = typeIotRepository.save(typeIot);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeIot.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /type-iots/:id} : Partial updates given fields of an existing typeIot, field will ignore if it is null
     *
     * @param id the id of the typeIot to save.
     * @param typeIot the typeIot to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeIot,
     * or with status {@code 400 (Bad Request)} if the typeIot is not valid,
     * or with status {@code 404 (Not Found)} if the typeIot is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeIot couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/type-iots/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TypeIot> partialUpdateTypeIot(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TypeIot typeIot
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeIot partially : {}, {}", id, typeIot);
        if (typeIot.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeIot.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeIotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeIot> result = typeIotRepository
            .findById(typeIot.getId())
            .map(
                existingTypeIot -> {
                    if (typeIot.getName() != null) {
                        existingTypeIot.setName(typeIot.getName());
                    }

                    return existingTypeIot;
                }
            )
            .map(typeIotRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeIot.getId().toString())
        );
    }

    /**
     * {@code GET  /type-iots} : get all the typeIots.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeIots in body.
     */
    @GetMapping("/type-iots")
    public List<TypeIot> getAllTypeIots() {
        log.debug("REST request to get all TypeIots");
        return typeIotRepository.findAll();
    }

    /**
     * {@code GET  /type-iots/:id} : get the "id" typeIot.
     *
     * @param id the id of the typeIot to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeIot, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-iots/{id}")
    public ResponseEntity<TypeIot> getTypeIot(@PathVariable Long id) {
        log.debug("REST request to get TypeIot : {}", id);
        Optional<TypeIot> typeIot = typeIotRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(typeIot);
    }

    /**
     * {@code DELETE  /type-iots/:id} : delete the "id" typeIot.
     *
     * @param id the id of the typeIot to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-iots/{id}")
    public ResponseEntity<Void> deleteTypeIot(@PathVariable Long id) {
        log.debug("REST request to delete TypeIot : {}", id);
        typeIotRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
