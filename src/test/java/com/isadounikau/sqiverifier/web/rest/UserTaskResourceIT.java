package com.isadounikau.sqiverifier.web.rest;

import com.isadounikau.sqiverifier.IntegrationTest;
import com.isadounikau.sqiverifier.domain.Task;
import com.isadounikau.sqiverifier.domain.User;
import com.isadounikau.sqiverifier.domain.UserTask;
import com.isadounikau.sqiverifier.repository.UserTaskRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the {@link UserTaskResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithJwtMockUser(value = UserTaskResourceIT.TEST_LOGIN)
class UserTaskResourceIT {

    public static final String TEST_LOGIN = "test_user";

    private static final String TEST_PASSWORD = "$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K";

    private static final Boolean DEFAULT_IS_SOLVED = false;

    private static final Boolean UPDATED_IS_SOLVED = true;

    private static final String ENTITY_API_URL = "/api/user-tasks";

    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();

    private static final AtomicLong count = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private UserTaskRepository userTaskRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserTaskMockMvc;

    private UserTask userTask;

    /**
     * Create an entity for this test.
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserTask createEntity(EntityManager em) {
        var task = new Task().title("TEST").text("TEST").answer("TEST");
        em.persist(task);
        var user = new User();
        user.setLogin(TEST_LOGIN);
        user.setPassword(TEST_PASSWORD);
        em.persist(user);
        return new UserTask().isSolved(DEFAULT_IS_SOLVED).task(task).user(user);
    }

    /**
     * Create an updated entity for this test.
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserTask createUpdatedEntity(EntityManager em) {
        return new UserTask().isSolved(UPDATED_IS_SOLVED);
    }

    @BeforeEach
    public void initTest() {
        userTask = createEntity(em);
    }

    @Test
    @Transactional
    void createUserTask() throws Exception {
        int databaseSizeBeforeCreate = userTaskRepository.findAll().size();
        // Create the UserTask
        restUserTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userTask)))
            .andExpect(status().isCreated());

        // Validate the UserTask in the database
        List<UserTask> userTaskList = userTaskRepository.findAll();
        assertThat(userTaskList).hasSize(databaseSizeBeforeCreate + 1);
        UserTask testUserTask = userTaskList.get(userTaskList.size() - 1);
        assertThat(testUserTask.getIsSolved()).isEqualTo(DEFAULT_IS_SOLVED);
    }

    @Test
    @Transactional
    void createUserTaskWithExistingId() throws Exception {
        // Create the UserTask with an existing ID
        userTask.setId(1L);

        int databaseSizeBeforeCreate = userTaskRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userTask)))
            .andExpect(status().isBadRequest());

        // Validate the UserTask in the database
        List<UserTask> userTaskList = userTaskRepository.findAll();
        assertThat(userTaskList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserTasks() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList
        restUserTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].isSolved").value(hasItem(DEFAULT_IS_SOLVED)));
    }

    @Test
    @Transactional
    void getUserTask() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get the userTask
        restUserTaskMockMvc
            .perform(get(ENTITY_API_URL_ID, userTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userTask.getId().intValue()))
            .andExpect(jsonPath("$.isSolved").value(DEFAULT_IS_SOLVED));
    }

    @Test
    @Transactional
    void getNonExistingUserTask() throws Exception {
        // Get the userTask
        restUserTaskMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserTask() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        int databaseSizeBeforeUpdate = userTaskRepository.findAll().size();

        // Update the userTask
        UserTask updatedUserTask = userTaskRepository.findById(userTask.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserTask are not directly saved in db
        em.detach(updatedUserTask);
        updatedUserTask.isSolved(UPDATED_IS_SOLVED);

        restUserTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserTask.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserTask))
            )
            .andExpect(status().isOk());

        // Validate the UserTask in the database
        List<UserTask> userTaskList = userTaskRepository.findAll();
        assertThat(userTaskList).hasSize(databaseSizeBeforeUpdate);
        UserTask testUserTask = userTaskList.get(userTaskList.size() - 1);
        assertThat(testUserTask.getIsSolved()).isEqualTo(UPDATED_IS_SOLVED);
    }

    @Test
    @Transactional
    void putNonExistingUserTask() throws Exception {
        int databaseSizeBeforeUpdate = userTaskRepository.findAll().size();
        userTask.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userTask.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userTask))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserTask in the database
        List<UserTask> userTaskList = userTaskRepository.findAll();
        assertThat(userTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserTask() throws Exception {
        int databaseSizeBeforeUpdate = userTaskRepository.findAll().size();
        userTask.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userTask))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserTask in the database
        List<UserTask> userTaskList = userTaskRepository.findAll();
        assertThat(userTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserTask() throws Exception {
        int databaseSizeBeforeUpdate = userTaskRepository.findAll().size();
        userTask.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserTaskMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userTask)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserTask in the database
        List<UserTask> userTaskList = userTaskRepository.findAll();
        assertThat(userTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserTaskWithPatch() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        int databaseSizeBeforeUpdate = userTaskRepository.findAll().size();

        // Update the userTask using partial update
        UserTask partialUpdatedUserTask = new UserTask();
        partialUpdatedUserTask.setId(userTask.getId());

        partialUpdatedUserTask.isSolved(UPDATED_IS_SOLVED);

        restUserTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserTask))
            )
            .andExpect(status().isOk());

        // Validate the UserTask in the database
        List<UserTask> userTaskList = userTaskRepository.findAll();
        assertThat(userTaskList).hasSize(databaseSizeBeforeUpdate);
        UserTask testUserTask = userTaskList.get(userTaskList.size() - 1);
        assertThat(testUserTask.getIsSolved()).isEqualTo(UPDATED_IS_SOLVED);
    }

    @Test
    @Transactional
    void fullUpdateUserTaskWithPatch() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        int databaseSizeBeforeUpdate = userTaskRepository.findAll().size();

        // Update the userTask using partial update
        UserTask partialUpdatedUserTask = new UserTask();
        partialUpdatedUserTask.setId(userTask.getId());

        partialUpdatedUserTask.isSolved(UPDATED_IS_SOLVED);

        restUserTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserTask))
            )
            .andExpect(status().isOk());

        // Validate the UserTask in the database
        List<UserTask> userTaskList = userTaskRepository.findAll();
        assertThat(userTaskList).hasSize(databaseSizeBeforeUpdate);
        UserTask testUserTask = userTaskList.get(userTaskList.size() - 1);
        assertThat(testUserTask.getIsSolved()).isEqualTo(UPDATED_IS_SOLVED);
    }

    @Test
    @Transactional
    void patchNonExistingUserTask() throws Exception {
        int databaseSizeBeforeUpdate = userTaskRepository.findAll().size();
        userTask.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userTask))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserTask in the database
        List<UserTask> userTaskList = userTaskRepository.findAll();
        assertThat(userTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserTask() throws Exception {
        int databaseSizeBeforeUpdate = userTaskRepository.findAll().size();
        userTask.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userTask))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserTask in the database
        List<UserTask> userTaskList = userTaskRepository.findAll();
        assertThat(userTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserTask() throws Exception {
        int databaseSizeBeforeUpdate = userTaskRepository.findAll().size();
        userTask.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserTaskMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userTask)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserTask in the database
        List<UserTask> userTaskList = userTaskRepository.findAll();
        assertThat(userTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserTask() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        int databaseSizeBeforeDelete = userTaskRepository.findAll().size();

        // Delete the userTask
        restUserTaskMockMvc
            .perform(delete(ENTITY_API_URL_ID, userTask.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserTask> userTaskList = userTaskRepository.findAll();
        assertThat(userTaskList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
