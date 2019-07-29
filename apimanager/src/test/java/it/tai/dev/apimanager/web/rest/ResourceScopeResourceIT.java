package it.tai.dev.apimanager.web.rest;

import it.tai.dev.apimanager.ApimanagerApp;
import it.tai.dev.apimanager.domain.ResourceScope;
import it.tai.dev.apimanager.domain.UsedResourceScope;
import it.tai.dev.apimanager.domain.Resource;
import it.tai.dev.apimanager.repository.ResourceScopeRepository;
import it.tai.dev.apimanager.service.ResourceScopeService;
import it.tai.dev.apimanager.service.dto.ResourceScopeDTO;
import it.tai.dev.apimanager.service.mapper.ResourceScopeMapper;
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

import it.tai.dev.apimanager.domain.enumeration.AuthType;
/**
 * Integration tests for the {@Link ResourceScopeResource} REST controller.
 */
@SpringBootTest(classes = ApimanagerApp.class)
public class ResourceScopeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_AUTH_LEVEL = 1;
    private static final Integer UPDATED_AUTH_LEVEL = 2;

    private static final AuthType DEFAULT_AUTH_TYPE = AuthType.CNS;
    private static final AuthType UPDATED_AUTH_TYPE = AuthType.SPID;

    @Autowired
    private ResourceScopeRepository resourceScopeRepository;

    @Autowired
    private ResourceScopeMapper resourceScopeMapper;

    @Autowired
    private ResourceScopeService resourceScopeService;

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

    private MockMvc restResourceScopeMockMvc;

    private ResourceScope resourceScope;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ResourceScopeResource resourceScopeResource = new ResourceScopeResource(resourceScopeService);
        this.restResourceScopeMockMvc = MockMvcBuilders.standaloneSetup(resourceScopeResource)
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
    public static ResourceScope createEntity(EntityManager em) {
        ResourceScope resourceScope = new ResourceScope()
            .name(DEFAULT_NAME)
            .authLevel(DEFAULT_AUTH_LEVEL)
            .authType(DEFAULT_AUTH_TYPE);
        // Add required entity
        UsedResourceScope usedResourceScope;
        if (TestUtil.findAll(em, UsedResourceScope.class).isEmpty()) {
            usedResourceScope = UsedResourceScopeResourceIT.createEntity(em);
            em.persist(usedResourceScope);
            em.flush();
        } else {
            usedResourceScope = TestUtil.findAll(em, UsedResourceScope.class).get(0);
        }
        resourceScope.getUsedBies().add(usedResourceScope);
        // Add required entity
        Resource resource;
        if (TestUtil.findAll(em, Resource.class).isEmpty()) {
            resource = ResourceResourceIT.createEntity(em);
            em.persist(resource);
            em.flush();
        } else {
            resource = TestUtil.findAll(em, Resource.class).get(0);
        }
        resourceScope.setResource(resource);
        return resourceScope;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResourceScope createUpdatedEntity(EntityManager em) {
        ResourceScope resourceScope = new ResourceScope()
            .name(UPDATED_NAME)
            .authLevel(UPDATED_AUTH_LEVEL)
            .authType(UPDATED_AUTH_TYPE);
        // Add required entity
        UsedResourceScope usedResourceScope;
        if (TestUtil.findAll(em, UsedResourceScope.class).isEmpty()) {
            usedResourceScope = UsedResourceScopeResourceIT.createUpdatedEntity(em);
            em.persist(usedResourceScope);
            em.flush();
        } else {
            usedResourceScope = TestUtil.findAll(em, UsedResourceScope.class).get(0);
        }
        resourceScope.getUsedBies().add(usedResourceScope);
        // Add required entity
        Resource resource;
        if (TestUtil.findAll(em, Resource.class).isEmpty()) {
            resource = ResourceResourceIT.createUpdatedEntity(em);
            em.persist(resource);
            em.flush();
        } else {
            resource = TestUtil.findAll(em, Resource.class).get(0);
        }
        resourceScope.setResource(resource);
        return resourceScope;
    }

    @BeforeEach
    public void initTest() {
        resourceScope = createEntity(em);
    }

    @Test
    @Transactional
    public void createResourceScope() throws Exception {
        int databaseSizeBeforeCreate = resourceScopeRepository.findAll().size();

        // Create the ResourceScope
        ResourceScopeDTO resourceScopeDTO = resourceScopeMapper.toDto(resourceScope);
        restResourceScopeMockMvc.perform(post("/api/resource-scopes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resourceScopeDTO)))
            .andExpect(status().isCreated());

        // Validate the ResourceScope in the database
        List<ResourceScope> resourceScopeList = resourceScopeRepository.findAll();
        assertThat(resourceScopeList).hasSize(databaseSizeBeforeCreate + 1);
        ResourceScope testResourceScope = resourceScopeList.get(resourceScopeList.size() - 1);
        assertThat(testResourceScope.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testResourceScope.getAuthLevel()).isEqualTo(DEFAULT_AUTH_LEVEL);
        assertThat(testResourceScope.getAuthType()).isEqualTo(DEFAULT_AUTH_TYPE);
    }

    @Test
    @Transactional
    public void createResourceScopeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = resourceScopeRepository.findAll().size();

        // Create the ResourceScope with an existing ID
        resourceScope.setId(1L);
        ResourceScopeDTO resourceScopeDTO = resourceScopeMapper.toDto(resourceScope);

        // An entity with an existing ID cannot be created, so this API call must fail
        restResourceScopeMockMvc.perform(post("/api/resource-scopes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resourceScopeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ResourceScope in the database
        List<ResourceScope> resourceScopeList = resourceScopeRepository.findAll();
        assertThat(resourceScopeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = resourceScopeRepository.findAll().size();
        // set the field null
        resourceScope.setName(null);

        // Create the ResourceScope, which fails.
        ResourceScopeDTO resourceScopeDTO = resourceScopeMapper.toDto(resourceScope);

        restResourceScopeMockMvc.perform(post("/api/resource-scopes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resourceScopeDTO)))
            .andExpect(status().isBadRequest());

        List<ResourceScope> resourceScopeList = resourceScopeRepository.findAll();
        assertThat(resourceScopeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAuthLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = resourceScopeRepository.findAll().size();
        // set the field null
        resourceScope.setAuthLevel(null);

        // Create the ResourceScope, which fails.
        ResourceScopeDTO resourceScopeDTO = resourceScopeMapper.toDto(resourceScope);

        restResourceScopeMockMvc.perform(post("/api/resource-scopes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resourceScopeDTO)))
            .andExpect(status().isBadRequest());

        List<ResourceScope> resourceScopeList = resourceScopeRepository.findAll();
        assertThat(resourceScopeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAuthTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = resourceScopeRepository.findAll().size();
        // set the field null
        resourceScope.setAuthType(null);

        // Create the ResourceScope, which fails.
        ResourceScopeDTO resourceScopeDTO = resourceScopeMapper.toDto(resourceScope);

        restResourceScopeMockMvc.perform(post("/api/resource-scopes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resourceScopeDTO)))
            .andExpect(status().isBadRequest());

        List<ResourceScope> resourceScopeList = resourceScopeRepository.findAll();
        assertThat(resourceScopeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllResourceScopes() throws Exception {
        // Initialize the database
        resourceScopeRepository.saveAndFlush(resourceScope);

        // Get all the resourceScopeList
        restResourceScopeMockMvc.perform(get("/api/resource-scopes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resourceScope.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].authLevel").value(hasItem(DEFAULT_AUTH_LEVEL)))
            .andExpect(jsonPath("$.[*].authType").value(hasItem(DEFAULT_AUTH_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getResourceScope() throws Exception {
        // Initialize the database
        resourceScopeRepository.saveAndFlush(resourceScope);

        // Get the resourceScope
        restResourceScopeMockMvc.perform(get("/api/resource-scopes/{id}", resourceScope.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(resourceScope.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.authLevel").value(DEFAULT_AUTH_LEVEL))
            .andExpect(jsonPath("$.authType").value(DEFAULT_AUTH_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingResourceScope() throws Exception {
        // Get the resourceScope
        restResourceScopeMockMvc.perform(get("/api/resource-scopes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResourceScope() throws Exception {
        // Initialize the database
        resourceScopeRepository.saveAndFlush(resourceScope);

        int databaseSizeBeforeUpdate = resourceScopeRepository.findAll().size();

        // Update the resourceScope
        ResourceScope updatedResourceScope = resourceScopeRepository.findById(resourceScope.getId()).get();
        // Disconnect from session so that the updates on updatedResourceScope are not directly saved in db
        em.detach(updatedResourceScope);
        updatedResourceScope
            .name(UPDATED_NAME)
            .authLevel(UPDATED_AUTH_LEVEL)
            .authType(UPDATED_AUTH_TYPE);
        ResourceScopeDTO resourceScopeDTO = resourceScopeMapper.toDto(updatedResourceScope);

        restResourceScopeMockMvc.perform(put("/api/resource-scopes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resourceScopeDTO)))
            .andExpect(status().isOk());

        // Validate the ResourceScope in the database
        List<ResourceScope> resourceScopeList = resourceScopeRepository.findAll();
        assertThat(resourceScopeList).hasSize(databaseSizeBeforeUpdate);
        ResourceScope testResourceScope = resourceScopeList.get(resourceScopeList.size() - 1);
        assertThat(testResourceScope.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testResourceScope.getAuthLevel()).isEqualTo(UPDATED_AUTH_LEVEL);
        assertThat(testResourceScope.getAuthType()).isEqualTo(UPDATED_AUTH_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingResourceScope() throws Exception {
        int databaseSizeBeforeUpdate = resourceScopeRepository.findAll().size();

        // Create the ResourceScope
        ResourceScopeDTO resourceScopeDTO = resourceScopeMapper.toDto(resourceScope);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResourceScopeMockMvc.perform(put("/api/resource-scopes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resourceScopeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ResourceScope in the database
        List<ResourceScope> resourceScopeList = resourceScopeRepository.findAll();
        assertThat(resourceScopeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteResourceScope() throws Exception {
        // Initialize the database
        resourceScopeRepository.saveAndFlush(resourceScope);

        int databaseSizeBeforeDelete = resourceScopeRepository.findAll().size();

        // Delete the resourceScope
        restResourceScopeMockMvc.perform(delete("/api/resource-scopes/{id}", resourceScope.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<ResourceScope> resourceScopeList = resourceScopeRepository.findAll();
        assertThat(resourceScopeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResourceScope.class);
        ResourceScope resourceScope1 = new ResourceScope();
        resourceScope1.setId(1L);
        ResourceScope resourceScope2 = new ResourceScope();
        resourceScope2.setId(resourceScope1.getId());
        assertThat(resourceScope1).isEqualTo(resourceScope2);
        resourceScope2.setId(2L);
        assertThat(resourceScope1).isNotEqualTo(resourceScope2);
        resourceScope1.setId(null);
        assertThat(resourceScope1).isNotEqualTo(resourceScope2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResourceScopeDTO.class);
        ResourceScopeDTO resourceScopeDTO1 = new ResourceScopeDTO();
        resourceScopeDTO1.setId(1L);
        ResourceScopeDTO resourceScopeDTO2 = new ResourceScopeDTO();
        assertThat(resourceScopeDTO1).isNotEqualTo(resourceScopeDTO2);
        resourceScopeDTO2.setId(resourceScopeDTO1.getId());
        assertThat(resourceScopeDTO1).isEqualTo(resourceScopeDTO2);
        resourceScopeDTO2.setId(2L);
        assertThat(resourceScopeDTO1).isNotEqualTo(resourceScopeDTO2);
        resourceScopeDTO1.setId(null);
        assertThat(resourceScopeDTO1).isNotEqualTo(resourceScopeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(resourceScopeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(resourceScopeMapper.fromId(null)).isNull();
    }
}
