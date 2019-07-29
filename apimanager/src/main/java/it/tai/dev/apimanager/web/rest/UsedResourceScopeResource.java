package it.tai.dev.apimanager.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import it.tai.dev.apimanager.service.UsedResourceScopeService;
import it.tai.dev.apimanager.service.dto.UsedResourceScopeDTO;
import it.tai.dev.apimanager.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link it.tai.dev.apimanager.domain.UsedResourceScope}.
 */
@RestController
@RequestMapping("/api")
public class UsedResourceScopeResource {

    private final Logger log = LoggerFactory.getLogger(UsedResourceScopeResource.class);

    private static final String ENTITY_NAME = "usedResourceScope";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UsedResourceScopeService usedResourceScopeService;

    public UsedResourceScopeResource(UsedResourceScopeService usedResourceScopeService) {
        this.usedResourceScopeService = usedResourceScopeService;
    }

    /**
     * {@code POST  /used-resource-scopes} : Create a new usedResourceScope.
     *
     * @param usedResourceScopeDTO the usedResourceScopeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new usedResourceScopeDTO, or with status {@code 400 (Bad Request)} if the usedResourceScope has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/used-resource-scopes")
    public ResponseEntity<UsedResourceScopeDTO> createUsedResourceScope(@Valid @RequestBody UsedResourceScopeDTO usedResourceScopeDTO) throws URISyntaxException {
        log.debug("REST request to save UsedResourceScope : {}", usedResourceScopeDTO);
        if (usedResourceScopeDTO.getId() != null) {
            throw new BadRequestAlertException("A new usedResourceScope cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UsedResourceScopeDTO result = usedResourceScopeService.save(usedResourceScopeDTO);
        return ResponseEntity.created(new URI("/api/used-resource-scopes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /used-resource-scopes} : Updates an existing usedResourceScope.
     *
     * @param usedResourceScopeDTO the usedResourceScopeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated usedResourceScopeDTO,
     * or with status {@code 400 (Bad Request)} if the usedResourceScopeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the usedResourceScopeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/used-resource-scopes")
    public ResponseEntity<UsedResourceScopeDTO> updateUsedResourceScope(@Valid @RequestBody UsedResourceScopeDTO usedResourceScopeDTO) throws URISyntaxException {
        log.debug("REST request to update UsedResourceScope : {}", usedResourceScopeDTO);
        if (usedResourceScopeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UsedResourceScopeDTO result = usedResourceScopeService.save(usedResourceScopeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, usedResourceScopeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /used-resource-scopes} : get all the usedResourceScopes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of usedResourceScopes in body.
     */
    @GetMapping("/used-resource-scopes")
    public ResponseEntity<List<UsedResourceScopeDTO>> getAllUsedResourceScopes(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of UsedResourceScopes");
        Page<UsedResourceScopeDTO> page = usedResourceScopeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /used-resource-scopes/:id} : get the "id" usedResourceScope.
     *
     * @param id the id of the usedResourceScopeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the usedResourceScopeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/used-resource-scopes/{id}")
    public ResponseEntity<UsedResourceScopeDTO> getUsedResourceScope(@PathVariable Long id) {
        log.debug("REST request to get UsedResourceScope : {}", id);
        Optional<UsedResourceScopeDTO> usedResourceScopeDTO = usedResourceScopeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(usedResourceScopeDTO);
    }

    /**
     * {@code DELETE  /used-resource-scopes/:id} : delete the "id" usedResourceScope.
     *
     * @param id the id of the usedResourceScopeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/used-resource-scopes/{id}")
    public ResponseEntity<Void> deleteUsedResourceScope(@PathVariable Long id) {
        log.debug("REST request to delete UsedResourceScope : {}", id);
        usedResourceScopeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }



}
