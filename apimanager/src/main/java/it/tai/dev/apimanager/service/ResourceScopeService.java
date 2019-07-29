package it.tai.dev.apimanager.service;

import it.tai.dev.apimanager.service.dto.ResourceScopeCriteriaDTO;
import it.tai.dev.apimanager.service.dto.ResourceScopeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service Interface for managing {@link it.tai.dev.apimanager.domain.ResourceScope}.
 */
public interface ResourceScopeService {

    /**
     * Save a resourceScope.
     *
     * @param resourceScopeDTO the entity to save.
     * @return the persisted entity.
     */
    ResourceScopeDTO save(ResourceScopeDTO resourceScopeDTO);

    /**
     * Updates the informazioni associated with the resource scope id
     * in "resourceScopeDTO" and updates the client scope name in keycloak
     *
     * @param resourceScopeDTO new resource scope information
     * @return DTO of resource scope if the updates goes well
     */
    ResourceScopeDTO update(ResourceScopeDTO resourceScopeDTO);

    /**
     * Get all the resourceScopes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ResourceScopeDTO> findAll(Pageable pageable);


    /**
     * Get the "id" resourceScope.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ResourceScopeDTO> findOne(Long id);

    /**
     * Delete the "id" resourceScope.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Find all the resource scope associated to a resource api
     * with the specified criteria
     *
     * @param pageable pagination information
     * @param resourceScopeCriteriaDTO criteria for the resource scope query
     * @return a page with the resourcescopes
     */
    Page<ResourceScopeDTO> findAllResourceScopeWithApiResource(Pageable pageable, ResourceScopeCriteriaDTO resourceScopeCriteriaDTO);

    /**
     *
     * @param resourceId id of the resource (client for keycloak)
     * @return a list of resource scope id
     */
    List<Long> findAllResourceScopeIdWithResourceId(Long resourceId);

    /**
     *
     * @param id id of the client scope
     * @return list of attribute scopes associated to the client scope
     */
    List<String> findAttributeScopes(Long id);

    void addAttributeScope(Long id, Set<String> attributes);

}
