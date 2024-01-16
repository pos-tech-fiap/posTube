package com.fiap.posTube.service;

import com.fiap.posTube.useCase.DTO.ReportDTO;
import com.fiap.posTube.useCase.entity.Video;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class ReportServiceTest {

    @Mock
    private VideoService videoService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void ShouldGetReport() {
        long totalVideos = 0L;
        long totalFavorites = 0L;
        double averageViews = 0.0;

        Mockito.when(videoService.getAllVideos()).thenReturn(Flux.fromIterable(Arrays.asList(new Video(), new Video())));
        Mockito.when(userService.getTotalFavoritesCount()).thenReturn(Mono.just(totalFavorites));

        Mono<ReportDTO> reportDTOMono = reportService.getReport();

        ReportDTO reportDTO = reportDTOMono.block();

        assertThat(reportDTO).isNotNull();
        assertThat(reportDTO.getTotalVideos()).isEqualTo(totalVideos);
        assertThat(reportDTO.getTotalFavoriteVideos()).isEqualTo(totalFavorites);
        assertThat(reportDTO.getAverageViews()).isEqualTo(averageViews);
    }
}