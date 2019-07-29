package it.tai.dev.apimanager.config;

import org.keycloak.OAuth2Constants;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Apimanager.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    private KeycloakConfiguration keycloakConfiguration = new KeycloakConfiguration();

    public KeycloakConfiguration getKeycloakConfiguration() {
        return keycloakConfiguration;
    }

    public static class KeycloakConfiguration {
        private static final String grantType = OAuth2Constants.PASSWORD;

        private String serverUrl;

        private String masterRealm;

        private String adminId;

        private String adminSecret;

        private String adminUsername;

        private String adminPassword;

        private String currentRealm;

        public String getServerUrl() {
            return serverUrl;
        }

        public String getMasterRealm() {
            return masterRealm;
        }

        public String getAdminId() {
            return adminId;
        }

        public String getAdminSecret() {
            return adminSecret;
        }

        public String getAdminUsername() {
            return adminUsername;
        }

        public String getAdminPassword() {
            return adminPassword;
        }

        public void setServerUrl(String serverUrl) {
            this.serverUrl = serverUrl;
        }

        public void setMasterRealm(String masterRealm) {
            this.masterRealm = masterRealm;
        }

        public void setAdminId(String adminId) {
            this.adminId = adminId;
        }

        public void setAdminSecret(String adminSecret) {
            this.adminSecret = adminSecret;
        }

        public void setAdminUsername(String adminUsername) {
            this.adminUsername = adminUsername;
        }

        public void setAdminPassword(String adminPassword) {
            this.adminPassword = adminPassword;
        }

        public String getGrantType() {
            return grantType;
        }

        public String getCurrentRealm() {
            return currentRealm;
        }

        public void setCurrentRealm(String currentRealm) {
            this.currentRealm = currentRealm;
        }
    }
}
