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
        var totalDeVideos = videoService.getAllVideos().count();
        var totalFavoritesVideo = userService.getTotalFavoritesCount();
        var averageViews = 10;
        ReportDTO reportDTO = new ReportDTO(totalDeVideos, totalFavoritesVideo, averageViews);
        return Mono.just(reportDTO);
    }
}
