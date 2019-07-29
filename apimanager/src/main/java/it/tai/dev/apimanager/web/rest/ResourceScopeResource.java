package it.tai.dev.apimanager.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import it.tai.dev.apimanager.service.ResourceScopeService;
import it.tai.dev.apimanager.service.dto.ResourceScopeCriteriaDTO;
import it.tai.dev.apimanager.service.dto.ResourceScopeDTO;
import it.tai.dev.apimanager.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing {@link it.tai.dev.apimanager.domain.ResourceScope}.
 */
@RestController
@RequestMapping("/api")
public class ResourceScopeResource {

    private final Logger log = LoggerFactory.getLogger(ResourceScopeResource.class);

    private static final String ENTITY_NAME = "resourceScope";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResourceScopeService resourceScopeService;

    public ResourceScopeResource(ResourceScopeService resourceScopeService) {
        this.resourceScopeService = resourceScopeService;
    }

    /**
     * {@code POST  /resource-scopes} : Create a new resourceScope.
     *
     * @param resourceScopeDTO the resourceScopeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resourceScopeDTO, or with status {@code 400 (Bad Request)} if the resourceScope has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/resource-scopes")
    public ResponseEntity<ResourceScopeDTO> createResourceScope(@Valid @RequestBody ResourceScopeDTO resourceScopeDTO) throws URISyntaxException {
        log.debug("REST request to save ResourceScope : {}", resourceScopeDTO);
        if (resourceScopeDTO.getId() != null) {
            throw new BadRequestAlertException("A new resourceScope cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ResourceScopeDTO result = resourceScopeService.save(resourceScopeDTO);
        return ResponseEntity.created(new URI("/api/resource-scopes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /resource-scopes} : Updates an existing resourceScope.
     *
     * @param resourceScopeDTO the resourceScopeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceScopeDTO,
     * or with status {@code 400 (Bad Request)} if the resourceScopeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resourceScopeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/resource-scopes")
    public ResponseEntity<ResourceScopeDTO> updateResourceScope(@Valid @RequestBody ResourceScopeDTO resourceScopeDTO) throws URISyntaxException {
        log.debug("REST request to update ResourceScope : {}", resourceScopeDTO);
        if (resourceScopeDTO.getId() == null)
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");

        ResourceScopeDTO result = resourceScopeService.update(resourceScopeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resourceScopeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /resource-scopes} : get all the resourceScopes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resourceScopes in body.
     */
    @GetMapping("/resource-scopes")
    public ResponseEntity<List<ResourceScopeDTO>> getAllResourceScopes(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of ResourceScopes");
        Page<ResourceScopeDTO> page = resourceScopeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /resource-scopes/:id} : get the "id" resourceScope.
     *
     * @param id the id of the resourceScopeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resourceScopeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/resource-scopes/{id}")
    public ResponseEntity<ResourceScopeDTO> getResourceScope(@PathVariable Long id) {
        log.debug("REST request to get ResourceScope : {}", id);
        Optional<ResourceScopeDTO> resourceScopeDTO = resourceScopeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(resourceScopeDTO);
    }

    /**
     * {@code DELETE  /resource-scopes/:id} : delete the "id" resourceScope.
     *
     * @param id the id of the resourceScopeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/resource-scopes/{id}")
    public ResponseEntity<Void> deleteResourceScope(@PathVariable Long id) {
        log.debug("REST request to delete ResourceScope : {}", id);
        resourceScopeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * Find all the resource scope associated to a resource with type API
     *
     * @param pageable                 pagination information
     * @param uriBuilder               uri builder information
     * @param resourceScopeCriteriaDTO query parameters
     * @return a list of resourceScopes
     */
    @GetMapping("/resource-scopes/all-api-resource-scope")
    public ResponseEntity<List<ResourceScopeDTO>> getAllResourceScopeWithApiResource(Pageable pageable, UriComponentsBuilder uriBuilder, ResourceScopeCriteriaDTO resourceScopeCriteriaDTO) {
        Page<ResourceScopeDTO> allResourceScopeWithApiResource = resourceScopeService.findAllResourceScopeWithApiResource(pageable, resourceScopeCriteriaDTO);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder, allResourceScopeWithApiResource);
        return ResponseEntity.ok().headers(headers).body(allResourceScopeWithApiResource.getContent());
    }

    /**
     *
     *
     * @param id id of the client scope
     * @param uriBuilder uri builder information
     * @return a collection of attribute scopes
     */
    @GetMapping("/resource-scopes/{id}/attribute-scopes")
    public ResponseEntity<Collection<String>> getAttributeScopes(@PathVariable Long id, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get ResourceScope attribute scopes : {}", id);

        //Optional<ResourceScopeDTO> resourceScopeDTO = resourceScopeService.findOne(id);
        List<String> attributeScopes = resourceScopeService.findAttributeScopes(id);
        Page<String> pagedAttributes = new PageImpl<>(attributeScopes);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder, pagedAttributes);
        return ResponseEntity.ok().headers(headers).body(pagedAttributes.getContent());
    }

    /**
     *
     * TODO Missing impl
     * @param id id of the client scope
     * @param uriBuilder uri builder information
     * @param attributes set of attributes to add
     * @return a collection of attribute scopes
     */
    @PutMapping("/resource-scopes/{id}/add-attribute-scope")
    public ResponseEntity<Collection<String>> addAttributeScopes(@PathVariable Long id, UriComponentsBuilder uriBuilder,@Valid @RequestBody Set<String> attributes) {
        log.debug("REST request to get ResourceScope attribute scopes : {}", id);
        resourceScopeService.addAttributeScope(id,attributes);
        return ResponseEntity.ok().build();
        //Optional<ResourceScopeDTO> resourceScopeDTO = resourceScopeService.findOne(id);
        /*List<String> attributeScopes = resourceScopeService.findAttributeScopes(id);
        Page<String> pagedAttributes = new PageImpl<>(attributeScopes);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder, pagedAttributes);
        return ResponseEntity.ok().headers(headers).body(pagedAttributes.getContent());*/
    }


}
