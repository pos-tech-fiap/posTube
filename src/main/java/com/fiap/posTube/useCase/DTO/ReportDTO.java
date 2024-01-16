package com.fiap.posTube.useCase.DTO;

import reactor.core.publisher.Mono;

public class ReportDTO {
    private long totalVideos;
    private long totalFavoriteVideos;
    private double averageViews;

    public ReportDTO(Mono<Long> totalDeVideos, Mono<Long> totalFavoritesVideo, int averageViews) {

    }

    public ReportDTO(long totalVideos, long totalFavoriteVideos, double averageViews) {
        this.totalVideos = totalVideos;
        this.totalFavoriteVideos = totalFavoriteVideos;
        this.averageViews = averageViews;
    }

    public long getTotalVideos() {
        return totalVideos;
    }

    public void setTotalVideos(long totalVideos) {
        this.totalVideos = totalVideos;
    }

    public long getTotalFavoriteVideos() {
        return totalFavoriteVideos;
    }

    public void setTotalFavoriteVideos(long totalFavoriteVideos) {
        this.totalFavoriteVideos = totalFavoriteVideos;
    }

    public double getAverageViews() {
        return averageViews;
    }

    public void setAverageViews(double averageViews) {
        this.averageViews = averageViews;
    }
}
