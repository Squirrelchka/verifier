package com.isadounikau.sqiverifier.service;

import com.isadounikau.sqiverifier.domain.AuthUserDetails;

import java.util.Optional;

public interface UserAuthService {

    Optional<AuthUserDetails> getCurrentUser();

}
