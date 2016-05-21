package com.scandroid.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Scan.
 */
@Entity
@Table(name = "scan")
public class Scan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "updated")
    private LocalDate updated;

    @Column(name = "success")
    private Boolean success;

    @OneToMany(mappedBy = "scan")
    @JsonIgnore
    private Set<Link> links = new HashSet<>();

    @ManyToOne
    private Application application;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDate updated) {
        this.updated = updated;
    }

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Set<Link> getLinks() {
        return links;
    }

    public void setLinks(Set<Link> links) {
        this.links = links;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Scan scan = (Scan) o;
        if(scan.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, scan.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Scan{" +
            "id=" + id +
            ", updated='" + updated + "'" +
            ", success='" + success + "'" +
            '}';
    }
}
