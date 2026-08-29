package io.suraj.projects.lovable.service.impl;

import io.suraj.projects.lovable.dto.auth.AuthResponse;
import io.suraj.projects.lovable.dto.auth.LoginRequest;
import io.suraj.projects.lovable.dto.auth.SignupRequest;
import io.suraj.projects.lovable.dto.auth.UserProfileRespose;
import io.suraj.projects.lovable.entity.User;
import io.suraj.projects.lovable.error.ResourseNotFoundException;
import io.suraj.projects.lovable.mapper.UserMapper;
import io.suraj.projects.lovable.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Value("${keycloak.realm}")
    private String realm;

    @Override
    public String signup(SignupRequest request) {
        log.info("request name" + request.name());
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEmail(request.email());
        userRepresentation.setUsername(request.name());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);
        userRepresentation.setFirstName(request.name());
        userRepresentation.setLastName("ll");
        userRepresentation.setRealmRoles(List.of("USER"));

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setCreatedDate(System.currentTimeMillis());
        userRepresentation.setCredentials(List.of(credentialRepresentation));
        credentialRepresentation.setValue(request.password());
        credentialRepresentation.setTemporary(false);

        String keycloakUserId;
        UsersResource usersResource = keycloak.realm(realm).users();
        try (Response response = usersResource.create(userRepresentation)) {
            if (response.getStatus() == 201) {
                // extract the new user's ID from the Location header
                String location = response.getLocation().getPath();
                //will use this id in our local postgres to store user again
                keycloakUserId = location.substring(location.lastIndexOf('/') + 1);
            } else {
                throw new RuntimeException(
                        "Failed to create user: " + response.getStatus() + " " + response.readEntity(String.class));
            }

            try {
                User localUser = new User();
                localUser.setId(keycloakUserId);    // <-- shared ID, this is the link
                localUser.setName(request.name());
                localUser.setEmail(request.email());

                userRepository.save(localUser);
            } catch (Exception e) {
                // rollback: delete the Keycloak user if local save fails, to avoid orphaned accounts
                usersResource.get(keycloakUserId).remove();
                throw new RuntimeException("Signup failed while saving local user, rolled back Keycloak user", e);
            }
        }
        return keycloakUserId;
    }




    @Override
    public AuthResponse login(LoginRequest request) {
        return null;
    }

    /*
    we gethering details from our main db
     */
    @Override
    public UserProfileRespose getProfile(String userId) {
        return userMapper.userToUserProfile(userRepository.findById(userId).orElseThrow(()->new ResourseNotFoundException("no user found")));
    }
}
