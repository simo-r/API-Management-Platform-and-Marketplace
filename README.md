# Tirocinio-Microservizi

# Prerequisiti
  - Keycloak configurato secondo le proprietà del progetto, si guardi application.yml (src/main/resources/config)

# How to easy run
  - Clona questo repository
  - Considera la cartella gateway e apimanager come due singoli progetti (aprili con il tuo IDE preferito)
    - Esegui npm install in entrambi i progetti
  - Scarica Jhipster registry https://github.com/jhipster/jhipster-registry/releases  (Current: v 5.0.2)
  - Run JHipster registry da riga di comando
    - ./jhipster-registry-5.0.2.jar --spring.security.user.password=admin --jhipster.security.authentication.jwt.base64-secret=bXktc2VjcmV0LWtleS13aGljaC1zaG91bGQtYmUtY2hhbmdlZC1pbi1wcm9kdWN0aW9uLWFuZC1iZS1iYXNlNjQtZW5jb2RlZA --spring.cloud.config.server.composite.0.type=native --spring.cloud.config.server.composite.0.search-locations=file:.\marketplace\src\main\docker\central-server-config\localhost-config
  - Esegui i due progetti apimanager e marketplace
