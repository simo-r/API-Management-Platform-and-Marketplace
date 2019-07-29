package it.tai.dev.apimanager.service.impl;

import it.tai.dev.apimanager.domain.Resource;
import it.tai.dev.apimanager.domain.enumeration.ResourceType;
import it.tai.dev.apimanager.service.KeycloakService;
import it.tai.dev.apimanager.web.rest.errors.BadRequestAlertException;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.ClientScopeResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.representations.idm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.Response;
import java.util.*;

@Service
@Transactional /*TODO Mi serve?*/
public class KeycloakServiceImpl implements KeycloakService {
    private static final String OPENID_CONNECT = "openid-connect";
    private static final String SPLIT_CHAR = "/";
    private final Logger log = LoggerFactory.getLogger(ResourceScopeServiceImpl.class);

    private final RealmResource realmResource;

    public KeycloakServiceImpl(RealmResource realmResource) {
        this.realmResource = realmResource;
    }

    /**
     * Updates the resource scope name
     *
     * @param resourceScopeId      old resource scope name
     * @param resourceId           field id of the client (resource)
     * @param newResourceScopeName new resource scope name
     */
    @Override
    public void updateKeycloakClientScope(Long resourceScopeId, Long resourceId, String newResourceScopeName) {
        ClientScopeResource clientScopeResource = realmResource.clientScopes().get(resourceScopeId.toString());
        ClientScopeRepresentation clientScopeRepresentation = clientScopeResource.toRepresentation();
        // Save the old name because i need it to update the role
        String oldRoleName = clientScopeRepresentation.getName().split(SPLIT_CHAR)[1];
        clientScopeRepresentation.setName(newResourceScopeName);
        clientScopeResource.update(clientScopeRepresentation);
        String newRoleName = newResourceScopeName.split(SPLIT_CHAR)[1];
        updateKeycloakClientRole(resourceId, oldRoleName, newRoleName);
        // Client scope in client resources will be auto-updated by keycloak :)
    }

    /**
     * Updates the role name associated to a client
     *
     * @param resourceId  id of the client
     * @param oldRoleName name of the old role
     * @param newRoleName name of the new role
     */
    private void updateKeycloakClientRole(Long resourceId, String oldRoleName, String newRoleName) {

        ClientResource clientResource = realmResource.clients().get(resourceId.toString());
        RoleResource roleResource = clientResource.roles().get(oldRoleName);
        RoleRepresentation roleRepresentation = roleResource.toRepresentation();
        roleRepresentation.setName(newRoleName);
        roleResource.update(roleRepresentation);
    }

    /**
     * Add a role with name "resourceScopeName" to the "keycloakClient" because
     * it owns the client scope "keycloakClient-resourceScopeName"
     *
     * @param resourceScopeName client scope name
     * @param keycloakClientId  client name
     */
    @Override
    public void addKeycloakRoleToClient(String resourceScopeName, Long keycloakClientId) {
        log.debug("ADD keycloak role to client : SCOPE {} - CLIENT {}", resourceScopeName, keycloakClientId);
        ClientResource clientResource = realmResource.clients().get(keycloakClientId.toString());
        RoleRepresentation roleRepresentation = new RoleRepresentation();
        // Maybe set the id too
        roleRepresentation.setName(resourceScopeName);
        clientResource.roles().create(roleRepresentation);
    }

    /**
     * WORKING
     * Add a new client scope to keycloak
     *  @param clientScopeId   id of the client scope on keycloak
     * @param clientScopeName name of the scope (resource_clientId/resourcescope_name)
     * @param authLevel
     */
    @Override
    public void addKeycloakClientScope(Long clientScopeId, String clientScopeName, Integer authLevel) {
        log.debug("Request to add keycloak client scope: {} - {}", clientScopeId, clientScopeName);
        ClientScopeRepresentation clientScopeRepresentation = new ClientScopeRepresentation();
        clientScopeRepresentation.setId(clientScopeId.toString());
        clientScopeRepresentation.setName(clientScopeName);
        clientScopeRepresentation.setProtocol(OPENID_CONNECT);
        Map<String, String> attributes = clientScopeRepresentation.getAttributes();
        if(attributes == null) attributes = new HashMap<>();
        attributes.put("auth_level",authLevel.toString());
        clientScopeRepresentation.setAttributes(attributes);
        Response response = realmResource.clientScopes().create(clientScopeRepresentation);
        keycloakOperationResponseLogic(response);
    }

