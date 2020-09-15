package com.jrp.pma.entities;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Video {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="video_seq")
	private long id;

    private String title;
    private String imgUrl;
    private String videoUrl;
    private String userUrl;
    private String duration;
    private String viewsCount;

    // spring data auditing https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.auditing
    // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.auditing
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = true, updatable = true)
    @CreatedDate
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = true)
    @LastModifiedDate
    private Date updatedAt;

	// rule of thumb, excluse CascadeType.REMOVE for many to many to avoid deleting more than necessary data
    /*
        optional = false is only for checking this constraint in runtime. nullable = false creates database constraint.
        For applications, to also set optional = false makes sense, because it is evaluated faster than
         going to database and check that constraint there
     */
    //https://www.callicoder.com/hibernate-spring-boot-jpa-one-to-many-mapping-example/
    @ManyToOne(optional = true)
    @JoinColumn(name="star_id", nullable = true)
	private Star star;

    public Video(){}

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(String viewsCount) {
        this.viewsCount = viewsCount;
    }

    public Star getStar() {
        return star;
    }

    public void setStar(Star star) {
        this.star = star;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", userUrl='" + userUrl + '\'' +
                ", duration='" + duration + '\'' +
                ", viewsCount='" + viewsCount + '\'' +
                ", star=" + star +
                '}';
    }
}
