package it.tai.dev.apimanager.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import it.tai.dev.apimanager.domain.enumeration.Status;
import it.tai.dev.apimanager.domain.enumeration.ValidateErrorCodes;
import it.tai.dev.apimanager.service.ResourceService;
import it.tai.dev.apimanager.service.dto.ResourceDTO;
import it.tai.dev.apimanager.service.dto.SaveDTO;
import it.tai.dev.apimanager.service.dto.UpdateStatusDTO;
import it.tai.dev.apimanager.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link it.tai.dev.apimanager.domain.Resource}.
 */
@RestController
@RequestMapping("/api")
public class ResourceResource {

    private final Logger log = LoggerFactory.getLogger(ResourceResource.class);

    private static final String ENTITY_NAME = "resource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResourceService resourceService;

    public ResourceResource(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    /**
     * {@code POST  /resources} : Create a new resource.
     *
     * @param resourceDTO the resourceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resourceDTO, or with status {@code 400 (Bad Request)} if the resource has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/resources")
    // TODO Pre authorize end points by scopes in JWT
    //@PreAuthorize("#oauth2.('my-scope')")
    public ResponseEntity<?> createResource(@Valid @RequestBody ResourceDTO resourceDTO) throws URISyntaxException {
        log.debug("REST request to save Resource : {}", resourceDTO);
        if (resourceDTO.getId() != null) {
            throw new BadRequestAlertException("A new resource cannot already have an ID", ENTITY_NAME, "idexists");
        }else if(resourceDTO.getStatus() != Status.DRAFT){
            throw new BadRequestAlertException("A new resource must start in draft status", ENTITY_NAME, "wrongstatus");
        }

        SaveDTO result = resourceService.save(resourceDTO);
        int responseStatus = result.getResponse().getStatus();
        ResponseEntity<?> body;
        if (responseStatus == HttpStatus.CREATED.value()) {
            ResourceDTO resultDTO = result.getResourceDTO();
            body = ResponseEntity.created(new URI("/api/resources/" + resultDTO.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, resultDTO.getId().toString()))
                .body(resultDTO);
        } else {
            body = ResponseEntity.status(responseStatus).body(result.getResponse().getStatusInfo());
        }
        return body;
    }

    /**
     * {@code PUT  /resources} : Updates an existing resource.
     *
     * @param resourceDTO the resourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceDTO,
     * or with status {@code 400 (Bad Request)} if the resourceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/resources")
    public ResponseEntity<ResourceDTO> updateResource(@Valid @RequestBody ResourceDTO resourceDTO) throws URISyntaxException{
        log.debug("REST request to update Resource : {}", resourceDTO);
        if (resourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ResourceDTO result = resourceService.update(resourceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName,
                true, ENTITY_NAME, resourceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /resources} : get all the resources.
     *  TODO Ritornare SOLO le risorse con stato published
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resources in body.
     */
    @GetMapping("/resources")
    public ResponseEntity<List<ResourceDTO>> getAllResources(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Resources");
        Page<ResourceDTO> page = resourceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /resources/:id} : get the "id" resource.
     *
     * @param id the id of the resourceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resourceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/resources/{id}")
    public ResponseEntity<ResourceDTO> getResource(@PathVariable Long id) {
        log.debug("REST request to get Resource : {}", id);
        Optional<ResourceDTO> resourceDTO = resourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(resourceDTO);
    }

    /**
     * {@code DELETE  /resources/:id} : delete the "id" resource.
     *
     * @param id the id of the resourceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/resources/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        log.debug("REST request to delete Resource : {}", id);
        resourceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code POST  /resource/{clientid}/usedresourcescope/{apiscopeid}} : Create a new association between a
     *                                                      client resource and a resource scope with api resource
     *
     * @param apiscopeid the id of the resource scope of the resource api
     * @param clientid id of the client resource
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new association, or with status {@code 400 (Bad Request)} if it was a bad request.
     */
    @PostMapping("/resource/{clientid}/usedresourcescope/{apiscopeid}")
    public ResponseEntity<?> addResourceScopeToResource(@PathVariable Long clientid, @PathVariable Long apiscopeid){
        log.debug("REST request to add UsedResourceScope : {} - {}", clientid,apiscopeid);
        Optional<?> resourceDTO = resourceService.addResourceScopeToResource(clientid, apiscopeid);
        return ResponseUtil.wrapOrNotFound(resourceDTO);
    }

    /**
     * {@code DELETE  /resource/{clientid}/usedresourcescope/{apiscopeid}} : Create a new association between a
     *                                                      client resource and a resource scope with api resource
     *
     * @param apiscopeid the id of the resource scope of the resource api
     * @param clientid id of the client resource
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resourceDTO, or with status {@code 400 (Bad Request)} if the resource has already an ID.
     */
    @DeleteMapping("/resource/{clientid}/usedresourcescope/{apiscopeid}")
    public ResponseEntity<Void> deleteResourceScopeToResource(@PathVariable Long clientid,@PathVariable Long apiscopeid){
        log.debug("REST request to delete UsedResourceScope: {} - {}", apiscopeid,clientid);
        resourceService.deleteResourceScopeToResource(clientid,apiscopeid);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, clientid.toString())).build();
    }


    /**
     * Changes the status of the resource with id "id" to the status "next"
     *
     * @param id id of the resource to update the status
     * @param next next status of the resource
     * @param authentication authentication request
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceDTO, or with status {@code 404 (Not Found)}.
     * @throws BadRequestAlertException if the next status is not valid
     */
    @PutMapping("/resources/{id}/transition/{next}")
    public ResponseEntity<ResourceDTO> updateResourceStatus(@PathVariable Long id, @PathVariable Status next, Authentication authentication) throws BadRequestAlertException {
        log.debug("REST request to update Resource status : id {} - next {} - user {}", id, next,authentication.getName());
        UpdateStatusDTO result = resourceService.updateResourceStatus(authentication.getName(),id,next);
        if(result.getValidateErrorCodes() != ValidateErrorCodes.OK){
            throw new BadRequestAlertException("TEST",
                ENTITY_NAME,"TEST");
        }
        return ResponseUtil.wrapOrNotFound(result.getResourceDTO());
    }
}
