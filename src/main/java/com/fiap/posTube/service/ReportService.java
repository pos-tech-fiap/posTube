package com.fiap.posTube.service;

import com.fiap.posTube.useCase.DTO.ReportDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ReportService {
    @Autowired
    private VideoService videoService;

    @Autowired
    private  UserService userService;

    public Mono<ReportDTO> getReport() {
        return Mono.zip(
                videoService.getAllVideos().count(),
                userService.getTotalFavoritesCount(),
                (totalDeVideos, totalFavoritesVideo) -> {
                    var averageViews = 10;
                    return new ReportDTO(totalDeVideos, totalFavoritesVideo, averageViews);
                });
    }
}
