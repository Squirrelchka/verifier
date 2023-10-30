package com.isadounikau.sqiverifier.domain.enums;

import java.util.Set;

public enum Role {
    ROLE_ADMIN, ROLE_USER_STUDENT, ROLE_USER_TEACHER, ROLE_ANONYMOUS;

    public static final Set<Role> USER_ROLES = Set.of(Role.ROLE_USER_TEACHER, Role.ROLE_USER_STUDENT);
}
