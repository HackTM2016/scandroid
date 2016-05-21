package com.scandroid.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.scandroid.domain.Application;
import com.scandroid.domain.Link;
import com.scandroid.domain.Scan;
import com.scandroid.repository.ApplicationRepository;
import com.scandroid.repository.LinkRepository;
import com.scandroid.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing Link.
 */
@RestController
@RequestMapping("/api")
public class LinkResource {

    private final Logger log = LoggerFactory.getLogger(LinkResource.class);

    @Inject
    private LinkRepository linkRepository;

    @Inject
    private ApplicationRepository applicationRepository;

    /**
     * POST  /links : Create a new link.
     *
     * @param link the link to create
     * @return the ResponseEntity with status 201 (Created) and with body the new link, or with status 400 (Bad Request) if the link has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/links",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Link> createLink(@RequestBody Link link) throws URISyntaxException {
        log.debug("REST request to save Link : {}", link);
        if (link.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("link", "idexists", "A new link cannot already have an ID")).body(null);
        }
        Link result = linkRepository.save(link);
        return ResponseEntity.created(new URI("/api/links/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("link", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /links : Updates an existing link.
     *
     * @param link the link to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated link,
     * or with status 400 (Bad Request) if the link is not valid,
     * or with status 500 (Internal Server Error) if the link couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/links",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Link> updateLink(@RequestBody Link link) throws URISyntaxException {
        log.debug("REST request to update Link : {}", link);
        if (link.getId() == null) {
            return createLink(link);
        }
        Link result = linkRepository.save(link);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("link", link.getId().toString()))
            .body(result);
    }

    /**
     * GET  /links : get all the links.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of links in body
     */
    @RequestMapping(value = "/links",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Link> getAllLinks() {
        log.debug("REST request to get all Links");
        List<Link> links = linkRepository.findAll();
        return links;
    }

    /**
     * GET  /links/:id : get the "id" link.
     *
     * @param id the id of the link to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the link, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/links/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Link> getLink(@PathVariable Long id) {
        log.debug("REST request to get Link : {}", id);
        Link link = linkRepository.findOne(id);
        return Optional.ofNullable(link)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /links/:id : get the "id" link.
     *
     * @param packageName the packageName of an app to receive
     * @return the ResponseEntity with status 200 (OK) and with body the link, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/links/packageName/{packageName:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    public Set<Link> getLink(@PathVariable String packageName) {
        log.debug("REST request to get Link for packageName: {}", packageName);
        List<Application> applications = applicationRepository.findAllByApplicationName(packageName);
        Set<Link> links = new HashSet<>(1);
        if(!applications.isEmpty()){
            for(Application a : applications){
                if(a.getScans().isEmpty()){
                    Link link = new Link();
                    link.setId(-2L);
                    link.setUrl("Application is not yet scanned, please return later");
                    links.add(link);
                }else{
                    for(Scan s : a.getScans()){
                        if(s.isSuccess()) {
                            return s.getLinks();
                        }else{
                            Link link = new Link();
                            link.setId(-3L);
                            link.setUrl("Application scan was not successful");
                            links.add(link);
                        }
                    }
                }
            }
        }else{
            log.debug("Adding new apk for scanner:"+packageName);
            Application newApplication = new Application();
            newApplication.setPackageName(packageName);
            newApplication.setName(packageName);
            newApplication.setDescription(packageName);
            applicationRepository.saveAndFlush(newApplication);
            Link link = new Link();
            link.setId(-1L);
            link.setUrl("Application is submitted for review, please return later");
            links.add(link);
        }
        return links;
    }

    /**
     * DELETE  /links/:id : delete the "id" link.
     *
     * @param id the id of the link to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/links/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLink(@PathVariable Long id) {
        log.debug("REST request to delete Link : {}", id);
        linkRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("link", id.toString())).build();
    }

}
