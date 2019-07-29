package it.tai.dev.apimanager.service.impl;


import it.tai.dev.apimanager.domain.Resource;
import it.tai.dev.apimanager.domain.enumeration.ResourceToResourceScopeError;
import it.tai.dev.apimanager.domain.enumeration.ResourceType;
import it.tai.dev.apimanager.domain.enumeration.Status;
import it.tai.dev.apimanager.domain.enumeration.ValidateErrorCodes;
import it.tai.dev.apimanager.repository.ResourceRepository;
import it.tai.dev.apimanager.service.KeycloakService;
import it.tai.dev.apimanager.service.ResourceScopeService;
import it.tai.dev.apimanager.service.ResourceService;
import it.tai.dev.apimanager.service.UsedResourceScopeService;
import it.tai.dev.apimanager.service.dto.*;
import it.tai.dev.apimanager.service.mapper.ResourceMapper;
import it.tai.dev.apimanager.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Resource}.
 */
@Service
@Transactional
public class ResourceServiceImpl implements ResourceService {

    private final Logger log = LoggerFactory.getLogger(ResourceServiceImpl.class);

    private final ResourceRepository resourceRepository;

    private final ResourceMapper resourceMapper;
    private final UsedResourceScopeService usedResourceScopeService;
    private final ResourceScopeService resourceScopeService;
    private final KeycloakService keycloakService;

    public ResourceServiceImpl(ResourceRepository resourceRepository, ResourceMapper resourceMapper,
                               UsedResourceScopeService usedResourceScopeService, ResourceScopeService resourceScopeService,
                               KeycloakService keycloakService) {
        this.resourceRepository = resourceRepository;
        this.resourceMapper = resourceMapper;
        this.usedResourceScopeService = usedResourceScopeService;
        this.resourceScopeService = resourceScopeService;
        this.keycloakService = keycloakService;
    }

    /**
     * Save a resource.
     *
     * @param resourceDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public SaveDTO save(ResourceDTO resourceDTO) {
        log.debug("Request to save Resource : {}", resourceDTO);
        if (resourceRepository.findFirstByClientId(resourceDTO.getClientId()).isPresent()) {
            throw new BadRequestAlertException("Client id already present", "RESOURCE", "client_id_already_present");
        }
        Resource resource = resourceMapper.toEntity(resourceDTO);
        resource = resourceRepository.save(resource);
        Response keycloakAddResponse = keycloakService.addKeycloakClient(resource);
        if (keycloakAddResponse.getStatus() != HttpStatus.CREATED.value()) {
            delete(resource.getId());
            resource = null;
        }
        return new SaveDTO(resourceMapper.toDto(resource), keycloakAddResponse);
    }


    /**
     * Update a resource.
     *
     * @param resourceDTO the entity to update.
     * @return the persisted entity.
     */
    @Override
    public ResourceDTO update(ResourceDTO resourceDTO) {
        log.debug("Request to update Resource : {}", resourceDTO);
        Resource resource = resourceMapper.toEntity(resourceDTO);
        String oldClientId = resourceRepository.getOne(resource.getId()).getClientId();
        String newClientId = resource.getClientId();
        resource = resourceRepository.save(resource);

        if (!oldClientId.equals(newClientId)) {
            List<Long> resourceScopesIds = resourceScopeService.findAllResourceScopeIdWithResourceId(resourceDTO.getId());
            keycloakService.updateKeycloakClient(resourceDTO.getId(), newClientId, resourceScopesIds);
        }


        return resourceMapper.toDto(resource);
    }


    /**
     * Get all the resources.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ResourceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Resources");
        return resourceRepository.findAll(pageable)
            .map(resourceMapper::toDto);
    }


    /**
     * Get one resource by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ResourceDTO> findOne(Long id) {
        log.debug("Request to get Resource : {}", id);
        return resourceRepository.findById(id)
            .map(resourceMapper::toDto);
    }

    /**
     * Delete the resource by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Resource : {}", id);
        Resource resourceToDelete = resourceRepository.getOne(id);
        if (resourceToDelete.getType() == ResourceType.API) {
            // Delete all client scopes associated to it
            List<Long> allResourceScopeIdWithResourceId = resourceScopeService.findAllResourceScopeIdWithResourceId(id);
            allResourceScopeIdWithResourceId.forEach(resourceScopeService::delete);
        }
        keycloakService.deleteKeycloakClient(id);
        resourceRepository.deleteById(id);
    }

    /**
     * Add an association between a client resource and a resource scope
     *
     * @param clientId   id of the client resource
     * @param apiScopeId id of the resource scope with api resource
     * @return the new association
     */
    @Override
    public Optional<?> addResourceScopeToResource(Long clientId, Long apiScopeId) {
        log.debug("Request to add resource scope to resource {} - {}", clientId, apiScopeId);
        Optional<ResourceToResourceScopeError> resourceToResourceScopeError = validateAddResourceScopeToResource(clientId, apiScopeId);
        if ( resourceToResourceScopeError.isPresent() &&
                resourceToResourceScopeError.get() != ResourceToResourceScopeError.OK )
            return resourceToResourceScopeError;
        keycloakService.addKeycloakClientScopeToClient(clientId, apiScopeId);
        UsedResourceScopeDTO usedResourceScopeDTO = new UsedResourceScopeDTO();
        usedResourceScopeDTO.setResourceId(clientId);
        usedResourceScopeDTO.setScopeId(apiScopeId);
        usedResourceScopeService.save(usedResourceScopeDTO);
        return Optional.of(usedResourceScopeDTO);
    }


