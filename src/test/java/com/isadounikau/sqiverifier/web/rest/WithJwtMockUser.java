package com.isadounikau.sqiverifier.web.rest;

import com.isadounikau.sqiverifier.security.AuthoritiesConstants;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;
import java.util.stream.Collectors;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithJwtMockUser.Factory.class)
public @interface WithJwtMockUser {

    String value() default "";

    String roles() default AuthoritiesConstants.USER;

    class Factory implements WithSecurityContextFactory<WithJwtMockUser> {

        private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();

        @Override
        public SecurityContext createSecurityContext(WithJwtMockUser withUser) {
            Jwt.Builder jwtBuilder = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim(JwtClaimNames.SUB, withUser.value())
                .claim("scope", "read")
                .claim("auth", withUser.roles());

            var authorities = Set.of(withUser.roles()).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());

            var token = new JwtAuthenticationToken(jwtBuilder.build(), authorities);

            var context = new SecurityContextImpl();
            context.setAuthentication(token);

            return context;
        }
    }
}
