package com.isadounikau.sqiverifier.service.impl;

import com.isadounikau.sqiverifier.domain.AuthUserDetails;
import com.isadounikau.sqiverifier.domain.enums.Role;
import com.isadounikau.sqiverifier.service.UserAuthService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserAuthServiceImpl implements UserAuthService {

    @Override
    public Optional<AuthUserDetails> getCurrentUser() {
        var jwt = this.getCurrentUserJwt();

        var auth = jwt.map(it -> it.getClaimAsString("auth"))
            .orElseThrow(() -> new BadCredentialsException("Authentication not found"));

        var roles = Arrays.stream(auth.split(" "))
            .map(Role::valueOf)
            .collect(Collectors.toSet());

        var login = jwt.map(it -> it.getClaimAsString("sub")).orElseThrow(
            () -> new BadCredentialsException("User not authorised")
        );
        return this.getCurrentUserJwt().map(it -> new AuthUserDetails(login, roles));
    }

    private Optional<Jwt> getCurrentUserJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof Jwt details) {
                return Optional.of(details);
            }
        }
        return Optional.empty(); // No authenticated user
    }
}
