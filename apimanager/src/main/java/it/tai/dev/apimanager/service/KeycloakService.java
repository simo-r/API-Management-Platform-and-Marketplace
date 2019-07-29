package it.tai.dev.apimanager.service;


import it.tai.dev.apimanager.domain.Resource;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

public interface KeycloakService {

    /**
     * Updates the resource scope name
     *
     * @param resourceScopeId      old resource scope name
     * @param resourceId
     * @param newResourceScopeName new resource scope name
     */
    void updateKeycloakClientScope(Long resourceScopeId, Long resourceId, String newResourceScopeName);


    /**
     * Add a role with name "resourceScopeName" to the "keycloakClient" because
     * it owns the client scope "keycloakClient/resourceScopeName"
     *
     * @param resourceScopeName client scope name
     * @param keycloakClientId  client name
     */
    void addKeycloakRoleToClient(String resourceScopeName, Long keycloakClientId);


    /**
     * Add a new client scope to keycloak
     *
     * @param clientScopeId
     * @param clientScopeName name of the scope (resource_clientId/resourcescope_name)
     * @param authLevel
     */
    void addKeycloakClientScope(Long clientScopeId, String clientScopeName, Integer authLevel);


    /**
     * Remove the client scope with name "scopeName" associated to "clientId"
     *
     * @param clientScopeId   client id of the resource associated with the client scope
     * @param clientScopeName name of the scope
     * @param usedResourceIds
     */
    void deleteKeycloakClientScopes(Long clientScopeId, Long clientScopeName, List<Long> usedResourceIds);

    /**
     * @param resource resource dto to ass to keycloak
     * @return a response containing the result of the add
     */
    Response addKeycloakClient(Resource resource);


    /**
     * Updates the resource client id on keycloak
     *
     * @param resourceId        old resource client id
     * @param newClientId       new resource client id
     * @param resourceScopesIds
     */
    void updateKeycloakClient(Long resourceId, String newClientId, List<Long> resourceScopesIds);


    /**
     * Add client scope to a client resource in keycloak
     *
     * @param resourceId    client resource representation
     * @param clientScopeId resource scope name
     */
    void addKeycloakClientScopeToClient(Long resourceId, Long clientScopeId);

    /**
     * @param resourceName
     * @param scopeName
     * @return client scope name like <resource_name><split_char><scope_name>
     */
    String getClientScopeName(String resourceName, String scopeName);

    /**
     * @param clientId   id of the client
     * @param apiScopeId id of the client scope
     */
    void deleteKeycloakClientScopesFromClient(Long clientId, Long apiScopeId);

    /**
     * @param id id of the client
     */
    void deleteKeycloakClient(Long id);

    /**
     * Checks if all the attributes scopes names specified in the parameter
     * exist in one or more keycloak user
     *
     * @param attributeScopes set of attribute scope names
     * @return true all the elements in "attributeScopes" exists as attribute in users,
     * false otherwise
     */
    boolean validateAttributeScopes(Set<String> attributeScopes);

    /**
     * Adds a custom mapper to the client scope with id "id" and put
     * "attributescopes" inside the custom mapper
     *
     * @param id              id of the client scope
     * @param attributeScopes set of attributes scopes name
     */
    void createAttributeScopeToResourceScope(Long id, Set<String> attributeScopes);

    /**
     * @param id if of the client scope
     * @return list of attribute scopes associated to the client scope
     */
    List<String> getAttributeScopes(Long id);

    void addAttributeScopeToResourceScope(Long id, Set<String> attributes);

}
