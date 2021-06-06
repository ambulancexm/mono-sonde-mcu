package fr.thomas.iot.service;

import fr.thomas.iot.domain.Iot;
import fr.thomas.iot.repository.IotRepository;
import fr.thomas.iot.service.dto.IotDTO;
import fr.thomas.iot.service.mapper.IotMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Iot}.
 */
@Service
@Transactional
public class IotService {

    private final Logger log = LoggerFactory.getLogger(IotService.class);

    private final IotRepository iotRepository;

    private final IotMapper iotMapper;

    public IotService(IotRepository iotRepository, IotMapper iotMapper) {
        this.iotRepository = iotRepository;
        this.iotMapper = iotMapper;
    }

    /**
     * Save a iot.
     *
     * @param iotDTO the entity to save.
     * @return the persisted entity.
     */
    public IotDTO save(IotDTO iotDTO) {
        log.debug("Request to save Iot : {}", iotDTO);
        Iot iot = iotMapper.toEntity(iotDTO);
        iot = iotRepository.save(iot);
        return iotMapper.toDto(iot);
    }

    /**
     * Partially update a iot.
     *
     * @param iotDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IotDTO> partialUpdate(IotDTO iotDTO) {
        log.debug("Request to partially update Iot : {}", iotDTO);

        return iotRepository
            .findById(iotDTO.getId())
            .map(
                existingIot -> {
                    iotMapper.partialUpdate(existingIot, iotDTO);
                    return existingIot;
                }
            )
            .map(iotRepository::save)
            .map(iotMapper::toDto);
    }

    /**
     * Get all the iots.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<IotDTO> findAll() {
        log.debug("Request to get all Iots");
        return iotRepository.findAll().stream().map(iotMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one iot by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IotDTO> findOne(Long id) {
        log.debug("Request to get Iot : {}", id);
        return iotRepository.findById(id).map(iotMapper::toDto);
    }

    /**
     * Delete the iot by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Iot : {}", id);
        iotRepository.deleteById(id);
    }
}
