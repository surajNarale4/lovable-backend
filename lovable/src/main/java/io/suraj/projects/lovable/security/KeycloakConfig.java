package io.suraj.projects.lovable.config;


import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.auth-server-url}")
    private String authServer;

    @Value("${keycloak.client-id}")
    private String clienId;

    @Value("${keycloak.admin-cli-secret}")
    private String clientSecrete;


    @Bean
    public Keycloak createKeycloak(){
            return KeycloakBuilder.builder()
                    .realm(realm)
                    .clientId(clienId)
                    .clientSecret(clientSecrete)
                    .serverUrl(authServer)
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .build();
    }

}
