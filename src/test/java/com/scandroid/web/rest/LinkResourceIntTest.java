package com.scandroid.web.rest;

import com.scandroid.ScandroidApp;
import com.scandroid.domain.Link;
import com.scandroid.repository.LinkRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the LinkResource REST controller.
 *
 * @see LinkResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ScandroidApp.class)
@WebAppConfiguration
@IntegrationTest
public class LinkResourceIntTest {

    private static final String DEFAULT_URL = "AAAAA";
    private static final String UPDATED_URL = "BBBBB";
    private static final String DEFAULT_POST_DATA = "AAAAA";
    private static final String UPDATED_POST_DATA = "BBBBB";

    private static final Boolean DEFAULT_SUSPECT = false;
    private static final Boolean UPDATED_SUSPECT = true;

    @Inject
    private LinkRepository linkRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLinkMockMvc;

    private Link link;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LinkResource linkResource = new LinkResource();
        ReflectionTestUtils.setField(linkResource, "linkRepository", linkRepository);
        this.restLinkMockMvc = MockMvcBuilders.standaloneSetup(linkResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        link = new Link();
        link.setUrl(DEFAULT_URL);
        link.setPostData(DEFAULT_POST_DATA);
        link.setSuspect(DEFAULT_SUSPECT);
    }

    @Test
    @Transactional
    public void createLink() throws Exception {
        int databaseSizeBeforeCreate = linkRepository.findAll().size();

        // Create the Link

        restLinkMockMvc.perform(post("/api/links")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(link)))
                .andExpect(status().isCreated());

        // Validate the Link in the database
        List<Link> links = linkRepository.findAll();
        assertThat(links).hasSize(databaseSizeBeforeCreate + 1);
        Link testLink = links.get(links.size() - 1);
        assertThat(testLink.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testLink.getPostData()).isEqualTo(DEFAULT_POST_DATA);
        assertThat(testLink.isSuspect()).isEqualTo(DEFAULT_SUSPECT);
    }

    @Test
    @Transactional
    public void getAllLinks() throws Exception {
        // Initialize the database
        linkRepository.saveAndFlush(link);

        // Get all the links
        restLinkMockMvc.perform(get("/api/links?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(link.getId().intValue())))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
                .andExpect(jsonPath("$.[*].postData").value(hasItem(DEFAULT_POST_DATA.toString())))
                .andExpect(jsonPath("$.[*].suspect").value(hasItem(DEFAULT_SUSPECT.booleanValue())));
    }

    @Test
    @Transactional
    public void getLink() throws Exception {
        // Initialize the database
        linkRepository.saveAndFlush(link);

        // Get the link
        restLinkMockMvc.perform(get("/api/links/{id}", link.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(link.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.postData").value(DEFAULT_POST_DATA.toString()))
            .andExpect(jsonPath("$.suspect").value(DEFAULT_SUSPECT.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLink() throws Exception {
        // Get the link
        restLinkMockMvc.perform(get("/api/links/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLink() throws Exception {
        // Initialize the database
        linkRepository.saveAndFlush(link);
        int databaseSizeBeforeUpdate = linkRepository.findAll().size();

        // Update the link
        Link updatedLink = new Link();
        updatedLink.setId(link.getId());
        updatedLink.setUrl(UPDATED_URL);
        updatedLink.setPostData(UPDATED_POST_DATA);
        updatedLink.setSuspect(UPDATED_SUSPECT);

        restLinkMockMvc.perform(put("/api/links")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLink)))
                .andExpect(status().isOk());

        // Validate the Link in the database
        List<Link> links = linkRepository.findAll();
        assertThat(links).hasSize(databaseSizeBeforeUpdate);
        Link testLink = links.get(links.size() - 1);
        assertThat(testLink.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testLink.getPostData()).isEqualTo(UPDATED_POST_DATA);
        assertThat(testLink.isSuspect()).isEqualTo(UPDATED_SUSPECT);
    }

    @Test
    @Transactional
    public void deleteLink() throws Exception {
        // Initialize the database
        linkRepository.saveAndFlush(link);
        int databaseSizeBeforeDelete = linkRepository.findAll().size();

        // Get the link
        restLinkMockMvc.perform(delete("/api/links/{id}", link.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Link> links = linkRepository.findAll();
        assertThat(links).hasSize(databaseSizeBeforeDelete - 1);
    }
}