    /**
     * Remove the client scope with name "scopeName" associated to "clientId"
     *
     * @param clientScopeId    client id of the resource associated with the client scope
     * @param clientResourceId name of the resource
     * @param usedResourceIds  aaa
     */
    @Override
    public void deleteKeycloakClientScopes(Long clientScopeId, Long clientResourceId, List<Long> usedResourceIds) {
        log.debug("DELETE KEYCLOAK CLIENT SCOPES: {} - {}", clientScopeId, clientResourceId);
        ClientResource clientResource = realmResource.clients().get(clientResourceId.toString());
        ClientScopeResource clientScopeResource = realmResource.clientScopes().get(clientScopeId.toString());
        String roleName = clientScopeResource.toRepresentation().getName().split(SPLIT_CHAR)[1];
        // First remove scope usages from other clients
        usedResourceIds.forEach(aLong -> realmResource.clients().get(aLong.toString()).removeOptionalClientScope(clientScopeId.toString()));
        // Then remove role from api resource
        clientResource.roles().deleteRole(roleName);
        // Finally remove the client scope
        clientScopeResource.remove();
    }

    /**
     * @param resource resource dto to ass to keycloak
     * @return a response containing the result of the add
     */
    @Override
    public Response addKeycloakClient(Resource resource) {
        log.debug("ADD new keycloak client with client ID: {}", resource.getClientId());
        ClientRepresentation clientRepresentation = new ClientRepresentation();
        ArrayList<String> strings = new ArrayList<>();
        strings.add(resource.getRedirectUrl());
        clientRepresentation.setRedirectUris(strings);
        clientRepresentation.setPublicClient(false);
        clientRepresentation.setProtocol(OPENID_CONNECT);
        clientRepresentation.setEnabled(true);
        clientRepresentation.setId(resource.getId().toString());
        clientRepresentation.setClientId(resource.getClientId());
        clientRepresentation.setName(resource.getClientId());
        Response creationResponse = realmResource.clients().create(clientRepresentation);
        if (resource.getType() == ResourceType.API) clientRepresentation.setDirectAccessGrantsEnabled(false);
        log.debug("ADD new keycloak client RESPONSE: {} - {}", creationResponse.getStatus(), creationResponse.getStatusInfo());
        return creationResponse;
    }

    /**
     * Updates the resource client id on keycloak
     *
     * @param resourceId        old resource client id
     * @param newClientId       new resource client id
     * @param resourceScopesIds id list of client scopes associated to the client
     */
    @Override
    public void updateKeycloakClient(Long resourceId, String newClientId, List<Long> resourceScopesIds) {
        ClientResource clientResource = realmResource.clients().get(resourceId.toString());
        ClientRepresentation clientRepresentation = clientResource.toRepresentation();
        clientRepresentation.setClientId(newClientId);
        clientRepresentation.setName(newClientId);
        // Updates each client scope created by this client
        for (Long resourceScopesId : resourceScopesIds) {
            ClientScopeResource clientScopeResource = realmResource.clientScopes().get(resourceScopesId.toString());
            ClientScopeRepresentation clientScopeRepresentation = clientScopeResource.toRepresentation();
            String[] splits = clientScopeRepresentation.getName().split(SPLIT_CHAR);
            String newScopeName = getClientScopeName(newClientId, splits[1]);
            clientScopeRepresentation.setName(newScopeName);
            clientScopeResource.update(clientScopeRepresentation);
            //Keycloak will auto updated used client scopes
        }
        clientResource.update(clientRepresentation);
    }

    /**
     * Add client scope to a client resource in keycloak
     *
     * @param resourceId    client resource representation
     * @param clientScopeId resource scope name
     */
    @Override
    public void addKeycloakClientScopeToClient(Long resourceId, Long clientScopeId) {
        log.debug("ADD keycloak client scope to client: {} - {}", clientScopeId, resourceId);
        ClientResource clientResource = realmResource.clients().get(resourceId.toString());
        clientResource.addOptionalClientScope(clientScopeId.toString());
    }

    /**
     * @param response response from keycloak
     * @throws BadRequestAlertException if the http code in the response is not CREATED
     */
    private void keycloakOperationResponseLogic(Response response) {
        if (response.getStatus() != HttpStatus.CREATED.value()) {
            throw new BadRequestAlertException(response.getStatusInfo().toString(), "Resource scope", "Errore bello");
        }
    }

    /**
     * @param resourceName name of the client
     * @param scopeName name of the client scope
     * @return client scope name like <resource_name><split_char><scope_name>
     */
    @Override
    public String getClientScopeName(String resourceName, String scopeName) {
        // Uses - instead of / cause keycloak web pages doesn't allow / as id of a client scope (breaks the client scope web page)
        return resourceName + SPLIT_CHAR + scopeName;
    }

