package fr.thomas.iot.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.thomas.iot.IntegrationTest;
import fr.thomas.iot.domain.Sensor;
import fr.thomas.iot.domain.enumeration.TypeSensor;
import fr.thomas.iot.repository.SensorRepository;
import fr.thomas.iot.service.dto.SensorDTO;
import fr.thomas.iot.service.mapper.SensorMapper;
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
 * Integration tests for the {@link SensorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SensorResourceIT {

    private static final TypeSensor DEFAULT_TYPE_SENSOR = TypeSensor.TEMP;
    private static final TypeSensor UPDATED_TYPE_SENSOR = TypeSensor.ALTITUDE;

    private static final Double DEFAULT_VALUE = 1D;
    private static final Double UPDATED_VALUE = 2D;

    private static final String ENTITY_API_URL = "/api/sensors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private SensorMapper sensorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSensorMockMvc;

    private Sensor sensor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sensor createEntity(EntityManager em) {
        Sensor sensor = new Sensor().typeSensor(DEFAULT_TYPE_SENSOR).value(DEFAULT_VALUE);
        return sensor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sensor createUpdatedEntity(EntityManager em) {
        Sensor sensor = new Sensor().typeSensor(UPDATED_TYPE_SENSOR).value(UPDATED_VALUE);
        return sensor;
    }

    @BeforeEach
    public void initTest() {
        sensor = createEntity(em);
    }

    @Test
    @Transactional
    void createSensor() throws Exception {
        int databaseSizeBeforeCreate = sensorRepository.findAll().size();
        // Create the Sensor
        SensorDTO sensorDTO = sensorMapper.toDto(sensor);
        restSensorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sensorDTO)))
            .andExpect(status().isCreated());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeCreate + 1);
        Sensor testSensor = sensorList.get(sensorList.size() - 1);
        assertThat(testSensor.getTypeSensor()).isEqualTo(DEFAULT_TYPE_SENSOR);
        assertThat(testSensor.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void createSensorWithExistingId() throws Exception {
        // Create the Sensor with an existing ID
        sensor.setId(1L);
        SensorDTO sensorDTO = sensorMapper.toDto(sensor);

        int databaseSizeBeforeCreate = sensorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSensorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sensorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSensors() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList
        restSensorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sensor.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeSensor").value(hasItem(DEFAULT_TYPE_SENSOR.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())));
    }

    @Test
    @Transactional
    void getSensor() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get the sensor
        restSensorMockMvc
            .perform(get(ENTITY_API_URL_ID, sensor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sensor.getId().intValue()))
            .andExpect(jsonPath("$.typeSensor").value(DEFAULT_TYPE_SENSOR.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingSensor() throws Exception {
        // Get the sensor
        restSensorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSensor() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        int databaseSizeBeforeUpdate = sensorRepository.findAll().size();

        // Update the sensor
        Sensor updatedSensor = sensorRepository.findById(sensor.getId()).get();
        // Disconnect from session so that the updates on updatedSensor are not directly saved in db
        em.detach(updatedSensor);
        updatedSensor.typeSensor(UPDATED_TYPE_SENSOR).value(UPDATED_VALUE);
        SensorDTO sensorDTO = sensorMapper.toDto(updatedSensor);

        restSensorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sensorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sensorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
        Sensor testSensor = sensorList.get(sensorList.size() - 1);
        assertThat(testSensor.getTypeSensor()).isEqualTo(UPDATED_TYPE_SENSOR);
        assertThat(testSensor.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingSensor() throws Exception {
        int databaseSizeBeforeUpdate = sensorRepository.findAll().size();
        sensor.setId(count.incrementAndGet());

        // Create the Sensor
        SensorDTO sensorDTO = sensorMapper.toDto(sensor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSensorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sensorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sensorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSensor() throws Exception {
        int databaseSizeBeforeUpdate = sensorRepository.findAll().size();
        sensor.setId(count.incrementAndGet());

        // Create the Sensor
        SensorDTO sensorDTO = sensorMapper.toDto(sensor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSensorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sensorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSensor() throws Exception {
        int databaseSizeBeforeUpdate = sensorRepository.findAll().size();
        sensor.setId(count.incrementAndGet());

        // Create the Sensor
        SensorDTO sensorDTO = sensorMapper.toDto(sensor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSensorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sensorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSensorWithPatch() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        int databaseSizeBeforeUpdate = sensorRepository.findAll().size();

        // Update the sensor using partial update
        Sensor partialUpdatedSensor = new Sensor();
        partialUpdatedSensor.setId(sensor.getId());

        partialUpdatedSensor.typeSensor(UPDATED_TYPE_SENSOR).value(UPDATED_VALUE);

        restSensorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSensor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSensor))
            )
            .andExpect(status().isOk());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
        Sensor testSensor = sensorList.get(sensorList.size() - 1);
        assertThat(testSensor.getTypeSensor()).isEqualTo(UPDATED_TYPE_SENSOR);
        assertThat(testSensor.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateSensorWithPatch() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        int databaseSizeBeforeUpdate = sensorRepository.findAll().size();

        // Update the sensor using partial update
        Sensor partialUpdatedSensor = new Sensor();
        partialUpdatedSensor.setId(sensor.getId());

        partialUpdatedSensor.typeSensor(UPDATED_TYPE_SENSOR).value(UPDATED_VALUE);

        restSensorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSensor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSensor))
            )
            .andExpect(status().isOk());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
        Sensor testSensor = sensorList.get(sensorList.size() - 1);
        assertThat(testSensor.getTypeSensor()).isEqualTo(UPDATED_TYPE_SENSOR);
        assertThat(testSensor.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingSensor() throws Exception {
        int databaseSizeBeforeUpdate = sensorRepository.findAll().size();
        sensor.setId(count.incrementAndGet());

        // Create the Sensor
        SensorDTO sensorDTO = sensorMapper.toDto(sensor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSensorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sensorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sensorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSensor() throws Exception {
        int databaseSizeBeforeUpdate = sensorRepository.findAll().size();
        sensor.setId(count.incrementAndGet());

        // Create the Sensor
        SensorDTO sensorDTO = sensorMapper.toDto(sensor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSensorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sensorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSensor() throws Exception {
        int databaseSizeBeforeUpdate = sensorRepository.findAll().size();
        sensor.setId(count.incrementAndGet());

        // Create the Sensor
        SensorDTO sensorDTO = sensorMapper.toDto(sensor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSensorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sensorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSensor() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        int databaseSizeBeforeDelete = sensorRepository.findAll().size();

        // Delete the sensor
        restSensorMockMvc
            .perform(delete(ENTITY_API_URL_ID, sensor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
