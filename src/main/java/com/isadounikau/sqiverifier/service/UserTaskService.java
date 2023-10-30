package com.isadounikau.sqiverifier.service;

import com.isadounikau.sqiverifier.domain.UserTask;
import com.isadounikau.sqiverifier.service.dto.UserTaskResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link UserTask}.
 */
public interface UserTaskService {
    /**
     * Save a userTask.
     *
     * @param userTask the entity to save.
     * @return the persisted entity.
     */
    Optional<UserTask> save(UserTask userTask);

    /**
     * Updates a userTask.
     *
     * @param userTask the entity to update.
     * @return the persisted entity.
     */
    Optional<UserTask> update(UserTask userTask);

    /**
     * Partially updates a userTask.
     *
     * @param userTask the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserTask> partialUpdate(UserTask userTask);

    /**
     * Get all the userTasks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserTaskResponseDto> findAll(Pageable pageable);

    /**
     * Get the "id" userTask.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserTask> findOne(Long id);

    /**
     * Delete the "id" userTask.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
