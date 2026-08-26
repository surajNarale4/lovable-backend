package io.suraj.projects.lovable.service.impl;

import io.suraj.projects.lovable.dto.auth.AuthResponse;
import io.suraj.projects.lovable.dto.auth.LoginRequest;
import io.suraj.projects.lovable.dto.auth.SignupRequest;
import io.suraj.projects.lovable.dto.auth.UserProfileRespose;
import io.suraj.projects.lovable.service.AuthService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final Keycloak keycloak;
    @Value("${keycloak.realm}")
    private String realm;

    @Override
    public String signup(SignupRequest request) {
        log.info("request name"+request.name());
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEmail(request.email());
        userRepresentation.setUsername(request.name());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);
        userRepresentation.setFirstName(request.name());
        userRepresentation.setLastName("ll");

        CredentialRepresentation credentialRepresentation =new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setCreatedDate(System.currentTimeMillis());
        userRepresentation.setCredentials(List.of(credentialRepresentation));
        credentialRepresentation.setValue(request.password());
        credentialRepresentation.setTemporary(false);


        UsersResource usersResource = keycloak.realm(realm).users();
        try (Response response = usersResource.create(userRepresentation)) {
            if (response.getStatus() == 201) {
                // extract the new user's ID from the Location header
                String location = response.getLocation().getPath();
                //will use this id in our local postgres to store user again
                return location.substring(location.lastIndexOf('/') + 1);
            } else {
                throw new RuntimeException(
                        "Failed to create user: " + response.getStatus() + " " + response.readEntity(String.class));
            }
        }


    }

    @Override
    public AuthResponse login(LoginRequest request) {
        return null;
    }

    @Override
    public UserProfileRespose getProfile(Long userId) {
        return null;
    }
}
