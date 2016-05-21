package com.scandroid.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.scandroid.domain.Scan;
import com.scandroid.repository.ScanRepository;
import com.scandroid.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Scan.
 */
@RestController
@RequestMapping("/api")
public class ScanResource {

    private final Logger log = LoggerFactory.getLogger(ScanResource.class);
        
    @Inject
    private ScanRepository scanRepository;
    
    /**
     * POST  /scans : Create a new scan.
     *
     * @param scan the scan to create
     * @return the ResponseEntity with status 201 (Created) and with body the new scan, or with status 400 (Bad Request) if the scan has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/scans",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Scan> createScan(@RequestBody Scan scan) throws URISyntaxException {
        log.debug("REST request to save Scan : {}", scan);
        if (scan.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("scan", "idexists", "A new scan cannot already have an ID")).body(null);
        }
        Scan result = scanRepository.save(scan);
        return ResponseEntity.created(new URI("/api/scans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("scan", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /scans : Updates an existing scan.
     *
     * @param scan the scan to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated scan,
     * or with status 400 (Bad Request) if the scan is not valid,
     * or with status 500 (Internal Server Error) if the scan couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/scans",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Scan> updateScan(@RequestBody Scan scan) throws URISyntaxException {
        log.debug("REST request to update Scan : {}", scan);
        if (scan.getId() == null) {
            return createScan(scan);
        }
        Scan result = scanRepository.save(scan);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("scan", scan.getId().toString()))
            .body(result);
    }

    /**
     * GET  /scans : get all the scans.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of scans in body
     */
    @RequestMapping(value = "/scans",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Scan> getAllScans() {
        log.debug("REST request to get all Scans");
        List<Scan> scans = scanRepository.findAll();
        return scans;
    }

    /**
     * GET  /scans/:id : get the "id" scan.
     *
     * @param id the id of the scan to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the scan, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/scans/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Scan> getScan(@PathVariable Long id) {
        log.debug("REST request to get Scan : {}", id);
        Scan scan = scanRepository.findOne(id);
        return Optional.ofNullable(scan)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /scans/:id : delete the "id" scan.
     *
     * @param id the id of the scan to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/scans/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteScan(@PathVariable Long id) {
        log.debug("REST request to delete Scan : {}", id);
        scanRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("scan", id.toString())).build();
    }

}
