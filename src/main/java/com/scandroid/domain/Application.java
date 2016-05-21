package com.scandroid.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Application.
 */
@Entity
@Table(name = "application")
public class Application implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "package_name", nullable = false)
    private String packageName;

    @Column(name = "url")
    private String url;

    @Lob
    @Column(name = "icon")
    private byte[] icon;

    @Column(name = "icon_content_type")
    private String iconContentType;

    @Lob
    @Column(name = "apk")
    private byte[] apk;

    @Column(name = "apk_content_type")
    private String apkContentType;

    @Column(name = "version")
    private String version;

    @OneToMany(mappedBy = "application")
    @JsonIgnore
    private Set<Scan> scans = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public String getIconContentType() {
        return iconContentType;
    }

    public void setIconContentType(String iconContentType) {
        this.iconContentType = iconContentType;
    }

    public byte[] getApk() {
        return apk;
    }

    public void setApk(byte[] apk) {
        this.apk = apk;
    }

    public String getApkContentType() {
        return apkContentType;
    }

    public void setApkContentType(String apkContentType) {
        this.apkContentType = apkContentType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Set<Scan> getScans() {
        return scans;
    }

    public void setScans(Set<Scan> scans) {
        this.scans = scans;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Application application = (Application) o;
        if(application.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, application.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Application{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", packageName='" + packageName + "'" +
            ", url='" + url + "'" +
            ", icon='" + icon + "'" +
            ", iconContentType='" + iconContentType + "'" +
            ", apk='" + apk + "'" +
            ", apkContentType='" + apkContentType + "'" +
            ", version='" + version + "'" +
            '}';
    }
}
