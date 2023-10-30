package com.isadounikau.sqiverifier.service.impl;

import com.isadounikau.sqiverifier.domain.UserTask;
import com.isadounikau.sqiverifier.domain.enums.Role;
import com.isadounikau.sqiverifier.repository.UserTaskRepository;
import com.isadounikau.sqiverifier.service.UserAuthService;
import com.isadounikau.sqiverifier.service.UserTaskService;
import com.isadounikau.sqiverifier.service.dto.UserTaskResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Supplier;

import static com.isadounikau.sqiverifier.domain.enums.Role.USER_ROLES;

/**
 * Service Implementation for managing {@link UserTask}.
 */
@Service
@Transactional
public class UserTaskServiceImpl implements UserTaskService {

    private final Logger log = LoggerFactory.getLogger(UserTaskServiceImpl.class);

    private final UserTaskRepository userTaskRepository;

    private final UserAuthService userAuthService;

    public UserTaskServiceImpl(
        UserTaskRepository userTaskRepository,
        UserAuthService userAuthService) {
        this.userTaskRepository = userTaskRepository;
        this.userAuthService = userAuthService;
    }

    @Override
    public Optional<UserTask> save(UserTask userTask) {
        log.debug("Request to save UserTask : {}", userTask);
        return Optional.of(userTaskRepository.save(userTask));
    }

    @Override
    public Optional<UserTask> update(UserTask userTask) {
        log.debug("Request to update UserTask : {}", userTask);
        return runVerified(userTask.getId(), () -> Optional.of(userTaskRepository.save(userTask)));
    }

    @Override
    public Optional<UserTask> partialUpdate(UserTask userTask) {
        log.debug("Request to partially update UserTask : {}", userTask);
        return runVerified(userTask.getId(), () -> userTaskRepository
            .findById(userTask.getId())
            .map(existingUserTask -> {
                if (userTask.getIsSolved() != null) {
                    existingUserTask.setIsSolved(userTask.getIsSolved());
                }

                return existingUserTask;
            })
            .map(userTaskRepository::save));


    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserTaskResponseDto> findAll(Pageable pageable) {
        log.debug("Request to get all UserTasks");
        var currentUser = userAuthService.getCurrentUser().orElseThrow(
            () -> new BadCredentialsException("User not authorised")
        );
        var roles = currentUser.roles();

        Page<UserTask> page;
        if (roles.contains(Role.ROLE_ADMIN)) {
            page = userTaskRepository.findAll(pageable);
        } else if (USER_ROLES.stream().anyMatch(roles::contains)) {
            page = userTaskRepository.findByUserIsCurrentUser(pageable, currentUser.login());
        } else {
            throw new IllegalArgumentException("Unsupported roles " + currentUser);
        }

        var response = page.getContent().stream().map(it -> new UserTaskResponseDto(
            it.getId(), it.getIsSolved(), it.getTask().getTitle(), it.getUser().getLogin()
        )).toList();

        return new PageImpl<>(response, pageable, page.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserTask> findOne(Long id) {
        log.debug("Request to get UserTask : {}", id);
        return runVerified(id, () -> userTaskRepository.findById(id));
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserTask : {}", id);
        runVerified(id, () -> {
            userTaskRepository.deleteById(id);
            return Optional.empty();
        });
    }

    private Optional<UserTask> runVerified(Long userTaskId, Supplier<Optional<UserTask>> supplier) {
        var currentUser = userAuthService.getCurrentUser().orElseThrow(
            () -> new BadCredentialsException("User not authorised")
        );

        var roles = currentUser.roles();
        if (roles.contains(Role.ROLE_ADMIN)) {
            return supplier.get();
        } else if (USER_ROLES.stream().anyMatch(roles::contains)) {
            var task = userTaskRepository.findById(userTaskId);
            if (task.isPresent() && task.get().getUser().getLogin().equals(currentUser.login())) {
                return supplier.get();
            } else {
                return Optional.empty();
            }
        }

        throw new IllegalArgumentException("Unsupported roles " + currentUser);
    }
}
