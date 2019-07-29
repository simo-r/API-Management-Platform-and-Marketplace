package it.tai.dev.apimanager.service;

import it.tai.dev.apimanager.service.dto.UsedResourceScopeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link it.tai.dev.apimanager.domain.UsedResourceScope}.
 */
public interface UsedResourceScopeService {

    /**
     * Save a usedResourceScope.
     *
     * @param usedResourceScopeDTO the entity to save.
     * @return the persisted entity.
     */
    UsedResourceScopeDTO save(UsedResourceScopeDTO usedResourceScopeDTO);

    /**
     * Get all the usedResourceScopes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UsedResourceScopeDTO> findAll(Pageable pageable);


    /**
     * Get the "id" usedResourceScope.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UsedResourceScopeDTO> findOne(Long id);

    /**
     * Delete the "id" usedResourceScope.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);


    /**
     * Delete the usedResourceScope identified by "clientId" and "apiScopeId"
     *
     * @param clientId the id of the client resource
     * @param apiScopeId the id of the resource scope with api resource
     */
    void delete(Long clientId, Long apiScopeId);


    Optional<UsedResourceScopeDTO> findOne(Long resourceId, Long resourceScopeId);

    List<Long> findResourceIdByScopeId(Long scopeId);
}
