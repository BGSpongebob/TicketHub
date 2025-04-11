package org.acme.Tools;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.Model.RoleEnum;

import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class TokenGenerator {
    public String generateToken(String username, RoleEnum role) {
        Set<String> roles = new HashSet<>();
        roles.add(role.name()); // Add the user's role to the JWT

        long currentTime = System.currentTimeMillis() / 1000; // Convert to seconds for JWT standard

        return Jwt.issuer("myapp")
                .upn(username)
                .groups(roles)
                .issuedAt(currentTime) // Add issuance time
                .expiresAt(currentTime + 3600) // 1 hour from issuance
                .sign();
    }
}