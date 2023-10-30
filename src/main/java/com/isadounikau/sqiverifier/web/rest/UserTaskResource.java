package com.isadounikau.sqiverifier.web.rest;

import com.isadounikau.sqiverifier.config.springdoc.OpenApiAvailable;
import com.isadounikau.sqiverifier.domain.UserTask;
import com.isadounikau.sqiverifier.repository.UserTaskRepository;
import com.isadounikau.sqiverifier.service.UserTaskService;
import com.isadounikau.sqiverifier.service.dto.UserTaskResponseDto;
import com.isadounikau.sqiverifier.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.isadounikau.sqiverifier.domain.UserTask}.
 */
@RestController
@RequestMapping("/api")
public class UserTaskResource {

    private final Logger log = LoggerFactory.getLogger(UserTaskResource.class);

    private static final String ENTITY_NAME = "userTask";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserTaskService userTaskService;

    private final UserTaskRepository userTaskRepository;

    public UserTaskResource(UserTaskService userTaskService, UserTaskRepository userTaskRepository) {
        this.userTaskService = userTaskService;
        this.userTaskRepository = userTaskRepository;
    }

    /**
     * {@code POST  /user-tasks} : Create a new userTask.
     *
     * @param userTask the userTask to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userTask, or with
     * status {@code 400 (Bad Request)} if the userTask has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @OpenApiAvailable
    @PostMapping("/user-tasks")
    public ResponseEntity<UserTask> createUserTask(@RequestBody UserTask userTask) throws URISyntaxException {
        log.debug("REST request to save UserTask : {}", userTask);
        if (userTask.getId() != null) {
            throw new BadRequestAlertException("A new userTask cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserTask result = userTaskService.save(userTask).get();
        return ResponseEntity
            .created(new URI("/api/user-tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
                result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-tasks/:id} : Updates an existing userTask.
     *
     * @param id       the id of the userTask to save.
     * @param userTask the userTask to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userTask,
     * or with status {@code 400 (Bad Request)} if the userTask is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userTask couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @OpenApiAvailable
    @PutMapping("/user-tasks/{id}")
    public ResponseEntity<UserTask> updateUserTask(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserTask userTask
    ) throws URISyntaxException {
        log.debug("REST request to update UserTask : {}, {}", id, userTask);
        if (userTask.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userTask.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userTaskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserTask result = userTaskService.update(userTask).get();
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
                    userTask.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-tasks/:id} : Partial updates given fields of an existing userTask, field will ignore if it
     * is null
     *
     * @param id       the id of the userTask to save.
     * @param userTask the userTask to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userTask,
     * or with status {@code 400 (Bad Request)} if the userTask is not valid,
     * or with status {@code 404 (Not Found)} if the userTask is not found,
     * or with status {@code 500 (Internal Server Error)} if the userTask couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @OpenApiAvailable
    @PatchMapping(value = "/user-tasks/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<UserTask> partialUpdateUserTask(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserTask userTask
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserTask partially : {}, {}", id, userTask);
        if (userTask.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userTask.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userTaskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserTask> result = userTaskService.partialUpdate(userTask);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userTask.getId().toString())
        );
    }

    /**
     * {@code GET  /user-tasks} : get all the userTasks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userTasks in body.
     */
    @OpenApiAvailable
    @GetMapping("/user-tasks")
    public ResponseEntity<List<UserTaskResponseDto>> getAllUserTasks(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of UserTasks");
        Page<UserTaskResponseDto> page = userTaskService.findAll(pageable);
        HttpHeaders headers =
            PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-tasks/:id} : get the "id" userTask.
     *
     * @param id the id of the userTask to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userTask, or with status
     * {@code 404 (Not Found)}.
     */
    @OpenApiAvailable
    @GetMapping("/user-tasks/{id}")
    public ResponseEntity<UserTask> getUserTask(@PathVariable Long id) {
        log.debug("REST request to get UserTask : {}", id);
        Optional<UserTask> userTask = userTaskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userTask);
    }

    /**
     * {@code DELETE  /user-tasks/:id} : delete the "id" userTask.
     *
     * @param id the id of the userTask to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @OpenApiAvailable
    @DeleteMapping("/user-tasks/{id}")
    public ResponseEntity<Void> deleteUserTask(@PathVariable Long id) {
        log.debug("REST request to delete UserTask : {}", id);
        userTaskService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
