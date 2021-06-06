package fr.thomas.iot.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.thomas.iot.IntegrationTest;
import fr.thomas.iot.domain.Iot;
import fr.thomas.iot.repository.IotRepository;
import fr.thomas.iot.service.dto.IotDTO;
import fr.thomas.iot.service.mapper.IotMapper;
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
 * Integration tests for the {@link IotResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IotResourceIT {

    private static final String DEFAULT_MAC = "AAAAAAAAAA";
    private static final String UPDATED_MAC = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/iots";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IotRepository iotRepository;

    @Autowired
    private IotMapper iotMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIotMockMvc;

    private Iot iot;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Iot createEntity(EntityManager em) {
        Iot iot = new Iot().mac(DEFAULT_MAC);
        return iot;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Iot createUpdatedEntity(EntityManager em) {
        Iot iot = new Iot().mac(UPDATED_MAC);
        return iot;
    }

    @BeforeEach
    public void initTest() {
        iot = createEntity(em);
    }

    @Test
    @Transactional
    void createIot() throws Exception {
        int databaseSizeBeforeCreate = iotRepository.findAll().size();
        // Create the Iot
        IotDTO iotDTO = iotMapper.toDto(iot);
        restIotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(iotDTO)))
            .andExpect(status().isCreated());

        // Validate the Iot in the database
        List<Iot> iotList = iotRepository.findAll();
        assertThat(iotList).hasSize(databaseSizeBeforeCreate + 1);
        Iot testIot = iotList.get(iotList.size() - 1);
        assertThat(testIot.getMac()).isEqualTo(DEFAULT_MAC);
    }

    @Test
    @Transactional
    void createIotWithExistingId() throws Exception {
        // Create the Iot with an existing ID
        iot.setId(1L);
        IotDTO iotDTO = iotMapper.toDto(iot);

        int databaseSizeBeforeCreate = iotRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(iotDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Iot in the database
        List<Iot> iotList = iotRepository.findAll();
        assertThat(iotList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIots() throws Exception {
        // Initialize the database
        iotRepository.saveAndFlush(iot);

        // Get all the iotList
        restIotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(iot.getId().intValue())))
            .andExpect(jsonPath("$.[*].mac").value(hasItem(DEFAULT_MAC)));
    }

    @Test
    @Transactional
    void getIot() throws Exception {
        // Initialize the database
        iotRepository.saveAndFlush(iot);

        // Get the iot
        restIotMockMvc
            .perform(get(ENTITY_API_URL_ID, iot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(iot.getId().intValue()))
            .andExpect(jsonPath("$.mac").value(DEFAULT_MAC));
    }

    @Test
    @Transactional
    void getNonExistingIot() throws Exception {
        // Get the iot
        restIotMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewIot() throws Exception {
        // Initialize the database
        iotRepository.saveAndFlush(iot);

        int databaseSizeBeforeUpdate = iotRepository.findAll().size();

        // Update the iot
        Iot updatedIot = iotRepository.findById(iot.getId()).get();
        // Disconnect from session so that the updates on updatedIot are not directly saved in db
        em.detach(updatedIot);
        updatedIot.mac(UPDATED_MAC);
        IotDTO iotDTO = iotMapper.toDto(updatedIot);

        restIotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, iotDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(iotDTO))
            )
            .andExpect(status().isOk());

        // Validate the Iot in the database
        List<Iot> iotList = iotRepository.findAll();
        assertThat(iotList).hasSize(databaseSizeBeforeUpdate);
        Iot testIot = iotList.get(iotList.size() - 1);
        assertThat(testIot.getMac()).isEqualTo(UPDATED_MAC);
    }

    @Test
    @Transactional
    void putNonExistingIot() throws Exception {
        int databaseSizeBeforeUpdate = iotRepository.findAll().size();
        iot.setId(count.incrementAndGet());

        // Create the Iot
        IotDTO iotDTO = iotMapper.toDto(iot);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, iotDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(iotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Iot in the database
        List<Iot> iotList = iotRepository.findAll();
        assertThat(iotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIot() throws Exception {
        int databaseSizeBeforeUpdate = iotRepository.findAll().size();
        iot.setId(count.incrementAndGet());

        // Create the Iot
        IotDTO iotDTO = iotMapper.toDto(iot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(iotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Iot in the database
        List<Iot> iotList = iotRepository.findAll();
        assertThat(iotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIot() throws Exception {
        int databaseSizeBeforeUpdate = iotRepository.findAll().size();
        iot.setId(count.incrementAndGet());

        // Create the Iot
        IotDTO iotDTO = iotMapper.toDto(iot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIotMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(iotDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Iot in the database
        List<Iot> iotList = iotRepository.findAll();
        assertThat(iotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIotWithPatch() throws Exception {
        // Initialize the database
        iotRepository.saveAndFlush(iot);

        int databaseSizeBeforeUpdate = iotRepository.findAll().size();

        // Update the iot using partial update
        Iot partialUpdatedIot = new Iot();
        partialUpdatedIot.setId(iot.getId());

        restIotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIot))
            )
            .andExpect(status().isOk());

        // Validate the Iot in the database
        List<Iot> iotList = iotRepository.findAll();
        assertThat(iotList).hasSize(databaseSizeBeforeUpdate);
        Iot testIot = iotList.get(iotList.size() - 1);
        assertThat(testIot.getMac()).isEqualTo(DEFAULT_MAC);
    }

    @Test
    @Transactional
    void fullUpdateIotWithPatch() throws Exception {
        // Initialize the database
        iotRepository.saveAndFlush(iot);

        int databaseSizeBeforeUpdate = iotRepository.findAll().size();

        // Update the iot using partial update
        Iot partialUpdatedIot = new Iot();
        partialUpdatedIot.setId(iot.getId());

        partialUpdatedIot.mac(UPDATED_MAC);

        restIotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIot))
            )
            .andExpect(status().isOk());

        // Validate the Iot in the database
        List<Iot> iotList = iotRepository.findAll();
        assertThat(iotList).hasSize(databaseSizeBeforeUpdate);
        Iot testIot = iotList.get(iotList.size() - 1);
        assertThat(testIot.getMac()).isEqualTo(UPDATED_MAC);
    }

    @Test
    @Transactional
    void patchNonExistingIot() throws Exception {
        int databaseSizeBeforeUpdate = iotRepository.findAll().size();
        iot.setId(count.incrementAndGet());

        // Create the Iot
        IotDTO iotDTO = iotMapper.toDto(iot);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, iotDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(iotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Iot in the database
        List<Iot> iotList = iotRepository.findAll();
        assertThat(iotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIot() throws Exception {
        int databaseSizeBeforeUpdate = iotRepository.findAll().size();
        iot.setId(count.incrementAndGet());

        // Create the Iot
        IotDTO iotDTO = iotMapper.toDto(iot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(iotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Iot in the database
        List<Iot> iotList = iotRepository.findAll();
        assertThat(iotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIot() throws Exception {
        int databaseSizeBeforeUpdate = iotRepository.findAll().size();
        iot.setId(count.incrementAndGet());

        // Create the Iot
        IotDTO iotDTO = iotMapper.toDto(iot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIotMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(iotDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Iot in the database
        List<Iot> iotList = iotRepository.findAll();
        assertThat(iotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIot() throws Exception {
        // Initialize the database
        iotRepository.saveAndFlush(iot);

        int databaseSizeBeforeDelete = iotRepository.findAll().size();

        // Delete the iot
        restIotMockMvc.perform(delete(ENTITY_API_URL_ID, iot.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Iot> iotList = iotRepository.findAll();
        assertThat(iotList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
