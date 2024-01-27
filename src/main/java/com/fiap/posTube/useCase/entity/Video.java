package com.fiap.posTube.useCase.entity;

import com.fiap.posTube.useCase.DTO.VideoDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class Video {
    @Id
    private String id;
    private String title;
    private String description;
    private String urlVideo;
    private LocalDateTime publicationDate;
    private Category category;
    private Integer views;

    public Video() {
    }

    public Video(VideoDTO videoDTO) {
        this.title = videoDTO.title();
        this.description = videoDTO.description();
        this.urlVideo = videoDTO.urlVideo();
        this.category = videoDTO.category();
        this.views = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }
}

