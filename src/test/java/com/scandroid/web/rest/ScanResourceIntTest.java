package com.scandroid.web.rest;

import com.scandroid.ScandroidApp;
import com.scandroid.domain.Scan;
import com.scandroid.repository.ScanRepository;

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

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ScanResource REST controller.
 *
 * @see ScanResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ScandroidApp.class)
@WebAppConfiguration
@IntegrationTest
public class ScanResourceIntTest {


    private static final LocalDate DEFAULT_UPDATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_SUCCESS = false;
    private static final Boolean UPDATED_SUCCESS = true;

    @Inject
    private ScanRepository scanRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restScanMockMvc;

    private Scan scan;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ScanResource scanResource = new ScanResource();
        ReflectionTestUtils.setField(scanResource, "scanRepository", scanRepository);
        this.restScanMockMvc = MockMvcBuilders.standaloneSetup(scanResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        scan = new Scan();
        scan.setUpdated(DEFAULT_UPDATED);
        scan.setSuccess(DEFAULT_SUCCESS);
    }

    @Test
    @Transactional
    public void createScan() throws Exception {
        int databaseSizeBeforeCreate = scanRepository.findAll().size();

        // Create the Scan

        restScanMockMvc.perform(post("/api/scans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scan)))
                .andExpect(status().isCreated());

        // Validate the Scan in the database
        List<Scan> scans = scanRepository.findAll();
        assertThat(scans).hasSize(databaseSizeBeforeCreate + 1);
        Scan testScan = scans.get(scans.size() - 1);
        assertThat(testScan.getUpdated()).isEqualTo(DEFAULT_UPDATED);
        assertThat(testScan.isSuccess()).isEqualTo(DEFAULT_SUCCESS);
    }

    @Test
    @Transactional
    public void getAllScans() throws Exception {
        // Initialize the database
        scanRepository.saveAndFlush(scan);

        // Get all the scans
        restScanMockMvc.perform(get("/api/scans?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(scan.getId().intValue())))
                .andExpect(jsonPath("$.[*].updated").value(hasItem(DEFAULT_UPDATED.toString())))
                .andExpect(jsonPath("$.[*].success").value(hasItem(DEFAULT_SUCCESS.booleanValue())));
    }

    @Test
    @Transactional
    public void getScan() throws Exception {
        // Initialize the database
        scanRepository.saveAndFlush(scan);

        // Get the scan
        restScanMockMvc.perform(get("/api/scans/{id}", scan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(scan.getId().intValue()))
            .andExpect(jsonPath("$.updated").value(DEFAULT_UPDATED.toString()))
            .andExpect(jsonPath("$.success").value(DEFAULT_SUCCESS.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingScan() throws Exception {
        // Get the scan
        restScanMockMvc.perform(get("/api/scans/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateScan() throws Exception {
        // Initialize the database
        scanRepository.saveAndFlush(scan);
        int databaseSizeBeforeUpdate = scanRepository.findAll().size();

        // Update the scan
        Scan updatedScan = new Scan();
        updatedScan.setId(scan.getId());
        updatedScan.setUpdated(UPDATED_UPDATED);
        updatedScan.setSuccess(UPDATED_SUCCESS);

        restScanMockMvc.perform(put("/api/scans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedScan)))
                .andExpect(status().isOk());

        // Validate the Scan in the database
        List<Scan> scans = scanRepository.findAll();
        assertThat(scans).hasSize(databaseSizeBeforeUpdate);
        Scan testScan = scans.get(scans.size() - 1);
        assertThat(testScan.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testScan.isSuccess()).isEqualTo(UPDATED_SUCCESS);
    }

    @Test
    @Transactional
    public void deleteScan() throws Exception {
        // Initialize the database
        scanRepository.saveAndFlush(scan);
        int databaseSizeBeforeDelete = scanRepository.findAll().size();

        // Get the scan
        restScanMockMvc.perform(delete("/api/scans/{id}", scan.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Scan> scans = scanRepository.findAll();
        assertThat(scans).hasSize(databaseSizeBeforeDelete - 1);
    }
}
