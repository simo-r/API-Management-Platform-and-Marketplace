# Api Management Platform and Marketplace

This projects has been developed as microservices with RESTful interactions.

# Requirements
  - You must have keycloak configured as in the application.yml specification in ~/src/main/resources/config)

# How to easy run
  - Clone this repository
  - Consider folder gateway and apimanager as two separate projects
    - Execute npm install on both of them
  - Download Jhipster registry https://github.com/jhipster/jhipster-registry/releases (Used version: v 5.0.2)
  - Run JHipster registry as:
    - ./jhipster-registry-5.0.2.jar --spring.security.user.password=admin --jhipster.security.authentication.jwt.base64-secret=bXktc2VjcmV0LWtleS13aGljaC1zaG91bGQtYmUtY2hhbmdlZC1pbi1wcm9kdWN0aW9uLWFuZC1iZS1iYXNlNjQtZW5jb2RlZA --spring.cloud.config.server.composite.0.type=native --spring.cloud.config.server.composite.0.search-locations=file:.\marketplace\src\main\docker\central-server-config\localhost-config
  - Execute both the gateway and the apimanager
