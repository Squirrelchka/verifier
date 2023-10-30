package com.isadounikau.sqiverifier.service.mapper;

import com.isadounikau.sqiverifier.domain.Task;
import com.isadounikau.sqiverifier.service.dto.TaskDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper extends EntityMapper<TaskDTO, Task> {}
