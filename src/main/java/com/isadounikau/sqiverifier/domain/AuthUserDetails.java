package com.isadounikau.sqiverifier.domain;

import com.isadounikau.sqiverifier.domain.enums.Role;

import java.util.Set;

public record AuthUserDetails (
    String login,
    Set<Role> roles
) {}
