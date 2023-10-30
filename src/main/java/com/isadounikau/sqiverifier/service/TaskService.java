package com.isadounikau.sqiverifier.service;

import com.isadounikau.sqiverifier.service.dto.TaskDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link TaskDTO}.
 */
public interface TaskService {
    /**
     * Save a TaskDTO.
     *
     * @param TaskDTO the entity to save.
     * @return the persisted entity.
     */
    TaskDTO save(TaskDTO TaskDTO);

    /**
     * Updates a TaskDTO.
     *
     * @param TaskDTO the entity to update.
     * @return the persisted entity.
     */
    TaskDTO update(TaskDTO TaskDTO);

    /**
     * Partially updates a TaskDTO.
     *
     * @param TaskDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TaskDTO> partialUpdate(TaskDTO TaskDTO);

    /**
     * Get all the TaskDTOs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TaskDTO> findAll(Pageable pageable);

    /**
     * Get the "id" TaskDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TaskDTO> findOne(Long id);

    /**
     * Delete the "id" TaskDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