    /**
     * @param clientId   id of the client
     * @param apiScopeId id of the client scope
     */
    @Override
    public void deleteKeycloakClientScopesFromClient(Long clientId, Long apiScopeId) {
        realmResource.clients().get(clientId.toString()).removeOptionalClientScope(apiScopeId.toString());
    }

    /**
     * @param id id of the client
     */
    @Override
    public void deleteKeycloakClient(Long id) {
        realmResource.clients().get(id.toString()).remove();
    }

    /**
     * Checks if all the attributes scopes names specified in the parameter
     * exist in one or more keycloak user
     *
     * @param attributeScopes set of attribute scope names
     * @return true all the elements in "attributeScopes" exists as attribute in users,
     * false otherwise
     */
    @Override
    public boolean validateAttributeScopes(Set<String> attributeScopes) {
        if (attributeScopes == null || attributeScopes.size() == 0) return true;
        Set<String> attributeSet = new LinkedHashSet<>();
        List<UserRepresentation> list = realmResource.users().list();
        if (list != null)
            list.forEach(userRepresentation -> {
                Map<String, List<String>> attrs = userRepresentation.getAttributes();
                if (attrs != null) attrs.forEach((s, strings) -> attributeSet.add(s));
            });

        return attributeSet.containsAll(attributeScopes);
    }

    /**
     * Adds a custom mapper to the client scope with id "id" and put
     * "attributescopes" inside the custom mapper
     *
     * @param id              id of the client scope
     * @param attributeScopes set of attributes scopes name
     */
    @Override
    public void createAttributeScopeToResourceScope(Long id, Set<String> attributeScopes) {
        if (attributeScopes == null) return;
        log.debug("ADD mapper to client scope");
        String attributeScopeId = buildAttributeScopeId(id);
        ClientScopeResource clientScope = realmResource.clientScopes().get(id.toString());
        ProtocolMapperRepresentation protocolMapperRepresentation = new ProtocolMapperRepresentation();
        protocolMapperRepresentation.setId(attributeScopeId);
        protocolMapperRepresentation.setProtocolMapper("oidc-custommapper");
        protocolMapperRepresentation.setName("Attribute Scope");
        protocolMapperRepresentation.setProtocol(OPENID_CONNECT);
        Map<String, String> config = new HashMap<>();
        StringBuilder stringBuilder = new StringBuilder();
        attributeScopes.forEach(s -> {
            stringBuilder.append(s);
            stringBuilder.append("##");
        });
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        config.put("id.token.claim", "true");
        config.put("userinfo.token.claim", "true");
        config.put("access.token.claim", "true");
        config.put("jsonType.label", "String");
        config.put("attributeList", stringBuilder.toString());
        protocolMapperRepresentation.setConfig(config);
        clientScope.getProtocolMappers().createMapper(protocolMapperRepresentation);
    }

    /**
     * @param id id of the client scope
     * @return a string representing the id of the attribute scope
     */
    private String buildAttributeScopeId(Long id) {
        return "attrib" + id;
    }

    /**
     *
     * @param id if of the client scope
     * @return list of attribute scopes associated to the client scope
     */
    public List<String> getAttributeScopes(Long id) {
        ClientScopeResource clientScopeResource = realmResource.clientScopes().get(id.toString());
        ProtocolMapperRepresentation mapperRepresentation = clientScopeResource.getProtocolMappers().getMapperById(buildAttributeScopeId(id));
        if (mapperRepresentation == null) {
            log.debug("No mapper");
            return Collections.emptyList();
        }
        String attributeList = mapperRepresentation.getConfig().get("attributeList");
        return Arrays.asList(attributeList.split("##"));
    }

    @Override
    public void addAttributeScopeToResourceScope(Long id, Set<String> attributes) {
        String attributeId = buildAttributeScopeId(id);
        ClientScopeResource clientScopeResource = realmResource.clientScopes().get(id.toString());
        ProtocolMapperRepresentation attributeMapper = clientScopeResource.getProtocolMappers().getMapperById(attributeId);
        String attributeList = attributeMapper.getConfig().get("attributeList");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(attributeList);
        stringBuilder.append("##");
        attributes.forEach(attr -> {stringBuilder.append(attr); stringBuilder.append("##");});
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        attributeMapper.getConfig().put("attributeList",stringBuilder.toString());
        clientScopeResource.getProtocolMappers().update(attributeId,attributeMapper);

    }

}