    /**
     *
     * @param clientId id of the client
     * @param apiScopeId id of the resource scope associated to an api resource
     * @return a code representing the result of the validation
     */
    private Optional<ResourceToResourceScopeError> validateAddResourceScopeToResource(Long clientId, Long apiScopeId) {
        if (usedResourceScopeService.findOne(clientId, apiScopeId).isPresent())
            return Optional.of(ResourceToResourceScopeError.ALREADY_PRESENT);
        Optional<ResourceDTO> clientResource = findOne(clientId);
        if (!clientResource.isPresent() || clientResource.get().getType() != ResourceType.CLIENT)
            return Optional.of(ResourceToResourceScopeError.NOT_A_CLIENT_RESOURCE);
        Optional<ResourceScopeDTO> resourceScope = resourceScopeService.findOne(apiScopeId);
        if (!resourceScope.isPresent())
            return Optional.of(ResourceToResourceScopeError.RESOURCE_SCOPE_NOT_EXIST);
        Optional<ResourceDTO> apiResource = findOne(resourceScope.get().getResourceId());
        if (!apiResource.isPresent() || apiResource.get().getType() != ResourceType.API)
            return Optional.of(ResourceToResourceScopeError.NOT_AN_API_RESOURCE);
        return Optional.of(ResourceToResourceScopeError.OK);
    }


    /**
     * Update resource status
     *
     * @param user username of the request
     * @param id   resource id
     * @param next next status
     * @return DTO containing info about the request result
     */
    @Override
    public UpdateStatusDTO updateResourceStatus(String user, Long id, Status next) {
        log.debug("Request to update the status of resource with id {} to status {} from user {}", id, next, user);
        Optional<ResourceDTO> resource = findOne(id);
        ValidateErrorCodes validateErrorCodes = ValidateErrorCodes.NOT_EXIST;
        if (resource.isPresent()) {
            ResourceDTO resourceDTO = resource.get();
            validateErrorCodes = validateStatusTransition(user, resourceDTO, next);
            if (validateErrorCodes == ValidateErrorCodes.OK) {
                resourceDTO.setStatus(next);
                update(resourceDTO);
            }
        }
        return new UpdateStatusDTO(resource, validateErrorCodes);
    }

    /**
     * Delete the association between a client resource and a resource scope
     *
     * @param clientId   id of the client resource
     * @param apiScopeId id of the resource scope with api resource
     */
    @Override
    public void deleteResourceScopeToResource(Long clientId, Long apiScopeId) {
        log.debug("DELETE resource scope to resource {} - {}", clientId, apiScopeId);
        usedResourceScopeService.delete(clientId, apiScopeId);
    }

    /**
     * Logic of resource state transition validation
     *
     * @param user        username of the request
     * @param resourceDTO resource to update status
     * @param next        next status
     * @return code representing the result of the request
     */
    private ValidateErrorCodes validateStatusTransition(String user, ResourceDTO resourceDTO, Status next) {
        Status currentStatus = resourceDTO.getStatus();
        if (next == Status.ARCHIVED) return ValidateErrorCodes.OK;
        else if (currentStatus == Status.DRAFT && next == Status.PENDING) {
            if (!resourceDTO.getOwner().equals(user)) return ValidateErrorCodes.UNAUTHORIZED;
            return ValidateErrorCodes.OK;
        } else if ((currentStatus == Status.PENDING && next == Status.REJECTED ||
            currentStatus == Status.PENDING && next == Status.PUBLISHED)) {
            if (!resourceDTO.getApprover().equals(user)) return ValidateErrorCodes.UNAUTHORIZED;
            return ValidateErrorCodes.OK;
        }
        return ValidateErrorCodes.INVALID_TRANSITION;
    }


}
