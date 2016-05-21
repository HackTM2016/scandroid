package com.scandroid.repository;

import com.scandroid.domain.Scan;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Scan entity.
 */
@SuppressWarnings("unused")
public interface ScanRepository extends JpaRepository<Scan,Long> {

}
