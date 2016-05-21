package com.scandroid.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Link.
 */
@Entity
@Table(name = "link")
public class Link implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "post_data")
    private String postData;

    @Column(name = "suspect")
    private Boolean suspect;

    @ManyToOne
    private Scan scan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPostData() {
        return postData;
    }

    public void setPostData(String postData) {
        this.postData = postData;
    }

    public Boolean isSuspect() {
        return suspect;
    }

    public void setSuspect(Boolean suspect) {
        this.suspect = suspect;
    }

    public Scan getScan() {
        return scan;
    }

    public void setScan(Scan scan) {
        this.scan = scan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Link link = (Link) o;
        if(link.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, link.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Link{" +
            "id=" + id +
            ", url='" + url + "'" +
            ", postData='" + postData + "'" +
            ", suspect='" + suspect + "'" +
            '}';
    }
}
