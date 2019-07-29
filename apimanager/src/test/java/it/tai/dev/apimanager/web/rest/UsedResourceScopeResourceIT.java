package it.tai.dev.apimanager.web.rest;

import it.tai.dev.apimanager.ApimanagerApp;
import it.tai.dev.apimanager.domain.UsedResourceScope;
import it.tai.dev.apimanager.domain.Resource;
import it.tai.dev.apimanager.domain.ResourceScope;
import it.tai.dev.apimanager.repository.UsedResourceScopeRepository;
import it.tai.dev.apimanager.service.UsedResourceScopeService;
import it.tai.dev.apimanager.service.dto.UsedResourceScopeDTO;
import it.tai.dev.apimanager.service.mapper.UsedResourceScopeMapper;
import it.tai.dev.apimanager.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static it.tai.dev.apimanager.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link UsedResourceScopeResource} REST controller.
 */
@SpringBootTest(classes = ApimanagerApp.class)
public class UsedResourceScopeResourceIT {

    @Autowired
    private UsedResourceScopeRepository usedResourceScopeRepository;

    @Autowired
    private UsedResourceScopeMapper usedResourceScopeMapper;

    @Autowired
    private UsedResourceScopeService usedResourceScopeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restUsedResourceScopeMockMvc;

    private UsedResourceScope usedResourceScope;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UsedResourceScopeResource usedResourceScopeResource = new UsedResourceScopeResource(usedResourceScopeService);
        this.restUsedResourceScopeMockMvc = MockMvcBuilders.standaloneSetup(usedResourceScopeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UsedResourceScope createEntity(EntityManager em) {
        UsedResourceScope usedResourceScope = new UsedResourceScope();
        // Add required entity
        Resource resource;
        if (TestUtil.findAll(em, Resource.class).isEmpty()) {
            resource = ResourceResourceIT.createEntity(em);
            em.persist(resource);
            em.flush();
        } else {
            resource = TestUtil.findAll(em, Resource.class).get(0);
        }
        usedResourceScope.setResource(resource);
        // Add required entity
        ResourceScope resourceScope;
        if (TestUtil.findAll(em, ResourceScope.class).isEmpty()) {
            resourceScope = ResourceScopeResourceIT.createEntity(em);
            em.persist(resourceScope);
            em.flush();
        } else {
            resourceScope = TestUtil.findAll(em, ResourceScope.class).get(0);
        }
        usedResourceScope.setScope(resourceScope);
        return usedResourceScope;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UsedResourceScope createUpdatedEntity(EntityManager em) {
        UsedResourceScope usedResourceScope = new UsedResourceScope();
        // Add required entity
        Resource resource;
        if (TestUtil.findAll(em, Resource.class).isEmpty()) {
            resource = ResourceResourceIT.createUpdatedEntity(em);
            em.persist(resource);
            em.flush();
        } else {
            resource = TestUtil.findAll(em, Resource.class).get(0);
        }
        usedResourceScope.setResource(resource);
        // Add required entity
        ResourceScope resourceScope;
        if (TestUtil.findAll(em, ResourceScope.class).isEmpty()) {
            resourceScope = ResourceScopeResourceIT.createUpdatedEntity(em);
            em.persist(resourceScope);
            em.flush();
        } else {
            resourceScope = TestUtil.findAll(em, ResourceScope.class).get(0);
        }
        usedResourceScope.setScope(resourceScope);
        return usedResourceScope;
    }

    @BeforeEach
    public void initTest() {
        usedResourceScope = createEntity(em);
    }

    @Test
    @Transactional
    public void createUsedResourceScope() throws Exception {
        int databaseSizeBeforeCreate = usedResourceScopeRepository.findAll().size();

        // Create the UsedResourceScope
        UsedResourceScopeDTO usedResourceScopeDTO = usedResourceScopeMapper.toDto(usedResourceScope);
        restUsedResourceScopeMockMvc.perform(post("/api/used-resource-scopes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(usedResourceScopeDTO)))
            .andExpect(status().isCreated());

        // Validate the UsedResourceScope in the database
        List<UsedResourceScope> usedResourceScopeList = usedResourceScopeRepository.findAll();
        assertThat(usedResourceScopeList).hasSize(databaseSizeBeforeCreate + 1);
        UsedResourceScope testUsedResourceScope = usedResourceScopeList.get(usedResourceScopeList.size() - 1);
    }

    @Test
    @Transactional
    public void createUsedResourceScopeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = usedResourceScopeRepository.findAll().size();

        // Create the UsedResourceScope with an existing ID
        usedResourceScope.setId(1L);
        UsedResourceScopeDTO usedResourceScopeDTO = usedResourceScopeMapper.toDto(usedResourceScope);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUsedResourceScopeMockMvc.perform(post("/api/used-resource-scopes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(usedResourceScopeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UsedResourceScope in the database
        List<UsedResourceScope> usedResourceScopeList = usedResourceScopeRepository.findAll();
        assertThat(usedResourceScopeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllUsedResourceScopes() throws Exception {
        // Initialize the database
        usedResourceScopeRepository.saveAndFlush(usedResourceScope);

        // Get all the usedResourceScopeList
        restUsedResourceScopeMockMvc.perform(get("/api/used-resource-scopes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usedResourceScope.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getUsedResourceScope() throws Exception {
        // Initialize the database
        usedResourceScopeRepository.saveAndFlush(usedResourceScope);

        // Get the usedResourceScope
        restUsedResourceScopeMockMvc.perform(get("/api/used-resource-scopes/{id}", usedResourceScope.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(usedResourceScope.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUsedResourceScope() throws Exception {
        // Get the usedResourceScope
        restUsedResourceScopeMockMvc.perform(get("/api/used-resource-scopes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUsedResourceScope() throws Exception {
        // Initialize the database
        usedResourceScopeRepository.saveAndFlush(usedResourceScope);

        int databaseSizeBeforeUpdate = usedResourceScopeRepository.findAll().size();

        // Update the usedResourceScope
        UsedResourceScope updatedUsedResourceScope = usedResourceScopeRepository.findById(usedResourceScope.getId()).get();
        // Disconnect from session so that the updates on updatedUsedResourceScope are not directly saved in db
        em.detach(updatedUsedResourceScope);
        UsedResourceScopeDTO usedResourceScopeDTO = usedResourceScopeMapper.toDto(updatedUsedResourceScope);

        restUsedResourceScopeMockMvc.perform(put("/api/used-resource-scopes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(usedResourceScopeDTO)))
            .andExpect(status().isOk());

        // Validate the UsedResourceScope in the database
        List<UsedResourceScope> usedResourceScopeList = usedResourceScopeRepository.findAll();
        assertThat(usedResourceScopeList).hasSize(databaseSizeBeforeUpdate);
        UsedResourceScope testUsedResourceScope = usedResourceScopeList.get(usedResourceScopeList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingUsedResourceScope() throws Exception {
        int databaseSizeBeforeUpdate = usedResourceScopeRepository.findAll().size();

        // Create the UsedResourceScope
        UsedResourceScopeDTO usedResourceScopeDTO = usedResourceScopeMapper.toDto(usedResourceScope);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsedResourceScopeMockMvc.perform(put("/api/used-resource-scopes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(usedResourceScopeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UsedResourceScope in the database
        List<UsedResourceScope> usedResourceScopeList = usedResourceScopeRepository.findAll();
        assertThat(usedResourceScopeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUsedResourceScope() throws Exception {
        // Initialize the database
        usedResourceScopeRepository.saveAndFlush(usedResourceScope);

        int databaseSizeBeforeDelete = usedResourceScopeRepository.findAll().size();

        // Delete the usedResourceScope
        restUsedResourceScopeMockMvc.perform(delete("/api/used-resource-scopes/{id}", usedResourceScope.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<UsedResourceScope> usedResourceScopeList = usedResourceScopeRepository.findAll();
        assertThat(usedResourceScopeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UsedResourceScope.class);
        UsedResourceScope usedResourceScope1 = new UsedResourceScope();
        usedResourceScope1.setId(1L);
        UsedResourceScope usedResourceScope2 = new UsedResourceScope();
        usedResourceScope2.setId(usedResourceScope1.getId());
        assertThat(usedResourceScope1).isEqualTo(usedResourceScope2);
        usedResourceScope2.setId(2L);
        assertThat(usedResourceScope1).isNotEqualTo(usedResourceScope2);
        usedResourceScope1.setId(null);
        assertThat(usedResourceScope1).isNotEqualTo(usedResourceScope2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UsedResourceScopeDTO.class);
        UsedResourceScopeDTO usedResourceScopeDTO1 = new UsedResourceScopeDTO();
        usedResourceScopeDTO1.setId(1L);
        UsedResourceScopeDTO usedResourceScopeDTO2 = new UsedResourceScopeDTO();
        assertThat(usedResourceScopeDTO1).isNotEqualTo(usedResourceScopeDTO2);
        usedResourceScopeDTO2.setId(usedResourceScopeDTO1.getId());
        assertThat(usedResourceScopeDTO1).isEqualTo(usedResourceScopeDTO2);
        usedResourceScopeDTO2.setId(2L);
        assertThat(usedResourceScopeDTO1).isNotEqualTo(usedResourceScopeDTO2);
        usedResourceScopeDTO1.setId(null);
        assertThat(usedResourceScopeDTO1).isNotEqualTo(usedResourceScopeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(usedResourceScopeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(usedResourceScopeMapper.fromId(null)).isNull();
    }
}
