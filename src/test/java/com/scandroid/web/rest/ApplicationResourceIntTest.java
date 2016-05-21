package com.scandroid.web.rest;

import com.scandroid.ScandroidApp;
import com.scandroid.domain.Application;
import com.scandroid.repository.ApplicationRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ApplicationResource REST controller.
 *
 * @see ApplicationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ScandroidApp.class)
@WebAppConfiguration
@IntegrationTest
public class ApplicationResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_PACKAGE_NAME = "AAAAA";
    private static final String UPDATED_PACKAGE_NAME = "BBBBB";
    private static final String DEFAULT_URL = "AAAAA";
    private static final String UPDATED_URL = "BBBBB";

    private static final byte[] DEFAULT_ICON = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ICON = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_ICON_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ICON_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_APK = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_APK = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_APK_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_APK_CONTENT_TYPE = "image/png";
    private static final String DEFAULT_VERSION = "AAAAA";
    private static final String UPDATED_VERSION = "BBBBB";

    @Inject
    private ApplicationRepository applicationRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restApplicationMockMvc;

    private Application application;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ApplicationResource applicationResource = new ApplicationResource();
        ReflectionTestUtils.setField(applicationResource, "applicationRepository", applicationRepository);
        this.restApplicationMockMvc = MockMvcBuilders.standaloneSetup(applicationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        application = new Application();
        application.setName(DEFAULT_NAME);
        application.setDescription(DEFAULT_DESCRIPTION);
        application.setPackageName(DEFAULT_PACKAGE_NAME);
        application.setUrl(DEFAULT_URL);
        application.setIcon(DEFAULT_ICON);
        application.setIconContentType(DEFAULT_ICON_CONTENT_TYPE);
        application.setApk(DEFAULT_APK);
        application.setApkContentType(DEFAULT_APK_CONTENT_TYPE);
        application.setVersion(DEFAULT_VERSION);
    }

    @Test
    @Transactional
    public void createApplication() throws Exception {
        int databaseSizeBeforeCreate = applicationRepository.findAll().size();

        // Create the Application

        restApplicationMockMvc.perform(post("/api/applications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(application)))
                .andExpect(status().isCreated());

        // Validate the Application in the database
        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeCreate + 1);
        Application testApplication = applications.get(applications.size() - 1);
        assertThat(testApplication.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testApplication.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testApplication.getPackageName()).isEqualTo(DEFAULT_PACKAGE_NAME);
        assertThat(testApplication.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testApplication.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testApplication.getIconContentType()).isEqualTo(DEFAULT_ICON_CONTENT_TYPE);
        assertThat(testApplication.getApk()).isEqualTo(DEFAULT_APK);
        assertThat(testApplication.getApkContentType()).isEqualTo(DEFAULT_APK_CONTENT_TYPE);
        assertThat(testApplication.getVersion()).isEqualTo(DEFAULT_VERSION);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationRepository.findAll().size();
        // set the field null
        application.setName(null);

        // Create the Application, which fails.

        restApplicationMockMvc.perform(post("/api/applications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(application)))
                .andExpect(status().isBadRequest());

        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationRepository.findAll().size();
        // set the field null
        application.setDescription(null);

        // Create the Application, which fails.

        restApplicationMockMvc.perform(post("/api/applications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(application)))
                .andExpect(status().isBadRequest());

        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPackageNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationRepository.findAll().size();
        // set the field null
        application.setPackageName(null);

        // Create the Application, which fails.

        restApplicationMockMvc.perform(post("/api/applications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(application)))
                .andExpect(status().isBadRequest());

        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApplications() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applications
        restApplicationMockMvc.perform(get("/api/applications?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(application.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].packageName").value(hasItem(DEFAULT_PACKAGE_NAME.toString())))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
                .andExpect(jsonPath("$.[*].iconContentType").value(hasItem(DEFAULT_ICON_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].icon").value(hasItem(Base64Utils.encodeToString(DEFAULT_ICON))))
                .andExpect(jsonPath("$.[*].apkContentType").value(hasItem(DEFAULT_APK_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].apk").value(hasItem(Base64Utils.encodeToString(DEFAULT_APK))))
                .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.toString())));
    }

    @Test
    @Transactional
    public void getApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get the application
        restApplicationMockMvc.perform(get("/api/applications/{id}", application.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(application.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.packageName").value(DEFAULT_PACKAGE_NAME.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.iconContentType").value(DEFAULT_ICON_CONTENT_TYPE))
            .andExpect(jsonPath("$.icon").value(Base64Utils.encodeToString(DEFAULT_ICON)))
            .andExpect(jsonPath("$.apkContentType").value(DEFAULT_APK_CONTENT_TYPE))
            .andExpect(jsonPath("$.apk").value(Base64Utils.encodeToString(DEFAULT_APK)))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingApplication() throws Exception {
        // Get the application
        restApplicationMockMvc.perform(get("/api/applications/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();

        // Update the application
        Application updatedApplication = new Application();
        updatedApplication.setId(application.getId());
        updatedApplication.setName(UPDATED_NAME);
        updatedApplication.setDescription(UPDATED_DESCRIPTION);
        updatedApplication.setPackageName(UPDATED_PACKAGE_NAME);
        updatedApplication.setUrl(UPDATED_URL);
        updatedApplication.setIcon(UPDATED_ICON);
        updatedApplication.setIconContentType(UPDATED_ICON_CONTENT_TYPE);
        updatedApplication.setApk(UPDATED_APK);
        updatedApplication.setApkContentType(UPDATED_APK_CONTENT_TYPE);
        updatedApplication.setVersion(UPDATED_VERSION);

        restApplicationMockMvc.perform(put("/api/applications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedApplication)))
                .andExpect(status().isOk());

        // Validate the Application in the database
        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeUpdate);
        Application testApplication = applications.get(applications.size() - 1);
        assertThat(testApplication.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testApplication.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testApplication.getPackageName()).isEqualTo(UPDATED_PACKAGE_NAME);
        assertThat(testApplication.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testApplication.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testApplication.getIconContentType()).isEqualTo(UPDATED_ICON_CONTENT_TYPE);
        assertThat(testApplication.getApk()).isEqualTo(UPDATED_APK);
        assertThat(testApplication.getApkContentType()).isEqualTo(UPDATED_APK_CONTENT_TYPE);
        assertThat(testApplication.getVersion()).isEqualTo(UPDATED_VERSION);
    }

    @Test
    @Transactional
    public void deleteApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);
        int databaseSizeBeforeDelete = applicationRepository.findAll().size();

        // Get the application
        restApplicationMockMvc.perform(delete("/api/applications/{id}", application.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeDelete - 1);
    }
}
