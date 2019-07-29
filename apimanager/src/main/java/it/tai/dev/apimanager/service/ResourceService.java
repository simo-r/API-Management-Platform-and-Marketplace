package it.tai.dev.apimanager.service;

import it.tai.dev.apimanager.domain.enumeration.Status;
import it.tai.dev.apimanager.service.dto.ResourceDTO;
import it.tai.dev.apimanager.service.dto.SaveDTO;
import it.tai.dev.apimanager.service.dto.UpdateStatusDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link it.tai.dev.apimanager.domain.Resource}.
 */
public interface ResourceService {

    /**
     * Save a resource.
     *
     * @param resourceDTO the entity to save.
     * @return the persisted entity.
     */
    SaveDTO save(ResourceDTO resourceDTO);

    /**
     * Update a resource.
     *
     * @param resourceDTO the entity to update.
     * @return the persisted entity.
     */
    ResourceDTO update(ResourceDTO resourceDTO);

    /**
     * Get all the resources.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ResourceDTO> findAll(Pageable pageable);


    /**
     * Get the "id" resource.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ResourceDTO> findOne(Long id);

    /**
     * Delete the "id" resource.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);


    /**
     * Add an association between a client resource and a resource scope
     *
     * @param clientId id of the client resource
     * @param apiScopeId id of the resource scope with api resource
     * @return the new association
     */
    Optional<?> addResourceScopeToResource(Long clientId, Long apiScopeId);

    /**
     * Update resource status
     *
     * @param user username of the request
     * @param id resource id
     * @param next next status
     * @return DTO containing info about the request result
     */
    UpdateStatusDTO updateResourceStatus(String user, Long id, Status next);

    /**
     * Delete the association between a client resource and a resource scope
     *
     * @param clientId id of the client resource
     * @param apiScopeId id of the resource scope with api resource
     */
    void deleteResourceScopeToResource(Long clientId, Long apiScopeId);

}
