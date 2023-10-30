package com.isadounikau.sqiverifier.repository;

import com.isadounikau.sqiverifier.domain.UserTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the UserTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserTaskRepository extends JpaRepository<UserTask, Long> {
    @Query("select userTask from UserTask userTask where userTask.user.login = :login")
    Page<UserTask> findByUserIsCurrentUser(Pageable pageable, String login);

    @Query("select userTask from UserTask userTask where userTask.id = :id and userTask.user.login = :login")
    Optional<UserTask> findByIdAndLogin(Long id, String login);
}
