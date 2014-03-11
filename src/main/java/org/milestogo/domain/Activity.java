package org.milestogo.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by shekhargulati on 04/03/14.
 */
@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 140)
    private String status;

    @NotNull
    private long distanceCovered;

    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date postedAt = new Date();

    @ManyToOne
    private Profile postedBy;


    public Activity() {
    }

    public Activity(String status, long distanceCovered, Profile postedBy) {
        this.status = status;
        this.distanceCovered = distanceCovered;
        this.postedBy = postedBy;
    }

    public Activity(Long id, String status, long distanceCovered) {
        this.id = id;
        this.status = status;
        this.distanceCovered = distanceCovered;
    }

    public Activity(Long id, String status, long distanceCovered, Date postedAt) {
        this.id = id;
        this.status = status;
        this.distanceCovered = distanceCovered;
        this.postedAt = postedAt;
    }

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getDistanceCovered() {
        return distanceCovered;
    }

    public void setDistanceCovered(long distanceCovered) {
        this.distanceCovered = distanceCovered;
    }

    public Date getPostedAt() {
        return postedAt;
    }

    public Profile getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(Profile postedBy) {
        this.postedBy = postedBy;
    }
}
