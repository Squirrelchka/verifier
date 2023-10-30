package com.isadounikau.sqiverifier.service.dto;

public record UserTaskResponseDto(long id, boolean isSolved, String taskTitle, String username) {}
