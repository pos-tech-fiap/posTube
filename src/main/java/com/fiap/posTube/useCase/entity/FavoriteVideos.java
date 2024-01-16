package com.fiap.posTube.useCase.entity;

public class FavoriteVideos {
    private String videoId;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public FavoriteVideos(String videoId) {
        this.videoId = String.valueOf(videoId);
    }
}
