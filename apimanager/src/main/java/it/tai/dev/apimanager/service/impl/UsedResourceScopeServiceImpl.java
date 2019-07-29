package it.tai.dev.apimanager.service.impl;


import it.tai.dev.apimanager.domain.UsedResourceScope;
import it.tai.dev.apimanager.repository.UsedResourceScopeRepository;
import it.tai.dev.apimanager.service.KeycloakService;
import it.tai.dev.apimanager.service.UsedResourceScopeService;
import it.tai.dev.apimanager.service.dto.UsedResourceScopeDTO;
import it.tai.dev.apimanager.service.mapper.UsedResourceScopeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link UsedResourceScope}.
 */
@Service
@Transactional
public class UsedResourceScopeServiceImpl implements UsedResourceScopeService {

    private final Logger log = LoggerFactory.getLogger(UsedResourceScopeServiceImpl.class);

    private final UsedResourceScopeRepository usedResourceScopeRepository;

    private final UsedResourceScopeMapper usedResourceScopeMapper;
    private final KeycloakService keycloakService;

    public UsedResourceScopeServiceImpl(UsedResourceScopeRepository usedResourceScopeRepository,
                                        UsedResourceScopeMapper usedResourceScopeMapper,
                                        KeycloakService keycloakService) {
        this.usedResourceScopeRepository = usedResourceScopeRepository;
        this.usedResourceScopeMapper = usedResourceScopeMapper;
        this.keycloakService = keycloakService;
    }

    /**
     * Save a usedResourceScope.
     *
     * @param usedResourceScopeDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public UsedResourceScopeDTO save(UsedResourceScopeDTO usedResourceScopeDTO) {
        log.debug("Request to save UsedResourceScope : {}", usedResourceScopeDTO);
        UsedResourceScope usedResourceScope = usedResourceScopeMapper.toEntity(usedResourceScopeDTO);
        usedResourceScope = usedResourceScopeRepository.save(usedResourceScope);
        return usedResourceScopeMapper.toDto(usedResourceScope);
    }

    /**
     * Get all the usedResourceScopes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UsedResourceScopeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UsedResourceScopes");
        return usedResourceScopeRepository.findAll(pageable)
            .map(usedResourceScopeMapper::toDto);
    }


    /**
     * Get one usedResourceScope by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UsedResourceScopeDTO> findOne(Long id) {
        log.debug("Request to get UsedResourceScope : {}", id);
        return usedResourceScopeRepository.findById(id)
            .map(usedResourceScopeMapper::toDto);
    }


    /**
     * Delete the usedResourceScope by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UsedResourceScope : {}", id);
        usedResourceScopeRepository.findById(id)
            .ifPresent(usedResourceScope1 -> {
                Long clientId = usedResourceScope1.getResource().getId();
                Long apiScopeId = usedResourceScope1.getScope().getId();
                keycloakService.deleteKeycloakClientScopesFromClient(clientId, apiScopeId);
            });
        usedResourceScopeRepository.deleteById(id);
    }

    /**
     * Delete the usedResourceScope identified by "clientId" and "apiScopeId"
     *
     * @param clientId   the id of the client resource
     * @param apiScopeId the id of the resource scope with api resource
     */

    @Override
    public void delete(Long clientId, Long apiScopeId) {
        log.debug("Request to delete UsedResourceScope : {} - {}", clientId, apiScopeId);
        keycloakService.deleteKeycloakClientScopesFromClient(clientId, apiScopeId);
        usedResourceScopeRepository.deleteByResource_IdAndScope_Id(clientId, apiScopeId);
    }

    @Override
    public Optional<UsedResourceScopeDTO> findOne(Long resourceId, Long resourceScopeId) {
        log.debug("Request to get UsedResourceScope with resource_id e resource_scope_id: {} - {} ", resourceId, resourceScopeId);
        return usedResourceScopeRepository.findByResource_IdAndScope_Id(resourceId, resourceScopeId)
            .map(usedResourceScopeMapper::toDto);
    }

    @Override
    public List<Long> findResourceIdByScopeId(Long scopeId) {
        return usedResourceScopeRepository.findByScope_Id(scopeId);
    }
}
