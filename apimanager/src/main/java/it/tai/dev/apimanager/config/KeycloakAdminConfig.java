package it.tai.dev.apimanager.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class KeycloakAdminConfig {
    private ApplicationProperties applicationProperties;

    public KeycloakAdminConfig(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;

    }

    @Bean
    @Lazy
    public Keycloak getKeycloak() {
        ApplicationProperties.KeycloakConfiguration keycloakConfiguration = applicationProperties.getKeycloakConfiguration();
        String serverUrl = keycloakConfiguration.getServerUrl();
        String realm = keycloakConfiguration.getMasterRealm();
        String clientId = keycloakConfiguration.getAdminId();
        String clientSecret = keycloakConfiguration.getAdminSecret();
        String username = keycloakConfiguration.getAdminUsername();
        String password = keycloakConfiguration.getAdminPassword();
        String grantType = keycloakConfiguration.getGrantType();
        return KeycloakBuilder.builder()
            .serverUrl(serverUrl)
            .realm(realm)
            .grantType(grantType)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .username(username)
            .password(password)
            .build();
    }

    @Bean
    public RealmResource getRealmResource(Keycloak keycloak) {
        return keycloak.realm(applicationProperties.getKeycloakConfiguration().getCurrentRealm());
    }
}
