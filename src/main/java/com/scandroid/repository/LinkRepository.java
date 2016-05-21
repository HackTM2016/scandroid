package com.scandroid.repository;

import com.scandroid.domain.Link;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Link entity.
 */
@SuppressWarnings("unused")
public interface LinkRepository extends JpaRepository<Link,Long> {

//    @Query("SELECT DISTINCT l from Link l LEFT JOIN l.scan s LEFT JOIN FETCH s.application a WHERE a.packageName= (:packageName)")
//    List<Link> findByApplicationName(@Param("packageName") String packageName);
}
