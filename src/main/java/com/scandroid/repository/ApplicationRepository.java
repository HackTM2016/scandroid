package com.scandroid.repository;

import com.scandroid.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Application entity.
 */
@SuppressWarnings("unused")
public interface ApplicationRepository extends JpaRepository<Application,Long> {
    //@Query("SELECT DISTINCT  from Link l LEFT JOIN l.scan s JOIN FETCH s.application a WHERE a.packageName= (:packageName)")
    @Query("SELECT DISTINCT a from Application a LEFT JOIN FETCH a.scans s LEFT JOIN FETCH s.links l WHERE a.packageName= (:packageName)")
    List<Application> findAllByApplicationName(@Param("packageName") String packageName);
}
