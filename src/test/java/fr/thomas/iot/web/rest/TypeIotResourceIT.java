package fr.thomas.iot.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.thomas.iot.IntegrationTest;
import fr.thomas.iot.domain.TypeIot;
import fr.thomas.iot.repository.TypeIotRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TypeIotResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeIotResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-iots";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeIotRepository typeIotRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeIotMockMvc;

    private TypeIot typeIot;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeIot createEntity(EntityManager em) {
        TypeIot typeIot = new TypeIot().name(DEFAULT_NAME);
        return typeIot;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeIot createUpdatedEntity(EntityManager em) {
        TypeIot typeIot = new TypeIot().name(UPDATED_NAME);
        return typeIot;
    }

    @BeforeEach
    public void initTest() {
        typeIot = createEntity(em);
    }

    @Test
    @Transactional
    void createTypeIot() throws Exception {
        int databaseSizeBeforeCreate = typeIotRepository.findAll().size();
        // Create the TypeIot
        restTypeIotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeIot)))
            .andExpect(status().isCreated());

        // Validate the TypeIot in the database
        List<TypeIot> typeIotList = typeIotRepository.findAll();
        assertThat(typeIotList).hasSize(databaseSizeBeforeCreate + 1);
        TypeIot testTypeIot = typeIotList.get(typeIotList.size() - 1);
        assertThat(testTypeIot.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createTypeIotWithExistingId() throws Exception {
        // Create the TypeIot with an existing ID
        typeIot.setId(1L);

        int databaseSizeBeforeCreate = typeIotRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeIotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeIot)))
            .andExpect(status().isBadRequest());

        // Validate the TypeIot in the database
        List<TypeIot> typeIotList = typeIotRepository.findAll();
        assertThat(typeIotList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTypeIots() throws Exception {
        // Initialize the database
        typeIotRepository.saveAndFlush(typeIot);

        // Get all the typeIotList
        restTypeIotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeIot.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getTypeIot() throws Exception {
        // Initialize the database
        typeIotRepository.saveAndFlush(typeIot);

        // Get the typeIot
        restTypeIotMockMvc
            .perform(get(ENTITY_API_URL_ID, typeIot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeIot.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingTypeIot() throws Exception {
        // Get the typeIot
        restTypeIotMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTypeIot() throws Exception {
        // Initialize the database
        typeIotRepository.saveAndFlush(typeIot);

        int databaseSizeBeforeUpdate = typeIotRepository.findAll().size();

        // Update the typeIot
        TypeIot updatedTypeIot = typeIotRepository.findById(typeIot.getId()).get();
        // Disconnect from session so that the updates on updatedTypeIot are not directly saved in db
        em.detach(updatedTypeIot);
        updatedTypeIot.name(UPDATED_NAME);

        restTypeIotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTypeIot.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTypeIot))
            )
            .andExpect(status().isOk());

        // Validate the TypeIot in the database
        List<TypeIot> typeIotList = typeIotRepository.findAll();
        assertThat(typeIotList).hasSize(databaseSizeBeforeUpdate);
        TypeIot testTypeIot = typeIotList.get(typeIotList.size() - 1);
        assertThat(testTypeIot.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingTypeIot() throws Exception {
        int databaseSizeBeforeUpdate = typeIotRepository.findAll().size();
        typeIot.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeIotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeIot.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeIot))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeIot in the database
        List<TypeIot> typeIotList = typeIotRepository.findAll();
        assertThat(typeIotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeIot() throws Exception {
        int databaseSizeBeforeUpdate = typeIotRepository.findAll().size();
        typeIot.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeIotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeIot))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeIot in the database
        List<TypeIot> typeIotList = typeIotRepository.findAll();
        assertThat(typeIotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeIot() throws Exception {
        int databaseSizeBeforeUpdate = typeIotRepository.findAll().size();
        typeIot.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeIotMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeIot)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeIot in the database
        List<TypeIot> typeIotList = typeIotRepository.findAll();
        assertThat(typeIotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeIotWithPatch() throws Exception {
        // Initialize the database
        typeIotRepository.saveAndFlush(typeIot);

        int databaseSizeBeforeUpdate = typeIotRepository.findAll().size();

        // Update the typeIot using partial update
        TypeIot partialUpdatedTypeIot = new TypeIot();
        partialUpdatedTypeIot.setId(typeIot.getId());

        partialUpdatedTypeIot.name(UPDATED_NAME);

        restTypeIotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeIot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeIot))
            )
            .andExpect(status().isOk());

        // Validate the TypeIot in the database
        List<TypeIot> typeIotList = typeIotRepository.findAll();
        assertThat(typeIotList).hasSize(databaseSizeBeforeUpdate);
        TypeIot testTypeIot = typeIotList.get(typeIotList.size() - 1);
        assertThat(testTypeIot.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateTypeIotWithPatch() throws Exception {
        // Initialize the database
        typeIotRepository.saveAndFlush(typeIot);

        int databaseSizeBeforeUpdate = typeIotRepository.findAll().size();

        // Update the typeIot using partial update
        TypeIot partialUpdatedTypeIot = new TypeIot();
        partialUpdatedTypeIot.setId(typeIot.getId());

        partialUpdatedTypeIot.name(UPDATED_NAME);

        restTypeIotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeIot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeIot))
            )
            .andExpect(status().isOk());

        // Validate the TypeIot in the database
        List<TypeIot> typeIotList = typeIotRepository.findAll();
        assertThat(typeIotList).hasSize(databaseSizeBeforeUpdate);
        TypeIot testTypeIot = typeIotList.get(typeIotList.size() - 1);
        assertThat(testTypeIot.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingTypeIot() throws Exception {
        int databaseSizeBeforeUpdate = typeIotRepository.findAll().size();
        typeIot.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeIotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeIot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeIot))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeIot in the database
        List<TypeIot> typeIotList = typeIotRepository.findAll();
        assertThat(typeIotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeIot() throws Exception {
        int databaseSizeBeforeUpdate = typeIotRepository.findAll().size();
        typeIot.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeIotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeIot))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeIot in the database
        List<TypeIot> typeIotList = typeIotRepository.findAll();
        assertThat(typeIotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeIot() throws Exception {
        int databaseSizeBeforeUpdate = typeIotRepository.findAll().size();
        typeIot.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeIotMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(typeIot)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeIot in the database
        List<TypeIot> typeIotList = typeIotRepository.findAll();
        assertThat(typeIotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypeIot() throws Exception {
        // Initialize the database
        typeIotRepository.saveAndFlush(typeIot);

        int databaseSizeBeforeDelete = typeIotRepository.findAll().size();

        // Delete the typeIot
        restTypeIotMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeIot.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeIot> typeIotList = typeIotRepository.findAll();
        assertThat(typeIotList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
