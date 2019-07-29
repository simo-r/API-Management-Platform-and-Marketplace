package it.tai.dev.apimanager.service.impl;


import io.github.jhipster.service.QueryService;
import io.github.jhipster.service.filter.Filter;
import it.tai.dev.apimanager.domain.Resource;
import it.tai.dev.apimanager.domain.ResourceScope;
import it.tai.dev.apimanager.domain.ResourceScope_;
import it.tai.dev.apimanager.domain.Resource_;
import it.tai.dev.apimanager.domain.enumeration.ResourceType;
import it.tai.dev.apimanager.repository.ResourceRepository;
import it.tai.dev.apimanager.repository.ResourceScopeRepository;
import it.tai.dev.apimanager.service.KeycloakService;
import it.tai.dev.apimanager.service.ResourceScopeService;
import it.tai.dev.apimanager.service.UsedResourceScopeService;
import it.tai.dev.apimanager.service.dto.ResourceScopeCriteriaDTO;
import it.tai.dev.apimanager.service.dto.ResourceScopeDTO;
import it.tai.dev.apimanager.service.mapper.ResourceScopeMapper;
import it.tai.dev.apimanager.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ResourceScope}.
 */
@Service
@Transactional
public class ResourceScopeServiceImpl extends QueryService<ResourceScope> implements ResourceScopeService {

    private final Logger log = LoggerFactory.getLogger(ResourceScopeServiceImpl.class);

    private final ResourceScopeRepository resourceScopeRepository;

    private final ResourceScopeMapper resourceScopeMapper;

    private final KeycloakService keycloakService;
    private final ResourceRepository resourceRepository;
    private final UsedResourceScopeService usedResourceScopeService;


    public ResourceScopeServiceImpl(ResourceScopeRepository resourceScopeRepository,
                                    ResourceScopeMapper resourceScopeMapper,
                                    KeycloakService keycloakService,
                                    ResourceRepository resourceRepository, UsedResourceScopeService usedResourceScopeService) {
        this.resourceScopeRepository = resourceScopeRepository;
        this.resourceScopeMapper = resourceScopeMapper;
        this.keycloakService = keycloakService;


        this.resourceRepository = resourceRepository;
        this.usedResourceScopeService = usedResourceScopeService;
    }

    /**
     * Save a resourceScope.
     *
     * @param resourceScopeDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ResourceScopeDTO save(ResourceScopeDTO resourceScopeDTO) {
        log.debug("Request to save ResourceScope : {}", resourceScopeDTO);
        /*if(!keycloakService.validateAttributeScopes(resourceScopeDTO.getAttributeScopes()))
            throw new BadRequestAlertException("Attribute scope not present","RESOURCE SCOPE","attributescopenotpresent");*/
        Resource resource = resourceRepository.getOne(resourceScopeDTO.getResourceId());
        saveValidationLogic(resource, resourceScopeDTO);
        ResourceScope resourceScope = resourceScopeMapper.toEntity(resourceScopeDTO);
        resourceScope = resourceScopeRepository.save(resourceScope);
        String keycloakClient = resource.getClientId();
        keycloakService.addKeycloakClientScope(resourceScope.getId(), keycloakService.getClientScopeName( keycloakClient ,resourceScope.getName()),resourceScopeDTO.getAuthLevel());
        keycloakService.addKeycloakRoleToClient(resourceScope.getName(), resource.getId());
        keycloakService.createAttributeScopeToResourceScope( resourceScope.getId(),resourceScopeDTO.getAttributeScopes());
        ResourceScopeDTO resultDTO = resourceScopeMapper.toDto(resourceScope);
        resultDTO.setAttributeScopes(resourceScopeDTO.getAttributeScopes());
        return resultDTO;
    }

    /**
     * Updates the informazioni associated with the resource scope id
     * in "resourceScopeDTO" and updates the client scope name in keycloak
     *
     * @param resourceScopeDTO new resource scope information
     * @return DTO of resource scope if the updates goes well
     */
    @Override
    public ResourceScopeDTO update(ResourceScopeDTO resourceScopeDTO) {
        log.debug("Request to update Resource : {}", resourceScopeDTO);
        ResourceScope resourceScope = resourceScopeMapper.toEntity(resourceScopeDTO);
        String oldResourceScopeName = resourceScopeRepository.getOne(resourceScope.getId()).getName();
        String newResourceScopeName = resourceScope.getName();
        String resourceClientId = resourceRepository.getOne(resourceScopeDTO.getResourceId()).getClientId();
        if (!oldResourceScopeName.equals(newResourceScopeName)) {
            //String oldClientScopeName = keycloakService.getClientScopeName(resourceClientId,oldResourceScopeName);
            String newClientScopeName = keycloakService.getClientScopeName(resourceClientId,newResourceScopeName);
            keycloakService.updateKeycloakClientScope(resourceScope.getId(),resourceScope.getResource().getId(),newClientScopeName);
        }
        resourceScope = resourceScopeRepository.save(resourceScope);
        return resourceScopeMapper.toDto(resourceScope);

    }

    /**
     * Checks if the resource is an API resource and there isn't another
     * one with same association resource_clientid - resourcescope_name
     *
     * @param resource resource to associate to a scope
     * @param resourceScopeDTO new resource scope
     * @throws BadRequestAlertException if it doesn't respect the validation properties
     */
    private void saveValidationLogic(Resource resource, ResourceScopeDTO resourceScopeDTO) {
        if (resource.getType() != ResourceType.API)
            throw new BadRequestAlertException("The selected resource is not an API resource", "Resource scope", "notapiresource");
        if (resourceScopeRepository.getAllByNameAndResource_Id(resourceScopeDTO.getName(),
            resourceScopeDTO.getResourceId()).isPresent())
            throw new BadRequestAlertException("Name already in use", "Resource scope", "alreadyusedname");
    }

    /**
     * Get all the resourceScopes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ResourceScopeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ResourceScopes");
        return resourceScopeRepository.findAll(pageable)
            .map(resourceScopeMapper::toDto);
    }


    /**
     * Get one resourceScope by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ResourceScopeDTO> findOne(Long id) {
        log.debug("Request to get ResourceScope : {}", id);
        return resourceScopeRepository.findById(id)
            .map(resourceScopeMapper::toDto);
    }

    /**
     * Delete the resourceScope by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ResourceScope : {}", id);
        ResourceScope one = resourceScopeRepository.getOne(id);
        Long clientResourceId = one.getResource().getId();
        List<Long> usedResourceIds = usedResourceScopeService.findResourceIdByScopeId(id);
        keycloakService.deleteKeycloakClientScopes(id,clientResourceId,usedResourceIds);
        resourceScopeRepository.deleteById(id);
    }

    /**
     * Find all the resource scope associated to a resource api
     * with the specified criteria
     *
     * @param pageable pagination information
     * @param resourceScopeCriteriaDTO criteria for the resource scope query
     * @return a page with the resourcescopes
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ResourceScopeDTO> findAllResourceScopeWithApiResource(Pageable pageable, @Valid ResourceScopeCriteriaDTO resourceScopeCriteriaDTO) {
        List<ResourceScopeDTO> all = resourceScopeRepository
            .findAll(createResourceScopeSpecification(resourceScopeCriteriaDTO), pageable)
            .stream()
            .map(resourceScopeMapper::toDto)
            .collect(Collectors.toList());
        return new PageImpl<>(all);
    }

    /**
     *
     * @param resourceId id of the resource (client for keycloak)
     * @return a list of resource scope id
     */
    @Override
    public List<Long> findAllResourceScopeIdWithResourceId(Long resourceId) {
        return resourceScopeRepository.findByResource_Id(resourceId);
    }

    /**
     *
     * @param id id of the client scope
     * @return list of attribute scopes associated to the client scope
     */
    @Override
    public List<String> findAttributeScopes(Long id) {
        return keycloakService.getAttributeScopes(id);
    }

    @Override
    public void addAttributeScope(Long id, Set<String> attributes) {
        keycloakService.addAttributeScopeToResourceScope(id,attributes);
    }

    /**
     * Creates a new specification for resource scope query
     *
     * @param resourceScopeCriteriaDTO criteria for the query
     * @return specification for the query
     */
    private Specification<ResourceScope> createResourceScopeSpecification(ResourceScopeCriteriaDTO resourceScopeCriteriaDTO) {
        Specification<ResourceScope> specification = Specification.where(null);
        specification = specification.and(buildReferringEntitySpecification(new Filter<ResourceType>().setEquals(ResourceType.API), ResourceScope_.resource, Resource_.type));
        if (resourceScopeCriteriaDTO.getName() != null) {
            specification = specification.and(buildStringSpecification(resourceScopeCriteriaDTO.getName(), (ResourceScope_.name)));
        }
        if (resourceScopeCriteriaDTO.getAuthLevel() != null) {
            specification = specification.and(buildSpecification(resourceScopeCriteriaDTO.getAuthLevel(), root -> root.get(ResourceScope_.authLevel)));
        }
        if (resourceScopeCriteriaDTO.getClientId() != null) {
            specification = specification
                .and(buildReferringEntitySpecification(resourceScopeCriteriaDTO.getClientId(),
                    ResourceScope_.resource, Resource_.clientId));
        }//TODO Come si fa con le enum?
        if (resourceScopeCriteriaDTO.getAuthTypeFilter() != null) {
            //log.debug("TESTERINO {} - {} ",resourceScopeCriteriaDTO.getAuthTypeFilter().getEquals().equals(AuthType.CNS),new Filter<AuthType>().setEquals(AuthType.CNS));
            /*Filter<AuthType> authTypeFilter = new Filter<AuthType>().setEquals(AuthType.CNS);*/
            specification = specification.and(buildSpecification(resourceScopeCriteriaDTO.getAuthTypeFilter(), (ResourceScope_.authType)));
        }
        return specification;
    }
}
